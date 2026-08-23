package com.cookie.runecore.api;

import com.cookie.runecore.api.attribute.AttributeContainer;
import com.cookie.runecore.systems.CombatStatsManager;
import com.cookie.runecore.systems.CombatStatsRegistry;
import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.cookie.runecore.systems.CreatureCombatRegistry;
import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Optional;
import java.util.UUID;

/**
 * Entry point for combat attributes.
 * <p>
 * Before this, reaching a player's armour meant
 * {@code CombatStatsManager.get().getOrCreate(uuid)} — a class in the {@code systems} package,
 * whose {@code get()} can return null depending on plugin start-up order. There was no boundary
 * between contract and implementation, and no sanctioned way for another mod to register its
 * own weapons or creatures.
 * <p>
 * Everything here is null-safe: an unavailable subsystem yields an empty {@link Optional} or a
 * no-op, never an exception during a damage event.
 */
public final class RuneAttributes {

    private RuneAttributes() {}

    // ── Entity attributes ────────────────────────────────────────────────────

    /**
     * Attributes for a connected player, created on first access.
     *
     * @return empty when the combat subsystem is not up yet
     */
    public static Optional<AttributeContainer> of(UUID playerId) {
        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null || playerId == null) return Optional.empty();
        return Optional.of(manager.getOrCreate(playerId).attributes());
    }

    /** Attributes for a player entity. */
    public static Optional<AttributeContainer> of(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return Optional.empty();
        Store<EntityStore> store = ref.getStore();
        if (store == null) return Optional.empty();
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        return playerRef != null ? of(playerRef.getUuid()) : Optional.empty();
    }

    public static Optional<AttributeContainer> of(PlayerRef playerRef) {
        return playerRef != null ? of(playerRef.getUuid()) : Optional.empty();
    }

    // ── Content registration ─────────────────────────────────────────────────

    /**
     * Declares the combat stats of an item, so it contributes when worn or held.
     *
     * @return false when the registry is not up yet, so callers can retry or log
     */
    public static boolean registerItem(String itemId, ItemCombatData data) {
        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (registry == null || itemId == null || data == null) return false;
        registry.register(itemId, data);
        return true;
    }

    /**
     * Declares how a creature deals and takes damage. The key is the model asset's file name
     * without path or namespace, matching how the damage interceptor identifies creatures.
     */
    public static boolean registerCreature(String creatureId, CreatureCombatData data) {
        CreatureCombatRegistry registry = CreatureCombatRegistry.get();
        if (registry == null || creatureId == null || data == null) return false;
        registry.register(creatureId, data);
        return true;
    }

    public static Optional<ItemCombatData> itemData(String itemId) {
        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (registry == null || itemId == null) return Optional.empty();
        return Optional.ofNullable(registry.getItemData(itemId));
    }

    public static Optional<CreatureCombatData> creatureData(String creatureId) {
        CreatureCombatRegistry registry = CreatureCombatRegistry.get();
        if (registry == null || creatureId == null) return Optional.empty();
        return Optional.ofNullable(registry.getData(creatureId));
    }
}
