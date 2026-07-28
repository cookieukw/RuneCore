package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.CompletableFuture;

/**
 * Read/write plumbing over Hytale's native {@link EntityStatMap}.
 * <p>
 * Split out of {@code PlayerStats}, which mixed this with movement/speed handling and packet
 * syncing. {@code PlayerStats} stays the public facade; this holds the mechanics.
 */
final class EntityStatAccess {

    /** Fallback ceiling for stats that do not declare their own bounds. */
    private static final float FALLBACK_MAX = 1000.0f;

    private EntityStatAccess() {}

    static void modify(Ref<EntityStore> ref, int statId, float amount) {
        WorldTasks.onWorldThread(ref, () -> {
            EntityStatMap statMap = statMap(ref);
            if (statMap == null) return;
            EntityStatValue value = statMap.get(statId);
            if (value == null) return;
            statMap.setStatValue(statId, clamp(value, value.get() + amount));
        });
    }

    static void set(Ref<EntityStore> ref, int statId, float newValue) {
        WorldTasks.onWorldThread(ref, () -> {
            EntityStatMap statMap = statMap(ref);
            if (statMap == null) return;
            EntityStatValue value = statMap.get(statId);
            statMap.setStatValue(statId, value != null
                    ? clamp(value, newValue)
                    : Math.max(0f, Math.min(FALLBACK_MAX, newValue)));
        });
    }

    /**
     * @return a future completing with the stat, or {@code -1f} when it cannot be read.
     *         The sentinel is preserved for compatibility with the existing public API.
     */
    static CompletableFuture<Float> read(Ref<EntityStore> ref, int statId) {
        CompletableFuture<Float> future = new CompletableFuture<>();

        boolean scheduled = WorldTasks.onWorldThread(ref, () -> {
            try {
                EntityStatMap statMap = statMap(ref);
                if (statMap != null) {
                    EntityStatValue value = statMap.get(statId);
                    if (value != null) {
                        future.complete(value.get());
                        return;
                    }
                }
                future.complete(-1f);
            } catch (RuntimeException e) {
                future.completeExceptionally(e);
            }
        });

        if (!scheduled) future.complete(-1f);
        return future;
    }

    /** Clamps to the stat's declared bounds rather than an arbitrary constant. */
    private static float clamp(EntityStatValue value, float raw) {
        return Math.max(value.getMin(), Math.min(value.getMax(), raw));
    }

    private static EntityStatMap statMap(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        return (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
    }
}
