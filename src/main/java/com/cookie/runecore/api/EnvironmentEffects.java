package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/** Effects that hold an environmental stat steady. */
final class EnvironmentEffects {

    private EnvironmentEffects() {}

    /** Tops oxygen back up every tick, which is what water breathing amounts to. */
    static void onWaterBreathingTick(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
        if (statMap == null) return;

        int oxygen = DefaultEntityStatTypes.getOxygen();
        EntityStatValue value = statMap.get(oxygen);
        // Refill to the stat's own ceiling rather than a hardcoded 100 — the same mistake that
        // capped healing at 100 in StatHelper before.
        statMap.setStatValue(oxygen, value != null ? value.getMax() : 100f);
    }
}
