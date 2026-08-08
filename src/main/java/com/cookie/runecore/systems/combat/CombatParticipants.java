package com.cookie.runecore.systems.combat;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsManager;
import com.cookie.runecore.systems.CombatStatsRegistry;
import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.cookie.runecore.systems.CreatureCombatRegistry;
import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/**
 * Resolves who is involved in a hit: the attacking player, their weapon, and the creature data
 * behind either side.
 * <p>
 * These lookups made up roughly half of {@code CombatDamageInterceptor}, which left the actual
 * damage flow hard to follow. Every method is null-tolerant — a damage event must never blow up
 * because a component was missing.
 */
public final class CombatParticipants {

    private CombatParticipants() {}

    /** @return the player UUID behind a damage source, or null when it is not a player. */
    public static UUID playerUuid(Damage.EntitySource source) {
        if (source == null) return null;
        Ref<EntityStore> ref = source.getRef();
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        return playerRef != null ? playerRef.getUuid() : null;
    }

    /** @return the attacker's tracked stats, or null when the source is not a tracked player. */
    public static CombatStats attackerStats(Damage.EntitySource source, CombatStatsManager manager) {
        if (manager == null) return null;
        UUID uuid = playerUuid(source);
        return (uuid != null && manager.hasStats(uuid)) ? manager.getStats(uuid) : null;
    }

    /**
     * Offensive stats of the weapon the attacker is holding right now.
     * <p>
     * Resolved at hit time rather than from tracked state, because {@code EquipmentStatsListener}
     * only watches the armour container — the held weapon never reaches a player's CombatStats.
     */
    public static CombatStats.Offense weaponOffense(Damage.EntitySource source) {
        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (registry == null || source == null) return CombatStats.Offense.NONE;

        Ref<EntityStore> ref = source.getRef();
        if (ref == null || !ref.isValid()) return CombatStats.Offense.NONE;

        ItemStack held = InventoryComponent.getItemInHand(ref.getStore(), ref);
        if (held == null || held.isEmpty()) return CombatStats.Offense.NONE;

        ItemCombatData data = registry.getItemData(held.getItemId());
        if (data == null) return CombatStats.Offense.NONE;

        return new CombatStats.Offense(data.physicalDamage, data.magicDamage, data.trueDamage,
                data.armorPenetration, data.magicPenetration);
    }

    /** Creature data for a damage source, or null when it is not a registered creature. */
    public static CreatureCombatData creatureBehind(Damage.Source source) {
        if (!(source instanceof Damage.EntitySource entitySource)) return null;
        Ref<EntityStore> ref = entitySource.getRef();
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        return creatureFor(store.getComponent(ref, ModelComponent.getComponentType()));
    }

    /**
     * Creature data for a model component.
     * <p>
     * The registry key is the model asset's file name with path and namespace stripped, which is
     * the form {@code CreatureCombatDefaults} registers.
     */
    public static CreatureCombatData creatureFor(ModelComponent model) {
        CreatureCombatRegistry registry = CreatureCombatRegistry.get();
        if (registry == null || model == null || model.getModel() == null) return null;

        String assetId = model.getModel().getModelAssetId();
        if (assetId == null) return null;

        String name = assetId.contains("/") ? assetId.substring(assetId.lastIndexOf('/') + 1) : assetId;
        if (name.contains(":")) name = name.substring(name.indexOf(':') + 1);
        return registry.getData(name);
    }
}
