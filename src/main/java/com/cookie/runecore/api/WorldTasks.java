package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Resolves the world that owns an entity, so callers can hop onto its thread.
 * <p>
 * The chain {@code ref → isValid → store → externalData → world} was written out by hand five
 * separate times inside {@code PlayerStats} alone, each with slightly different early-return
 * behaviour. It lives here once now.
 */
final class WorldTasks {

    private WorldTasks() {}

    /** @return the world owning {@code ref}, or {@code null} if it cannot be resolved. */
    static World worldOf(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        EntityStore external = store.getExternalData();
        if (external == null) return null;
        return external.getWorld();
    }

    /**
     * Runs {@code action} on the owning world's thread.
     *
     * @return false when the entity has no resolvable world, so the caller can decide what a
     *         failed schedule means (a read completes its future, a write simply drops).
     */
    static boolean onWorldThread(Ref<EntityStore> ref, Runnable action) {
        World world = worldOf(ref);
        if (world == null) return false;
        world.execute(action);
        return true;
    }
}
