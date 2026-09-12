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
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
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
 * movement result the client already computed each tick (the {@code ClientMovement} packet).
 * This system reconstructs player intent from two values that already arrive complete and
 * reliable every tick, with no dependency on any click:
 *
 * <ul>
 *   <li>{@link Velocity#getClientVelocity()} — horizontal velocity in world space (X/Z axes),
 *       supplied by the client itself with every {@code ClientMovement};</li>
 *   <li>{@link HeadRotation#getRotation()} — which way the player's camera is looking (yaw),
 *       used to rotate the world-space velocity into local space (forward/back/right/left
 *       relative to the camera, which is how W/A/S/D actually work).</li>
 * </ul>
 *
 * <p>Space and Shift/Ctrl don't need any of that math: {@code MovementStates.jumping} and
 * {@code MovementStates.crouching} already come ready-made from the same packet — the same
 * source {@code ChildCarryHelper.isCrouching()} reads from in SimTale without a single reported
 * failure, and confirmed working on every in-game test of this class so far.
 *
 * <p><b>12/09, still under calibration:</b> two rounds of guessing a fixed angular bias (first on
 * {@code TransformComponent}'s body yaw, then switching to {@link HeadRotation}'s camera yaw)
 * both still leaked one direction into a neighboring one, and a further test showed the leak
 * pattern itself flips when the player turns around — which rules out a fixed constant entirely.
 * Rather than guess a fourth constant, every message now carries the raw numbers behind the
 * decision ({@code debug} below: yaw, world-space velocity, and the two projected components) so
 * the actual fault can be read directly from a single test instead of inferred from symptoms.
 */
public class MovementKeyDetectionSystem extends EntityTickingSystem<EntityStore> {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    /**
     * Minimum horizontal speed (same units as {@code getClientVelocity()}) to count as the player
     * "pushing" some direction. Below this is physics noise (sliding, friction), not an actually
     * held key.
     */
    private static final double DEADZONE = 0.05;

    private static final Map<UUID, DirState> STATE = new ConcurrentHashMap<>();

    /** One boolean per direction, tracking whether it was "pressed" on the previous tick. */
    private static final class DirState {
        boolean w, a, s, d, space, shift;
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

        HeadRotation headRotation =
                store.getComponent(playerRef, HeadRotation.getComponentType());
        Velocity velocity = store.getComponent(playerRef, Velocity.getComponentType());
        MovementStatesComponent msc =
                store.getComponent(playerRef, MovementStatesComponent.getComponentType());
        if (headRotation == null || velocity == null || msc == null) return;

        MovementStates states = msc.getMovementStates();
        if (states == null) return;

        double yawDeg = headRotation.getRotation().yaw();
        double yawRad = Math.toRadians(yawDeg);

        Vector3d vel = velocity.getClientVelocity();
        double velX = vel.x();
        double velZ = vel.z();

        // Rotate the velocity (world space) into the player's local space, using the camera's
        // actual look yaw (HeadRotation) rather than the body's.
        double forward = -velX * Math.sin(yawRad) - velZ * Math.cos(yawRad);
        double right = velX * Math.cos(yawRad) - velZ * Math.sin(yawRad);

        DirState prev = STATE.computeIfAbsent(playerRefComp.getUuid(), id -> new DirState());

        boolean w = forward > DEADZONE;
        boolean s = forward < -DEADZONE;
        boolean d = right > DEADZONE;
        boolean a = right < -DEADZONE;
        boolean space = states.jumping;
        boolean shift = states.crouching || states.forcedCrouching;

        // TEMPORARY debug tag appended to every message and log line below, so a single in-game
        // test — including the "I turned around" case — tells us the exact raw numbers instead of
        // us having to guess a fix blind again. Remove once the projection is confirmed correct.
        String debug = String.format(Locale.US,
                " | looking=%.1f° velX=%.3f velZ=%.3f fwd=%.3f right=%.3f",
                yawDeg, velX, velZ, forward, right);

        report(playerRefComp, "W", "front", prev.w, w, debug);
        report(playerRefComp, "S", "back", prev.s, s, debug);
        report(playerRefComp, "A", "left", prev.a, a, debug);
        report(playerRefComp, "D", "right", prev.d, d, debug);
        report(playerRefComp, "SPACE", "jump", prev.space, space, debug);
        report(playerRefComp, "SHIFT", "crouch", prev.shift, shift, debug);

        prev.w = w;
        prev.s = s;
        prev.a = a;
        prev.d = d;
        prev.space = space;
        prev.shift = shift;
    }

    /**
     * Sends the "pressed" message on the rising edge (false -&gt; true) and the "released" one on
     * the falling edge (true -&gt; false) — the two actions requested: one announcing the key was
     * pressed, the other calculating when the player stopped pressing it. Both the chat message
     * and the server log line carry the same {@code debug} tag (looking direction plus the raw
     * velocity/projection numbers), so either source is enough to read back exact values.
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
