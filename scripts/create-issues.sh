#!/usr/bin/env bash
#
# Creates the GitHub issues tracked in AUDITORIA.md.
#
# Written as a script because the environment the audit ran in has no network access to
# GitHub — no `gh`, no API. Run it once from the repo root:
#
#     ./scripts/create-issues.sh
#
# Requires: gh (authenticated).  Dry run:  DRY_RUN=1 ./scripts/create-issues.sh
#
set -euo pipefail

DRY_RUN="${DRY_RUN:-0}"

if [[ "$DRY_RUN" != "1" ]] && ! command -v gh >/dev/null 2>&1; then
    echo "gh not found. Install https://cli.github.com/ or run with DRY_RUN=1" >&2
    exit 1
fi

issue() {
    local state="$1" title="$2" labels="$3" body="$4"
    if [[ "$DRY_RUN" == "1" ]]; then
        printf '\n=== [%s] %s\n    labels: %s\n%s\n' "$state" "$title" "$labels" "$body"
        return
    fi

    local url
    url=$(gh issue create --title "$title" --label "$labels" --body "$body")
    echo "created: $url"

    # Problems already fixed are filed for the record, then closed immediately.
    if [[ "$state" == "fixed" ]]; then
        gh issue close "$url" --comment "Fixed. See AUDITORIA.md for the analysis and the commits on this branch."
        echo "  closed (already fixed)"
    fi
}

ensure_labels() {
    [[ "$DRY_RUN" == "1" ]] && return
    gh label create bug          --color d73a4a --description "Something is broken"      2>/dev/null || true
    gh label create combat       --color 5319e7 --description "Combat stats and damage"  2>/dev/null || true
    gh label create effects      --color 1d76db --description "Status effects and buffs" 2>/dev/null || true
    gh label create multiplayer  --color 0e8a16 --description "Only shows with 2+ players" 2>/dev/null || true
    gh label create tech-debt    --color fbca04 --description "Design and maintenance"   2>/dev/null || true
    gh label create build        --color c5def5 --description "Build and tooling"        2>/dev/null || true
}

ensure_labels

# ─────────────────────────────────────────────────────────────────────────────
# Invisibility — the reported bug, split by root cause
# ─────────────────────────────────────────────────────────────────────────────

issue fixed "Invisibility hides the player from their own client" "bug,effects" \
'`StatusEffectHelper.applyInvisibility` looped over `world.getPlayerRefs()` and called
`hidePlayer(uuid)` on **every** observer — including the invisible player themselves.

`HiddenPlayersManager` is per viewer (it holds the viewer in a `playerRef` field and a
`Set<UUID> hiddenPlayers`), so this told the player'"'"'s own client to stop tracking their own
entity.

**Symptoms**
- You cannot see your own character.
- Your character falls through the ground and dies — a client that is not tracking its own
  entity has nothing to collide with.

**Fix:** the loop now skips the target. Ownership of the state moved to `InvisibilityManager`.'

issue fixed "Players who join mid-effect still see the invisible player" "bug,effects,multiplayer" \
'Hiding was a one-shot broadcast to whoever happened to be online when the effect was applied.
Anyone connecting afterwards was never told, so they saw the invisible player normally.

Note that `RuneCoreHudManager` and `CombatStatsManager` already hook `PlayerReadyEvent`;
invisibility did not.

**Fix:** `InvisibilityManager` keeps the authoritative set and catches new arrivals up on join.'

issue fixed "Dying or disconnecting leaves a player invisible forever" "bug,effects,multiplayer" \
'`revertInvisibility` only ran from the buff'"'"'s `onExpire`. Two things stopped it:

1. `EffectTickSystem.tick` dropped a buff whose entity ref had gone invalid with
   `it.remove(); continue;` — without running `onExpire`.
2. `ActiveBuff.tick` guarded `onExpire` behind `ref.isValid()` anyway.

`EffectTimerListener.onPlayerDisconnect` only forgot the player'"'"'s world; it never cancelled
buffs.

Result: the UUID stayed in every observer'"'"'s hidden set for the rest of **their** session. The
player could reconnect and still be invisible to everyone who had been online.

**Fix:** `ActiveBuff.expire` runs unconditionally, `EffectTickSystem` calls it on the orphaned
and cancelled paths, the invisibility revert is keyed on the player UUID instead of the ref, and
`InvisibilityManager` clears the flag on disconnect and on reconnect.'

issue open "Invisibility gives the player no feedback that it is active" "effects" \
'With the self-hide bug fixed you now see yourself normally, which means there is no signal at
all that the effect is running.

Rendering yourself translucent is **not possible** with what the server API exposes:
`HiddenPlayersManager` is binary (hidden/shown) and `protocol.Opacity` is block and fluid
lighting — the classes referencing it are all chunk, lighting, `BlockType` and `Fluid`.

Workable alternative: attach a visual `EntityEffect` to the player'"'"'s own entity. Because every
other viewer has them hidden, such a cue is inherently self-only. Needs a decision on which
asset — `Glowing` exists but "shining" reads as the opposite of invisible.'

# ─────────────────────────────────────────────────────────────────────────────
# Open items carried over from the audit
# ─────────────────────────────────────────────────────────────────────────────

issue open "build.gradle hardcodes the author's home directory" "build" \
'```gradle
destinationDirectory = file("/home/cookie/.var/app/com.hypixel.HytaleLauncher/.../Mods/")
```

In a library this breaks the build for anyone else and for CI. Should be a relative path with an
optional override via a Gradle property.

AUDITORIA.md § 2.2'

issue open "Singletons are published through constructor side effects" "tech-debt" \
'`CombatStatsRegistry`, `CreatureCombatRegistry`, `CombatStatsManager` and `RuneCoreHudManager`
all do `instance = this` in their constructor, with a non-volatile `static` field.

- Constructing a second instance silently hijacks the singleton.
- `get()` returns `null` if called before `setup()`, so every caller has to null-check.
- There is no formal memory barrier between the setup thread and the tick threads.

`AttributeRegistry` shows the alternative: static, self-initialising, no construction order to
get wrong.

AUDITORIA.md § 2.4'

issue open "PlayerStats returns -1f as a sentinel and its futures never time out" "tech-debt" \
'`getStat` completes the future with `-1f` when the stat cannot be read, which is
indistinguishable from a real value. Worse, if the world unloads between scheduling and
execution the future never completes at all — anything calling `.join()` hangs forever.

`getMaxMana()` also returns a hardcoded `100f` with a `// Default for now`.

AUDITORIA.md § 2.6'

issue open "StatusEffectHelper mixes seven unrelated effect families in one class" "tech-debt" \
'242 lines, 22 public static methods covering bleeding, burn, nausea, haste, mining fatigue,
strength/weakness/resistance and invisibility — no shared behaviour between them.

Since the class is public API, the split should keep it as a thin facade delegating to
per-family classes.'

issue open "calculateFinalDamage has a side effect" "tech-debt,combat" \
'It drains `shieldHP` through `absorbDamage`. The name reads as a pure calculation, so calling it
twice to "preview" damage really spends the shield.

The behaviour is documented in the Javadoc and pinned by a unit test, but calculation and
application should be separated.

AUDITORIA.md § 2.3'

echo
echo "Done. Re-run with DRY_RUN=1 to preview without touching GitHub."
