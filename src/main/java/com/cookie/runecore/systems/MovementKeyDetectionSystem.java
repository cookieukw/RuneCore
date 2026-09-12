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
 * failure, and confirmed working on the first in-game test of this class too.
 *
 * <p><b>12/09, two rounds of calibration:</b> the first in-game test showed every key also
 * firing the next one in an A-&gt;W-&gt;D-&gt;S-&gt;A cycle — the signature of a +45° bias. A
 * blanket "subtract 45° from yaw" fix made that specific test pass, but a follow-up test still
 * leaked (D also fired W) with a different pattern than before, which meant the bias wasn't a
 * fixed constant — the earlier fix only happened to cancel it at the one facing angle it was
 * measured at. Root cause: the yaw was being read from {@code TransformComponent}, which is the
 * character's <em>body</em> rotation — it only loosely follows where the camera actually looks
 * (especially while strafing), so its offset from the real look direction isn't constant.
 * {@link HeadRotation} is the component the {@code SetHead} input actually writes to every time
 * the mouse moves, so it tracks the camera precisely; switching to it removes the need for any
 * calibration constant at all. (The forward/right projection formulas were never wrong — checked
 * them against the engine's own {@code Vector3dUtil.setYawPitch}, same convention.)
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

        report(playerRefComp, "W", "frente", prev.w, w);
        report(playerRefComp, "S", "trás", prev.s, s);
        report(playerRefComp, "A", "esquerda", prev.a, a);
        report(playerRefComp, "D", "direita", prev.d, d);
        report(playerRefComp, "SPACE", "pulo", prev.space, space);
        report(playerRefComp, "SHIFT", "agachar", prev.shift, shift);

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
     * pressed, the other calculating when the player stopped pressing it.
     */
    private void report(PlayerRef playerRefComp, String key, String label, boolean was, boolean is) {
        if (was == is) return;

        if (is) {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla pressionada: " + key + " (" + label + ")"));
            LOG.fine("[RuneCore] " + playerRefComp.getUuid() + " apertou " + key);
        } else {
            playerRefComp.sendMessage(Message.raw("[RuneCore] Tecla solta: " + key + " (" + label + ")"));
            LOG.fine("[RuneCore] " + playerRefComp.getUuid() + " soltou " + key);
        }
    }
}
