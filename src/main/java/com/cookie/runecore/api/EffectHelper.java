package com.cookie.runecore.api;

import java.util.function.Consumer;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.ServerCameraSettings;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.cookie.runecore.systems.ui.RuneCoreHud;
import com.cookie.runecore.systems.ui.RuneCoreHudManager;

/**
 * Shared infrastructure and constants for all RuneCore effect helpers.
 * <p>
 * Package-private methods are accessible to sibling helpers in this package.
 * Public API: {@link #modifyMovement} and the {@link MovementModifier} interface.
 */
public final class EffectHelper {

    // ── Constants ─────────────────────────────────────────────────────────────

    public static final float DEFAULT_SPEED         = 5.5f;
    public static final float DEFAULT_JUMP_FORCE    = 8.0f;
    public static final float DEFAULT_MASS          = 1.0f;
    public static final float DEFAULT_DRAG          = 0.05f;
    public static final float DEFAULT_AIR_DRAG_MAX  = 0.08f;

    private EffectHelper() {}

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Queues a movement settings modification on the world thread and syncs to client.
     */
    public static void modifyMovement(Ref<EntityStore> ref, MovementModifier modifier) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        world.execute(() -> {
            if (!ref.isValid()) return;
            MovementManager mm = (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
            if (mm == null) return;
            MovementSettings settings = mm.getSettings();
            if (settings == null) return;
            modifier.apply(settings);
            syncSettings(store, ref, settings);
        });
    }

    /**
     * Helper to execute logic on the world thread safely.
     */
    public static void worldExecute(Ref<EntityStore> ref, Runnable action) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world != null) {
            world.execute(() -> {
                if (ref.isValid()) action.run();
            });
        }
    }

    @FunctionalInterface
    public interface MovementModifier {
        void apply(MovementSettings settings);
    }

    // ── Package-private utilities ─────────────────────────────────────────────

    static void syncSettings(Store<EntityStore> store, Ref<EntityStore> ref, MovementSettings settings) {
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr != null && pr.getPacketHandler() != null) {
            pr.getPacketHandler().write(new UpdateMovementSettings(settings));
        }
    }

    static void updateHud(Ref<EntityStore> ref, Consumer<RuneCoreHud> action) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr != null) {
            RuneCoreHud hud = RuneCoreHudManager.get().getHud(pr.getUuid());
            if (hud != null) action.accept(hud);
        }
    }

    static void spawnParticleEffect(Ref<EntityStore> ref, String particleId) {
        spawnParticleEffect(ref, particleId, 0, 0, 0);
    }

    /** Spawns a particle effect with a relative offset from the entity's position. */
    static void spawnParticleEffect(Ref<EntityStore> ref, String particleId, double offsetX, double offsetY, double offsetZ) {
        if (ref == null || !ref.isValid()) return;
        
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        Vector3d pos = Vector3d.ZERO;
        
        try {
            // Re-validate inside the try block to avoid race conditions
            if (!ref.isValid()) return;

            // Base transform position
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) pos = transform.getPosition();

            // Fallback/Validation: If it's a player, MovementManager often has more accurate position
            Object mm = store.getComponent(ref, MovementManager.getComponentType());
            if (mm != null) {
                // We'll trust the transform for now if it exists, otherwise movement
                if (pos == Vector3d.ZERO || Math.abs(pos.y - 83.0) < 1.0) { // Specific fix for the 83-height bug seen earlier
                     // If we could access MovementManager position we would here
                }
            }
        } catch (Exception e) {
            // Silently fail if entity becomes invalid mid-check
            return;
        }

        // Safely create a completely new primitive-backed vector to avoid aliasing the player's transform
        Vector3d finalPos = new Vector3d(pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ);
        
        if (pos != Vector3d.ZERO) {
            ParticleUtil.spawnParticleEffect(particleId, finalPos, store);
        }
    }

    /** Looks up an EntityEffect asset index, with automatic {@code runecore:} namespace fallback. */
    static int getEffectIndex(String id) {
        int index = EntityEffect.getAssetMap().getIndex(id);
        if (index < 0) index = EntityEffect.getAssetMap().getIndex("runecore:" + id);
        return index;
    }

    static void sendCameraLock(PlayerRef playerRef, boolean locked) {
        if (playerRef == null || playerRef.getPacketHandler() == null) return;
        ServerCameraSettings settings = new ServerCameraSettings();
        if (locked) settings.lookMultiplier = new Vector2f(0.0f, 0.0f);
        playerRef.getPacketHandler().write(
                new SetServerCamera(ClientCameraView.FirstPerson, locked, settings));
    }
}
