# 📖 RuneCore — Technical API Reference

> **Package:** `com.cookie.runecore`
> **Target:** Hytale Server Modding API
> **Version:** In Development

This document covers every public class, method, parameter, and return type of the RuneCore API. It is intended for experienced modders who want full technical detail.

---

## Table of Contents

1. [RuneCore (Engine)](#1-runecore-engine)
2. [CastContext](#2-castcontext)
3. [RuneEffect](#3-runeeffect)
4. [ActiveBuff & Builder](#4-activebuff)
5. [Spell](#5-spell)
6. [Essence](#6-essence)
7. [GameResource](#7-gameresource)
8. [RuneElement](#8-runeelement)
9. [IEffectAction](#9-ieffectaction)
10. [PlayerStats](#10-playerstats)
11. [PlayerDataComponent](#11-playerdatacomponent)
12. [EffectHelper](#12-effecthelper)
13. [MovementHelper](#13-movementhelper)
14. [StatusEffectHelper](#14-statuseffecthelper)
15. [VisualEffectHelper](#15-visualeffecthelper)
16. [StatHelper](#16-stathelper)
17. [EffectTickSystem](#17-effectticksystem)
18. [Event System](#18-event-system)

---

## 1. RuneCore (Engine)

**File:** `system/RuneCore.java`
**Pattern:** Singleton

The central registry and execution engine. All essences, effects, spells, and resources are registered here.

### Getting the Instance

```java
RuneCore core = RuneCore.get();
```

### Methods

| Method | Parameters | Return | Description |
|---|---|---|---|
| `get()` | — | `RuneCore` | Returns the singleton instance. |
| `initDefaults()` | — | `void` | Registers default mana resource (`arcane_mana`, max 100, regen 5) and loads all `Core*` content classes. |
| `registerEssence(essence)` | `Essence essence` | `void` | Registers an essence by its `id`. |
| `registerResource(res)` | `GameResource res` | `void` | Registers a game resource by its `id`. |
| `registerEffect(effect)` | `RuneEffect effect` | `void` | Registers an effect by its `id`. |
| `registerSpell(spell)` | `Spell spell` | `void` | Registers a spell by its `id`. |
| `getEffect(id)` | `String id` | `RuneEffect` or `null` | Looks up a registered effect. |
| `castSpell(spellId, ctx)` | `String spellId`, `CastContext ctx` | `boolean` | Executes the full spell pipeline (see below). Returns `true` on success. |
| `on(eventType, listener)` | `String eventType`, `Consumer<CastContext> listener` | `void` | Subscribes to a spell lifecycle event. |

### Spell Casting Pipeline

When `castSpell()` is called, the following happens in order:

```
1. Spell lookup          → returns false if not found
2. spell.checkConditions → returns false if condition fails
3. emit("spell:pre_cast")
4. Deduct mana from source PlayerStats
5. emit("resource:consume")
6. For each effect in spell.getEffectIds():
   → effect.execute(ctx)
   → emit("effect:applied")
7. emit("spell:post_cast")
8. return true
```

---

## 2. CastContext

**File:** `api/CastContext.java`

Transient state object passed through the entire spell pipeline. Shared between all effects within a single cast.

### Constructor

```java
new CastContext(PlayerRef source, Object target, World world, double power)
```

### Fields (all `public`)

| Field | Type | Description |
|---|---|---|
| `source` | `PlayerRef` | The caster. |
| `target` | `Object` | The target. Usually a `Ref<EntityStore>` for entities. |
| `world` | `World` | The game world instance. |
| `power` | `double` | Multiplier for effect strength. Can be modified by event listeners. |
| `metadata` | `Map<String, Object>` | Shared key-value store for passing data between effects in the same spell. |

### Methods

| Method | Return | Description |
|---|---|---|
| `getOwner()` | `PlayerRef` | Alias for `source`. |

### Usage Example

```java
CastContext ctx = new CastContext(playerRef, targetEntityRef, world, 1.5);
ctx.metadata.put("custom_flag", true);
```

---

## 3. RuneEffect

**File:** `api/RuneEffect.java`

The logic unit of the system. Represents a single effect that can be instant or duration-based.

### Constructors

```java
// Duration-based effect (no action yet, configure with builder methods)
new RuneEffect(String id, int defaultDurationTicks)

// Instant or timed with inline action
new RuneEffect(String id, boolean isInstant, int durationTicks, IEffectAction action)

// Native Hytale entity effect wrapper
new RuneEffect(String id, String nativeEffectId, int durationTicks)
```

### Builder Methods (Fluent API)

All return `this` for chaining.

| Method | Parameter | Description |
|---|---|---|
| `withBuff(factory)` | `Function<CastContext, ActiveBuff>` | Registers a buff factory. When `execute()` is called, produces an `ActiveBuff` and registers it in `EffectTickSystem`. |
| `withAction(action)` | `IEffectAction` | Sets the instant action lambda. |
| `withNativeEffect(id)` | `String nativeEffectId` | Sets a native Hytale `EntityEffect` to apply (e.g., `"Speed"`, `"Poison"`). |
| `withAsset(path)` | `String assetPath` | Sets the asset path. Also sets `nativeEffectId` if not already set. |
| `withAmplifier(amp)` | `int amplifier` | Sets the amplifier level. |

### Execution Flow

When `execute(CastContext ctx)` is called:

```
1. If action != null      → action.apply(ctx)
2. If buffFactory != null  → creates ActiveBuff, registers in EffectTickSystem
3. If nativeEffectId != null && target is Ref → applies native Hytale effect
   (tries bare name first, then "runecore:" prefix)
```

### Getters

| Method | Return |
|---|---|
| `getId()` | `String` |
| `getDurationTicks()` | `int` |
| `isInstant()` | `boolean` |
| `getAmplifier()` | `int` |
| `getAssetPath()` | `String` |

### Example: Custom Poison Effect

```java
RuneEffect poison = new RuneEffect("poison", false, 200, null)
    .withNativeEffect("Poison")
    .withBuff(ctx -> ActiveBuff.builder(
            ctx.source.getUuid().toString(), "poison", 200)
        .interval(25)
        .onTick(ref -> StatHelper.subtractHealth(ref, 1.0f))
        .onExpire(ref -> System.out.println("Poison expired"))
        .build()
    );
```

---

## 4. ActiveBuff

**File:** `api/ActiveBuff.java`

Represents a ticking buff/debuff attached to a player. Managed by `EffectTickSystem`.

### Fields

| Field | Type | Description |
|---|---|---|
| `effectId` | `String` (final) | The effect identifier. |
| `playerId` | `String` (final) | UUID string of the affected player. |
| `remainingTicks` | `int` | Ticks left before expiry. Decremented each tick. |
| `intervalTicks` | `int` (final) | How often `onTick` fires (every N ticks). `0` = never. |
| `ticksSinceLastApply` | `int` | Counter since last `onTick` call. |

### Method: `tick(Ref<EntityStore> ref)`

Called every server tick by `EffectTickSystem`.

**Returns:** `true` if buff is still alive, `false` if expired (will be removed).

**Logic:**
```
remainingTicks--
ticksSinceLastApply++
if ticksSinceLastApply >= intervalTicks → reset counter, call onTick
if remainingTicks <= 0 → call onExpire, return false
```

### Builder Pattern

```java
ActiveBuff buff = ActiveBuff.builder(playerId, effectId, durationTicks)
    .interval(25)                          // fire onTick every 25 ticks
    .onTick(ref -> { /* damage/heal */ })  // called every interval
    .onExpire(ref -> { /* cleanup */ })    // called on expiry
    .build();
```

| Builder Method | Parameter | Description |
|---|---|---|
| `interval(ticks)` | `int` | Sets tick interval. |
| `onTick(action)` | `Consumer<Ref<EntityStore>>` | Lambda executed every interval. |
| `onExpire(action)` | `Consumer<Ref<EntityStore>>` | Lambda executed on buff expiry. |
| `build()` | — | Returns the constructed `ActiveBuff`. |

---

## 5. Spell

**File:** `api/Spell.java`

Blueprint that connects costs, conditions, and effects.

### Constructor

```java
new Spell(String id, Predicate<CastContext> conditions)
```

The `conditions` predicate is checked before casting. Return `false` to block the cast.

### Methods (Fluent)

| Method | Parameters | Return | Description |
|---|---|---|---|
| `addCost(resourceId, amount)` | `String`, `int` | `Spell` | Adds a resource cost (e.g., `"mana"`, `10`). |
| `addEssenceRequirement(element, amount)` | `RuneElement`, `int` | `Spell` | Adds an essence cost by element. |
| `addEffect(effectId)` | `String` | `Spell` | Adds an effect to execute on cast. |

### Getters

| Method | Return |
|---|---|
| `getId()` | `String` |
| `getResourceCost()` | `Map<String, Integer>` |
| `getEffectIds()` | `List<String>` |
| `checkConditions(ctx)` | `boolean` |

### Full Example

```java
Spell fireball = new Spell("fireball", ctx -> ctx.target != null)
    .addCost("mana", 15)
    .addEssenceRequirement(RuneElement.FIRE, 1)
    .addEffect("explosion")
    .addEffect("burn");

RuneCore.get().registerSpell(fireball);
```

---

## 6. Essence

**File:** `api/Essence.java` — Java `record`

```java
public record Essence(String id, RuneElement element, int tier, double stability, Set<String> tags)
```

### Compact Constructor

```java
new Essence("fire_T1", RuneElement.FIRE, 1)
// stability defaults to 1.0, tags defaults to empty HashSet
```

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier (e.g., `"essence_fire_weak"`). |
| `element` | `RuneElement` | Elemental category. |
| `tier` | `int` | Power level (1, 2, 3...). |
| `stability` | `double` | Fizzle rate modifier. Default `1.0`. |
| `tags` | `Set<String>` | Custom tags for categorization. |

---

## 7. GameResource

**File:** `api/GameResource.java` — Java `record`

```java
public record GameResource(String id, double max, double regenRate, String overflowPenalty)
```

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier (e.g., `"arcane_mana"`). |
| `max` | `double` | Maximum cap. |
| `regenRate` | `double` | Passive regen per tick. |
| `overflowPenalty` | `String` | Penalty type when exceeding max (e.g., `"burnout"`, `"none"`). |

---

## 8. RuneElement

**File:** `api/RuneElement.java` — `enum`

```java
// Basic
FIRE, EARTH, WIND, WATER, ICE, LIGHTNING
// Advanced
LIGHT, SHADOW, LIFE, DEATH, MIND, BLOOD
// Unstable
CHAOS, AETHER, VOID, TIME
// Chemical
METAL, CRYSTAL, POISON, ACID
```

---

## 9. IEffectAction

**File:** `api/IEffectAction.java` — `@FunctionalInterface`

```java
void apply(CastContext ctx);
```

Lambda that contains the actual game logic for an effect.

```java
IEffectAction heal = (ctx) -> {
    Ref<EntityStore> ref = (Ref<EntityStore>) ctx.target;
    StatHelper.addHealth(ref, (float)(4.0 * ctx.power));
};
```

---

## 10. PlayerStats

**File:** `api/PlayerStats.java`

Wrapper for manipulating entity stats (health, mana, stamina, speed) via Hytale's `EntityStatMap` and `MovementManager`.

### Constructors

```java
new PlayerStats(Ref<EntityStore> playerRef)
new PlayerStats(PlayerRef playerRef)   // convenience, calls .getReference()
```

### Constants

| Constant | Value | Description |
|---|---|---|
| `DEFAULT_SPEED` | `5.5f` | Default walk speed. |
| `MIN_SPEED` | `0.0f` | Floor for speed clamping. |
| `MAX_SPEED` | `100.0f` | Ceiling for speed clamping. |
| `MAX_STAT` | `1000.0f` | Ceiling for all stats. |

### Health Methods

| Method | Parameter | Description |
|---|---|---|
| `getHealth()` | — | Returns `CompletableFuture<Float>`. |
| `addHealth(amount)` | `float` | Adds HP (clamped to `MAX_STAT`). |
| `setHealth(amount)` | `float` | Sets HP directly. |
| `subtractHealth(amount)` | `float` | Removes HP (clamped to `0`). |

### Mana Methods

| Method | Parameter | Description |
|---|---|---|
| `getMana()` | — | Returns `CompletableFuture<Float>`. |
| `getMaxMana()` | — | Returns `CompletableFuture<Float>` (hardcoded `100f`). |
| `addMana(amount)` | `float` | Adds mana. |
| `setMana(amount)` | `float` | Sets mana. |
| `subtractMana(amount)` | `float` | Removes mana. |

### Stamina Methods

| Method | Parameter | Description |
|---|---|---|
| `getStamina()` | — | Returns `CompletableFuture<Float>`. |
| `addStamina(amount)` | `float` | Adds stamina. |
| `setStamina(amount)` | `float` | Sets stamina. |
| `subtractStamina(amount)` | `float` | Removes stamina. |

### Speed Methods

| Method | Parameter | Description |
|---|---|---|
| `getSpeed()` | — | Returns `CompletableFuture<Float>` via `MovementManager`. |
| `addSpeed(amount)` | `float` | Increases base speed. |
| `setSpeed(amount)` | `float` | Sets base speed (clamped `0–100`). |
| `subtractSpeed(amount)` | `float` | Decreases base speed. |
| `resetSpeed()` | — | Resets to `DEFAULT_SPEED` (`5.5`). |

> **Note:** All stat modifications are scheduled on the world thread via `world.execute()` and sync movement changes to the client via `UpdateMovementSettings` packet.

---

## 11. PlayerDataComponent

**File:** `api/PlayerDataComponent.java`

Persistent component attached to entities via Hytale's ECS. Stores RuneCore-specific player state that survives across ticks.

**Component ID:** `"runecore:player_data"`

### Persisted Fields

| Field | Type | Default | Description |
|---|---|---|---|
| `mana` | `float` | `100.0` | Current mana. |
| `maxMana` | `float` | `100.0` | Mana ceiling. |
| `activeGrimoireId` | `String` | `null` | ID of equipped grimoire. |
| `activeSpellIndex` | `int` | `0` | Selected spell slot. |
| `frozen` | `boolean` | `false` | Frozen state flag. |
| `frozenRotX/Y/Z` | `float` | `0.0` | Saved rotation when frozen. |
| `nausea` | `boolean` | `false` | Nausea state flag. |
| `nauseaTime` | `float` | `0.0` | Accumulated nausea time for camera rotation. |
| `blinded` | `boolean` | `false` | Blindness state flag. |
| `glowing` | `boolean` | `false` | Glowing state flag. |
| `nightVision` | `boolean` | `false` | Night vision state flag. |

All fields have standard getter/setter pairs. Data is serialized via `BuilderCodec`.

---

## 12. EffectHelper

**File:** `api/EffectHelper.java`

Shared utilities used by all effect helper classes. Contains movement modification, HUD updates, particle spawning, and world-thread execution.

### Constants

| Constant | Value |
|---|---|
| `DEFAULT_SPEED` | `5.5f` |
| `DEFAULT_JUMP_FORCE` | `8.0f` |
| `DEFAULT_MASS` | `1.0f` |
| `DEFAULT_DRAG` | `0.05f` |
| `DEFAULT_AIR_DRAG_MAX` | `0.08f` |

### Public Methods

| Method | Parameters | Description |
|---|---|---|
| `modifyMovement(ref, modifier)` | `Ref<EntityStore>`, `MovementModifier` | Safely modifies movement settings on the world thread and syncs to client. |
| `worldExecute(ref, action)` | `Ref<EntityStore>`, `Runnable` | Runs a `Runnable` on the world thread with entity validity check. |

### MovementModifier Interface

```java
@FunctionalInterface
public interface MovementModifier {
    void apply(MovementSettings settings);
}
```

### Usage

```java
EffectHelper.modifyMovement(ref, settings -> {
    settings.baseSpeed = 10.0f;
    settings.jumpForce = 15.0f;
});
```

### Package-Private Utilities (used by sibling helpers)

| Method | Description |
|---|---|
| `syncSettings(store, ref, settings)` | Sends `UpdateMovementSettings` packet to client. |
| `updateHud(ref, action)` | Gets `RuneCoreHud` for the player and applies the action. |
| `spawnParticleEffect(ref, particleId)` | Spawns particle at entity position. |
| `spawnParticleEffect(ref, id, offX, offY, offZ)` | Spawns particle with offset. |
| `getEffectIndex(id)` | Looks up `EntityEffect` asset index with `runecore:` fallback. |
| `sendCameraLock(playerRef, locked)` | Locks/unlocks player camera rotation. |

---

## 13. MovementHelper

**File:** `api/MovementHelper.java`

Static helpers for movement-based status effects.

| Method | What it does |
|---|---|
| `applySlowness(ref)` | `baseSpeed -= 2.0` (min `1.0`). |
| `revertSlowness(ref)` | `baseSpeed += 2.0`. |
| `applyJumpBoost(ref)` | `jumpForce += 5.0`. |
| `revertJumpBoost(ref)` | `jumpForce -= 5.0` (min `DEFAULT_JUMP_FORCE`). |
| `applyHighJump(ref)` | `jumpForce += 10.0`. |
| `revertHighJump(ref)` | `jumpForce -= 10.0` (min `DEFAULT_JUMP_FORCE`). |
| `applySlowFalling(ref)` | `mass=0.15`, `airDragMax=0.6`, `airDragMaxSpeed=0.5`. |
| `revertSlowFalling(ref)` | Resets mass, drag to defaults. |
| `applyLevitation(ref)` | `invertedGravity=true`, `mass=0.05`, `airDragMax=3.5`. |
| `revertLevitation(ref)` | Resets defaults, clears velocity, clears ghost inputs. |
| `applyFrozen(ref)` | Saves rotation, locks camera, sets `frozen=true`, shows HUD. |
| `revertFrozen(ref)` | Unlocks camera, sets `frozen=false`, hides HUD. |
| `onFrozenTick(ref)` | Forces saved rotation each tick + spawns `hytale:ice_shards` particles. |

---

## 14. StatusEffectHelper

**File:** `api/StatusEffectHelper.java`

Static helpers for gameplay status effects.

### Bleeding

| Method | Description |
|---|---|
| `applyBleeding(ref)` | Shows bleeding HUD indicator. |
| `revertBleeding(ref)` | Hides bleeding HUD indicator. |
| `onBleedingTick(ref)` | Spawns blood particles at chest height (0.8–1.5m). |

### Burn

| Method | Description |
|---|---|
| `applyBurn(ref)` | Shows burning HUD indicator. |
| `revertBurn(ref)` | Hides burning HUD indicator. |

### Nausea

| Method | Description |
|---|---|
| `applyNausea(ref)` | Sets `nausea=true` in `PlayerDataComponent`, shows HUD. |
| `revertNausea(ref)` | Resets camera to first-person, removes native `Nausea` effect, hides HUD. |
| `onNauseaTick(ref)` | Increments `nauseaTime`, rotates camera: `yaw = (time * 4) % 360`, `pitch = sin(time * 0.15) * 20`. |

### Haste

| Method | Description |
|---|---|
| `applyHaste(ref)` | Applies `1.5x` MULTIPLICATIVE modifier to `attack_speed` and `mining_speed`. Shows HUD. |
| `revertHaste(ref)` | Removes stat modifiers. Hides HUD. |

### Mining Fatigue

| Method | Description |
|---|---|
| `applyMiningFatigue(ref)` | Applies `0.3x` MULTIPLICATIVE modifier to `attack_speed` and `mining_speed`. Shows HUD. |
| `revertMiningFatigue(ref)` | Removes stat modifiers. Hides HUD. |

### Invisibility

| Method | Description |
|---|---|
| `applyInvisibility(ref)` | Calls `hidePlayer(uuid)` on all observers in the world. |
| `revertInvisibility(ref)` | Calls `showPlayer(uuid)` on all observers in the world. |

---

## 15. VisualEffectHelper

**File:** `api/VisualEffectHelper.java`

Static helpers for visual effects (light, vision).

### Glowing

| Method | Description |
|---|---|
| `applyGlowing(ref)` | Adds `DynamicLight(radius=1, R=32, G=32, B=0)` (subtle yellow). Sets `glowing=true`. Shows HUD. |
| `revertGlowing(ref)` | Removes `DynamicLight`, removes native `Glowing` effect. Hides HUD. |

### Night Vision

| Method | Description |
|---|---|
| `applyNightVision(ref)` | Adds `DynamicLight(radius=-1, R=-1, G=-1, B=-1)` (full bright white). Sets `nightVision=true`. Shows HUD. |
| `revertNightVision(ref)` | Removes `DynamicLight`, removes native `NightVision` effect. Hides HUD. |

### Blindness

| Method | Description |
|---|---|
| `applyBlindness(ref)` | Sets `blinded=true` in `PlayerDataComponent`. Shows HUD. |
| `revertBlindness(ref)` | Sets `blinded=false`. Removes native `Blindness` effect. Hides HUD. |

---

## 16. StatHelper

**File:** `api/StatHelper.java`

Direct stat manipulation utilities.

### HP Methods

| Method | Parameters | Description |
|---|---|---|
| `addHealth(ref, amount)` | `Ref<EntityStore>`, `float` | Heals (capped at `100`). |
| `subtractHealth(ref, amount)` | `Ref<EntityStore>`, `float` | Damages (capped at `0`). |

### Stat Modifier Methods

| Method | Parameters | Description |
|---|---|---|
| `applyStatModifier(ref, statId, modId, value, type)` | `Ref`, `String`, `String`, `float`, `CalculationType` | Applies a named `StaticModifier` to a stat. |
| `removeStatModifier(ref, statId, modId)` | `Ref`, `String`, `String` | Removes a named modifier. |

**CalculationType values:** `MULTIPLICATIVE`, `ADDITIVE`

### Example

```java
// +50% attack speed
StatHelper.applyStatModifier(ref, "hytale:attack_speed", "MyBuff",
    1.5f, StaticModifier.CalculationType.MULTIPLICATIVE);

// Remove it later
StatHelper.removeStatModifier(ref, "hytale:attack_speed", "MyBuff");
```

---

## 17. EffectTickSystem

**File:** `systems/EffectTickSystem.java`
**Pattern:** Singleton

Manages all active `ActiveBuff` instances. Called every server tick.

### Methods

| Method | Parameters | Return | Description |
|---|---|---|---|
| `getInstance()` | — | `EffectTickSystem` | Singleton access. |
| `applyBuff(buff, ref)` | `ActiveBuff`, `Ref<EntityStore>` | `void` | Registers a buff. Key = `playerId|effectId`. |
| `cancelBuff(playerId, effectId)` | `String`, `String` | `void` | Removes a specific buff. |
| `cancelAllBuffs(playerId)` | `String` | `void` | Removes all buffs for a player. |
| `hasBuff(playerId, effectId)` | `String`, `String` | `boolean` | Checks if a buff is active. |
| `getBuff(playerId, effectId)` | `String`, `String` | `ActiveBuff` or `null` | Gets a specific active buff. |
| `getBuffsForPlayer(playerId)` | `String` | `List<ActiveBuff>` | Lists all active buffs for a player. |
| `tick(world)` | `World` | `void` | Ticks all active buffs. Removes expired/invalid ones. |

### Internal Key Format

```
"playerUUID|effectId"    e.g. "550e8400-...|poison"
```

---

## 18. Event System

RuneCore has a lightweight event bus for intercepting the spell pipeline.

### Subscribing

```java
RuneCore.get().on("spell:pre_cast", ctx -> {
    ctx.power *= 1.5; // Boost damage by 50%
});
```

### Available Events

| Event | When it fires | Common use |
|---|---|---|
| `spell:pre_cast` | Before conditions are checked | Modify `ctx.power`, cancel spells, add metadata. |
| `resource:consume` | After mana is deducted | Track resource usage, trigger on-low-mana effects. |
| `effect:applied` | After each individual effect executes | Chain reactions, combo tracking. |
| `spell:post_cast` | After all effects complete | Cooldown management, logging, UI updates. |

---

## Quick Start: Creating a Complete Custom Effect

```java
// 1. Get the engine
RuneCore core = RuneCore.get();

// 2. Create a custom effect with buff
RuneEffect frostbite = new RuneEffect("frostbite", false, 100, null)
    .withNativeEffect("Frozen")
    .withAction(ctx -> {
        System.out.println("Frostbite applied with power: " + ctx.power);
    })
    .withBuff(ctx -> {
        String pid = ctx.source.getUuid().toString();
        Ref<EntityStore> ref = (Ref<EntityStore>) ctx.target;
        return ActiveBuff.builder(pid, "frostbite", 100)
            .interval(20)
            .onTick(r -> {
                StatHelper.subtractHealth(r, 0.5f);
                MovementHelper.applySlowness(r);
            })
            .onExpire(r -> {
                MovementHelper.revertSlowness(r);
            })
            .build();
    });

// 3. Register
core.registerEffect(frostbite);

// 4. Create a spell that uses it
Spell iceBlast = new Spell("ice_blast", ctx -> ctx.target != null)
    .addCost("mana", 20)
    .addEssenceRequirement(RuneElement.ICE, 1)
    .addEffect("frostbite");

core.registerSpell(iceBlast);

// 5. Cast it
CastContext ctx = new CastContext(player, target, world, 1.0);
core.castSpell("ice_blast", ctx);
```
