package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Helpers for entity stat modification (attack speed, mining speed) and HP delta.
 */
public final class StatHelper {

    private StatHelper() {}

    // ── HP ────────────────────────────────────────────────────────────────────

    public static void addHealth(Ref<EntityStore> ref, float amount) {
        modifyHealth(ref, amount);
    }

    public static void subtractHealth(Ref<EntityStore> ref, float amount) {
        modifyHealth(ref, -amount);
    }

    private static void modifyHealth(Ref<EntityStore> ref, float delta) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        world.execute(() -> {
            if (!ref.isValid()) return;
            EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap == null) return;
            EntityStatValue hp = statMap.get(DefaultEntityStatTypes.getHealth());
            if (hp == null) return;
            float newHp = delta > 0
                    ? Math.min(100f, hp.get() + delta)
                    : Math.max(0f, hp.get() + delta);
            statMap.setStatValue(DefaultEntityStatTypes.getHealth(), newHp);
        });
    }

    // ── Stat Modifiers ────────────────────────────────────────────────────────

    public static void applyStatModifier(Ref<EntityStore> ref, String statId,
            String modifierId, float value, StaticModifier.CalculationType type) {
        EntityStatMap statMap = getStatMap(ref);
        if (statMap == null) return;
        int index = EntityStatType.getAssetMap().getIndex(statId);
        if (index >= 0) {
            statMap.putModifier(index, modifierId,
                    new StaticModifier(Modifier.ModifierTarget.MAX, type, value));
        }
    }

    public static void removeStatModifier(Ref<EntityStore> ref, String statId, String modifierId) {
        EntityStatMap statMap = getStatMap(ref);
        if (statMap == null) return;
        int index = EntityStatType.getAssetMap().getIndex(statId);
        if (index >= 0) statMap.removeModifier(index, modifierId);
    }

    private static EntityStatMap getStatMap(Ref<EntityStore> ref) {
        if (ref == null) return null;
        Store<EntityStore> store = ref.getStore();
        return store == null ? null : (EntityStatMap) store.getComponent(ref,
                EntityStatsModule.get().getEntityStatMapComponentType());
    }
}
