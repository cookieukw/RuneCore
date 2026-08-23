package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Bleeding and burn: HUD state only. The visuals come from the native entity effect assets.
 */
final class DamageOverTimeEffects {

    private DamageOverTimeEffects() {}

    static void applyBleeding(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBleeding(true));
    }

    static void revertBleeding(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBleeding(false));
    }

    /**
     * No-op.
     * <p>
     * This used to run a two-iteration loop computing a random height and spread for blood
     * particles — with the only line that consumed them commented out. It burned two
     * {@code Math.random()} calls per tick per bleeding entity and produced nothing. Kept as an
     * entry point so re-enabling particles later does not change the effect definition.
     */
    static void onBleedingTick(Ref<EntityStore> ref) {
        // Intentionally empty: see above.
    }

    static void applyBurn(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBurning(true));
    }

    static void revertBurn(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBurning(false));
    }

    /** No-op: particles and visuals come from the native Burn.json entity effect. */
    static void onBurnTick(Ref<EntityStore> ref) {
    }
}
