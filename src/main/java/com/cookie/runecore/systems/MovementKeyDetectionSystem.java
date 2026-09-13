package com.cookie.runecore.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Infers, per player, when each WASD direction is being "held" (plus space/crouch), and posts a
 * chat message when a direction starts and when it stops.
 *
 * <p>The server API has no raw key event — the server never receives "W was pressed", only the
 * movement result the client already computed each tick (the {@code ClientMovement} packet). Space
 * and Shift/Ctrl are easy: {@code MovementStates.jumping}/{@code crouching} come ready-made from
 * that packet — the same source {@code ChildCarryHelper.isCrouching()} reads from in SimTale, never
 * a reported problem. W/A/S/D are the hard part, and took a long back-and-forth to get right — the
 * short version, kept here so the next person touching this file doesn't repeat the same dead ends:
 *
 * <ol>
 *   <li>The obvious approach is: read {@link Velocity#getClientVelocity()} (world-space X/Z) and
 *       rotate it into local space using the player's look yaw, so "forward" lines up with the
 *       camera. Tried this against {@code TransformComponent}'s body yaw, then against {@code
 *       HeadRotation}'s camera yaw, each time chasing a small leak with a hand-calibrated angle
 *       offset (up to ~6.9°, confirmed against four independent isolated single-key readings).
 *   <li>That calibration held up fine right after spawn but drifted further off the more the player
 *       actually moved/turned, up to a full ~90° mismatch. A continuous per-tick log (server file
 *       only, {@link #TICK_LOG_INTERVAL}) confirmed why: in this build, neither {@code HeadRotation}
 *       nor {@code TransformComponent}'s yaw, nor {@code PlayerRef#getHeadRotation()}, ever change at
 *       all — all three sat frozen at exactly {@code 0.0°} for the entire session regardless of how
 *       much the player actually turned or moved. None of the three yaw sources this server exposes
 *       track the live camera, so no fixed (or even per-session) calibration offset against any of
 *       them can work.
 *   <li><b>Current approach — self-calibrating, no yaw source needed at all:</b> the first tick a
 *       player's speed crosses {@link #DEADZONE} after being stopped, whatever direction they're
 *       moving in *becomes* "forward" for that movement (see {@link Anchor}). Every following tick,
 *       until they stop again, direction is judged relative to that anchor instead of to any
 *       (broken) absolute yaw. This correctly tracks switching keys (e.g. releasing W and pressing D)
 *       within one continuous movement, but it has one known, unavoidable blind spot: it cannot tell
 *       *which* key started a movement — whatever key(s) are held at that first tick are always
 *       reported as "W" (and, if two were held together, only the dominant one). That trade-off was
 *       discussed and accepted rather than continuing to chase a yaw source this server build simply
 *       doesn't provide.
 *   <li><b>Investigated and ruled out — {@code wishMovement}:</b> {@code ClientMovement} also carries
 *       a raw {@code wishMovement} field, queued as {@code PlayerInput.WishMovement} — but reading the
 *       actual Hytale server source (not just decompiled bytecode) settled it: {@code
 *       KnockbackPredictionSystems} is the only real consumer, and it treats the value as a
 *       world-space movement delta, directly interchangeable with the player's raw position delta
 *       (see {@code relativeMovement.set(client).sub(clientLast)} as its own fallback when {@code
 *       wishMovement} is absent). So even if it reaches us, it is exactly as camera-dependent as
 *       {@link Velocity#getClientVelocity()} already is — it would not have identified the literal
 *       key either. There is a genuinely camera-independent signal in the protocol
 *       ({@code MovementDirection}, e.g. {@code Forward}/{@code BackLeft}/etc., sent by the client as
 *       part of {@code InteractionSyncData}), but it is only synced while an interaction (attack, use
 *       item, place block, ...) is actively running — not on every tick of plain movement — so it is
 *       not a usable substitute here without an invasive always-on dummy interaction, which was not
 *       pursued.
 *   <li><b>Sticky anchor (current):</b> the blind spot in point 3 above only actually bites when the
 *       anchor is thrown away the instant the player's speed dips below {@link #DEADZONE} — which
 *       happens on every brief pause, even a fraction of a second between releasing one key and
 *       pressing the next, long before the player could plausibly have turned the camera. {@link
 *       Anchor#stoppedSeconds} now tracks how long the player has been stopped, and the anchor is only
 *       actually cleared (falling back to the "assume W" default on the next movement) after {@link
 *       #ANCHOR_GRACE_SECONDS} of continuous rest. A quick key swap, or releasing and re-pressing the
 *       same key (e.g. "W, let go, W again"), reuses the still-valid old reference instead of
 *       re-guessing — resolving the vast majority of real-world first-key mislabels without requiring
 *       the player to change how they play at all. It still cannot help if the player genuinely stops
 *       for longer than the grace window and turns the camera during that pause — nothing server-side
 *       can detect that in this build (see point 2).
 * </ol>
 */
public class MovementKeyDetectionSystem extends EntityTickingSystem<EntityStore> {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    /**
     * Minimum horizontal speed (same units as {@code getClientVelocity()}) to count as the player
     * pushing any direction at all, and the threshold used to detect "stopped" (below this) vs.
     * "moving" (at/above this) for resetting the self-calibrated {@link Anchor}.
     */
    private static final double DEADZONE = 0.05;

    /**
     * How far off the anchor's forward axis (as a fraction of total horizontal speed, i.e. {@code
     * sin} of the angle) still counts as "just that axis" rather than a diagonal. 0.26 is roughly a
     * 15° cone around each axis — generous enough for normal noise, well under 45° so a real
     * diagonal (two keys actually held) still registers as both.
     */
    private static final double AXIS_RATIO_THRESHOLD = 0.26;

    /**
     * How many consecutive ticks a direction must read the same way before it is trusted and
     * reported. Filters one-tick glitches without adding noticeable delay.
     */
    private static final int CONFIRM_TICKS = 2;

    /**
     * How long (in seconds of continuous rest, i.e. speed below {@link #DEADZONE}) the last {@link
     * Anchor} is kept alive before it is thrown away. Below this, resuming movement reuses the old
     * reference direction (the player almost certainly hasn't had time to turn the camera); above it,
     * the anchor resets and the next movement falls back to the usual "assume W" guess. See the class
     * javadoc's "Sticky anchor" point for why this exists.
     */
    private static final float ANCHOR_GRACE_SECONDS = 1.5f;

    /**
     * Every this many ticks, write the raw diagnostic numbers to the server log (only, never chat)
     * regardless of whether any key state changed. Left in from the yaw-source investigation — see
     * the class javadoc — and still useful if a future Hytale update starts populating a real yaw.
     */
    private static final int TICK_LOG_INTERVAL = 10;

    private static final Map<UUID, DirState> STATE = new ConcurrentHashMap<>();

    /**
     * Debounces one direction's raw per-tick reading: {@link #update(boolean)} only flips the
     * confirmed value once the same candidate has shown up {@link #CONFIRM_TICKS} times in a row.
     */
    private static final class Debounce {
        private boolean confirmed;
        private boolean candidate;
        private int streak;

        boolean update(boolean reading) {
            if (reading == candidate) {
                streak++;
            } else {
                candidate = reading;
                streak = 1;
            }
            if (streak >= CONFIRM_TICKS) {
                confirmed = candidate;
            }
            return confirmed;
        }
    }

    /**
     * The self-calibrated reference direction for one continuous movement (see the class javadoc,
     * point 3). {@code yawRad} is chosen, from the world-space velocity at the moment movement
     * started, so that projecting that exact velocity through it gives pure forward (right = 0).
     */
    private static final class Anchor {
        boolean active;
        double yawRad;
        /** Seconds the player has been continuously at rest (speed below {@link #DEADZONE}). */
        float stoppedSeconds;
    }

    /** Debounced state per direction, the anchor for the current movement, and space/shift/tick state. */
    private static final class DirState {
        final Debounce w = new Debounce();
        final Debounce a = new Debounce();
        final Debounce s = new Debounce();
        final Debounce d = new Debounce();
        final Anchor anchor = new Anchor();
        boolean space, shift;
        int tickCounter;
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player == null) return;

        Ref<EntityStore> playerRef = player.getReference();
        if (playerRef == null || !playerRef.isValid()) return;

        PlayerRef playerRefComp =
                store.getComponent(playerRef, Universe.get().getPlayerRefComponentType());
        if (playerRefComp == null) return;

        Velocity velocity = store.getComponent(playerRef, Velocity.getComponentType());
        MovementStatesComponent msc =
                store.getComponent(playerRef, MovementStatesComponent.getComponentType());
        if (velocity == null || msc == null) return;

        MovementStates states = msc.getMovementStates();
        if (states == null) return;

        Vector3d vel = velocity.getClientVelocity();
        double velX = vel.x();
        double velZ = vel.z();
        double worldSpeed = Math.hypot(velX, velZ);

        DirState prev = STATE.computeIfAbsent(playerRefComp.getUuid(), id -> new DirState());
        Anchor anchor = prev.anchor;

        double forward;
        double right;
        if (worldSpeed < DEADZONE) {
            // Stopped. Keep the anchor alive for a short grace period (see #ANCHOR_GRACE_SECONDS and
            // the class javadoc's "Sticky anchor" point) instead of throwing it away immediately —
            // only a genuinely long pause resets it, so a quick key swap or a release-and-repress of
            // the same key reuses the still-good old reference instead of re-guessing "W".
            anchor.stoppedSeconds += dt;
            if (anchor.stoppedSeconds > ANCHOR_GRACE_SECONDS) {
                anchor.active = false;
            }
            forward = 0;
            right = 0;
        } else {
            anchor.stoppedSeconds = 0f;
            if (!anchor.active) {
                // First tick of a new movement with no still-valid anchor to reuse: this velocity
                // direction becomes "forward" by definition. See the class javadoc, point 3, for why
                // (no working yaw source) and for the resulting blind spot (can't tell which key
                // started this movement).
                anchor.yawRad = Math.atan2(-velX, -velZ);
                anchor.active = true;
            }
            double yawRad = anchor.yawRad;
            forward = -velX * Math.sin(yawRad) - velZ * Math.cos(yawRad);
            right = velX * Math.cos(yawRad) - velZ * Math.sin(yawRad);
        }

        // Classify by ANGLE (ratio to total speed), not by raw magnitude — a fixed-magnitude
        // deadzone would let any noise grow into a false positive as speed rises.
        double speed = Math.hypot(forward, right);
        boolean rawW, rawS, rawA, rawD;
        if (speed < DEADZONE) {
            rawW = rawS = rawA = rawD = false;
        } else {
            double fwdRatio = forward / speed;
            double rightRatio = right / speed;
            rawW = fwdRatio > AXIS_RATIO_THRESHOLD;
            rawS = fwdRatio < -AXIS_RATIO_THRESHOLD;
            rawD = rightRatio > AXIS_RATIO_THRESHOLD;
            rawA = rightRatio < -AXIS_RATIO_THRESHOLD;
        }

        // Remember each direction's *previous confirmed* value before updating it, so report()
        // below still sees a proper false->true / true->false edge instead of comparing a value
        // against its own just-updated self.
        boolean wasW = prev.w.confirmed;
        boolean wasS = prev.s.confirmed;
        boolean wasA = prev.a.confirmed;
        boolean wasD = prev.d.confirmed;

        boolean w = prev.w.update(rawW);
        boolean s = prev.s.update(rawS);
        boolean a = prev.a.update(rawA);
        boolean d = prev.d.update(rawD);
        boolean space = states.jumping;
        boolean shift = states.crouching || states.forcedCrouching;

        String debug = String.format(Locale.US,
                " | anchor=%s (%.1f°) stoppedFor=%.2fs velX=%.3f velZ=%.3f fwd=%.3f right=%.3f speed=%.3f",
                anchor.active ? "yes" : "no", Math.toDegrees(anchor.yawRad), anchor.stoppedSeconds, velX,
                velZ, forward, right, speed);

        report(playerRefComp, "W", "front", wasW, w, debug);
        report(playerRefComp, "S", "back", wasS, s, debug);
        report(playerRefComp, "A", "left", wasA, a, debug);
        report(playerRefComp, "D", "right", wasD, d, debug);
        report(playerRefComp, "SPACE", "jump", prev.space, space, debug);
        report(playerRefComp, "SHIFT", "crouch", prev.shift, shift, debug);

        prev.space = space;
        prev.shift = shift;

        // Continuous timeline, server log only (never chat) — see the class javadoc. Left in from
        // the yaw-source investigation in case it's ever useful again.
        prev.tickCounter++;
        if (prev.tickCounter % TICK_LOG_INTERVAL == 0) {
            LOG.info("[RuneCore] " + playerRefComp.getUuid() + " tickLog" + debug);
        }
    }

    /**
     * Sends the "pressed" message on the rising edge (false -&gt; true) and the "released" one on
     * the falling edge (true -&gt; false) — the two actions requested: one announcing the key was
     * pressed, the other calculating when the player stopped pressing it. Both the chat message
     * and the server log line carry the same {@code debug} tag, so either source is enough to read
     * back exact values.
     */
    private void report(PlayerRef playerRefComp, String key, String label, boolean was, boolean is,
            String debug) {
        if (was == is) return;

        if (is) {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla pressionada: " + key + " (" + label + ")" + debug));
            LOG.info("[RuneCore] " + playerRefComp.getUuid() + " apertou " + key + debug);
        } else {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla solta: " + key + " (" + label + ")" + debug));
            LOG.info("[RuneCore] " + playerRefComp.getUuid() + " soltou " + key + debug);
        }
    }
}
