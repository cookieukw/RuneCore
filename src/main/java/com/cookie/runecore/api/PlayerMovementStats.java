package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.CompletableFuture;

/**
 * Movement speed handling and the client sync that goes with it.
 * <p>
 * Split out of {@code PlayerStats}: reading {@code MovementSettings} and pushing an
 * {@link UpdateMovementSettings} packet has nothing to do with the native stat map, and the
 * two operations below were near-identical copies of each other.
 */
final class PlayerMovementStats {

    private PlayerMovementStats() {}

    static void addSpeed(Ref<EntityStore> ref, float delta, float min, float max) {
        applySpeed(ref, current -> current + delta, min, max);
    }

    static void setSpeed(Ref<EntityStore> ref, float value, float min, float max) {
        applySpeed(ref, current -> value, min, max);
    }

    /**
     * @return a future completing with the current base speed, or {@code -1f} when unavailable.
     */
    static CompletableFuture<Float> readSpeed(Ref<EntityStore> ref) {
        CompletableFuture<Float> future = new CompletableFuture<>();

        boolean scheduled = WorldTasks.onWorldThread(ref, () -> {
            try {
                MovementSettings settings = settings(ref);
                future.complete(settings != null ? settings.baseSpeed : -1f);
            } catch (RuntimeException e) {
                future.completeExceptionally(e);
            }
        });

        if (!scheduled) future.complete(-1f);
        return future;
    }

    /** Single implementation behind both add and set; they differed only in this one step. */
    private static void applySpeed(Ref<EntityStore> ref, SpeedChange change, float min, float max) {
        WorldTasks.onWorldThread(ref, () -> {
            MovementSettings settings = settings(ref);
            if (settings == null) return;

            settings.baseSpeed = Math.max(min, Math.min(max, change.from(settings.baseSpeed)));
            sync(ref, settings);
        });
    }

    private static MovementSettings settings(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        MovementManager manager =
                (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
        return manager != null ? manager.getSettings() : null;
    }

    private static void sync(Ref<EntityStore> ref, MovementSettings settings) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;
        PacketHandler handler = playerRef.getPacketHandler();
        if (handler != null) {
            handler.write(new UpdateMovementSettings(settings));
        }
    }

    @FunctionalInterface
    private interface SpeedChange {
        float from(float current);
    }
}
