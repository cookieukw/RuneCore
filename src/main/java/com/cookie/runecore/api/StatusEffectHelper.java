package com.cookie.runecore.api;

import com.cookie.runecore.systems.InvisibilityManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Entry point for gameplay status effects.
 * <p>
 * This is a facade. It used to hold all ten effect families inline — HUD flags, camera packets,
 * stat modifiers and visibility, 251 lines with nothing in common between them. The behaviour
 * now lives in focused classes ({@code DamageOverTimeEffects}, {@code NauseaEffect},
 * {@code StatModifierEffects}, {@code EnvironmentEffects}, {@code InvisibilityManager}) while
 * this public surface stays exactly as it was.
 */
public final class StatusEffectHelper {

    private StatusEffectHelper() {}

    // ── Bleeding ──────────────────────────────────────────────────────────────

    public static void applyBleeding(Ref<EntityStore> ref) {
        DamageOverTimeEffects.applyBleeding(ref);
    }

    public static void revertBleeding(Ref<EntityStore> ref) {
        DamageOverTimeEffects.revertBleeding(ref);
    }

    public static void onBleedingTick(Ref<EntityStore> ref) {
        DamageOverTimeEffects.onBleedingTick(ref);
    }

    // ── Burn ──────────────────────────────────────────────────────────────────

    public static void applyBurn(Ref<EntityStore> ref) {
        DamageOverTimeEffects.applyBurn(ref);
    }

    public static void revertBurn(Ref<EntityStore> ref) {
        DamageOverTimeEffects.revertBurn(ref);
    }

    public static void onBurnTick(Ref<EntityStore> ref) {
        DamageOverTimeEffects.onBurnTick(ref);
    }

    // ── Nausea ────────────────────────────────────────────────────────────────

    public static void applyNausea(Ref<EntityStore> ref) {
        NauseaEffect.apply(ref);
    }

    public static void revertNausea(Ref<EntityStore> ref) {
        NauseaEffect.revert(ref);
    }

    public static void onNauseaTick(Ref<EntityStore> ref, float time) {
        NauseaEffect.onTick(ref, time);
    }

    // ── Stat modifiers ────────────────────────────────────────────────────────

    public static void applyHaste(Ref<EntityStore> ref) {
        StatModifierEffects.applyHaste(ref);
    }

    public static void revertHaste(Ref<EntityStore> ref) {
        StatModifierEffects.revertHaste(ref);
    }

    public static void applyMiningFatigue(Ref<EntityStore> ref) {
        StatModifierEffects.applyMiningFatigue(ref);
    }

    public static void revertMiningFatigue(Ref<EntityStore> ref) {
        StatModifierEffects.revertMiningFatigue(ref);
    }

    public static void applyStrength(Ref<EntityStore> ref) {
        StatModifierEffects.applyStrength(ref);
    }

    public static void revertStrength(Ref<EntityStore> ref) {
        StatModifierEffects.revertStrength(ref);
    }

    public static void applyWeakness(Ref<EntityStore> ref) {
        StatModifierEffects.applyWeakness(ref);
    }

    public static void revertWeakness(Ref<EntityStore> ref) {
        StatModifierEffects.revertWeakness(ref);
    }

    public static void applyResistance(Ref<EntityStore> ref) {
        StatModifierEffects.applyResistance(ref);
    }

    public static void revertResistance(Ref<EntityStore> ref) {
        StatModifierEffects.revertResistance(ref);
    }

    // ── Environment ───────────────────────────────────────────────────────────

    public static void onWaterBreathingTick(Ref<EntityStore> ref) {
        EnvironmentEffects.onWaterBreathingTick(ref);
    }

    // ── Invisibility ──────────────────────────────────────────────────────────

    /**
     * Hides the player from every <b>other</b> player.
     * <p>
     * State is owned by {@link InvisibilityManager}, which also catches up players who join
     * mid-effect and clears the flag on disconnect.
     */
    public static void applyInvisibility(Ref<EntityStore> ref) {
        EffectTargets.withWorldAndPlayer(ref, (world, uuid) -> {
            InvisibilityManager manager = InvisibilityManager.get();
            if (manager == null) return;
            world.execute(() -> manager.hide(uuid));
        });
        // Clear mob aggro target if present
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            if (store != null) {
                EffectHelper.worldExecute(ref, () -> {
                    EffectControllerComponent controller =
                            store.getComponent(ref, EffectControllerComponent.getComponentType());
                    if (controller == null) {
                        controller = new EffectControllerComponent();
                        store.putComponent(ref, EffectControllerComponent.getComponentType(), controller);
                    }
                    EntityEffect nativeEffect = EntityEffect.getAssetMap().getAsset("Invisibility");
                    int index = EntityEffect.getAssetMap().getIndex("Invisibility");
                    if (nativeEffect != null && index >= 0) {
                        controller.addEffect(ref, index, nativeEffect, 60.0f, OverlapBehavior.OVERWRITE, store);
                    }
                });
            }
        }
    }

    public static void revertInvisibility(Ref<EntityStore> ref) {
        EffectTargets.withWorldAndPlayer(ref, (world, uuid) -> {
            InvisibilityManager manager = InvisibilityManager.get();
            if (manager == null) return;
            world.execute(() -> manager.show(uuid));
        });
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            if (store != null) {
                EffectHelper.worldExecute(ref, () -> {
                    EffectControllerComponent controller =
                            store.getComponent(ref, EffectControllerComponent.getComponentType());
                    if (controller != null) {
                        int index = EntityEffect.getAssetMap().getIndex("Invisibility");
                        if (index >= 0) {
                            controller.removeEffect(ref, index, store);
                        }
                    }
                });
            }
        }
    }
}
