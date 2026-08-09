package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/**
 * Resolves the pieces a status effect needs from an entity reference.
 * <p>
 * These were private helpers inside {@code StatusEffectHelper}, which had grown to cover ten
 * unrelated effect families. They live here so the per-family classes can share them.
 */
final class EffectTargets {

    private EffectTargets() {}

    @FunctionalInterface
    interface PlayerRefAction {
        void accept(Store<EntityStore> store, PlayerRef playerRef);
    }

    @FunctionalInterface
    interface WorldPlayerAction {
        void accept(World world, UUID uuid);
    }

    /** Runs {@code action} when the entity is a player with a live packet handler. */
    static void withPlayerRef(Ref<EntityStore> ref, PlayerRefAction action) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef != null && playerRef.getPacketHandler() != null) {
            action.accept(store, playerRef);
        }
    }

    /** Runs {@code action} with the owning world and the player's UUID. */
    static void withWorldAndPlayer(Ref<EntityStore> ref, WorldPlayerAction action) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world == null) return;
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;
        action.accept(world, playerRef.getUuid());
    }
}
