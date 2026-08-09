package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Effects that work by attaching a named modifier to a native entity stat.
 * <p>
 * Each pair shares one modifier id, so applying twice replaces rather than stacks, and reverting
 * removes exactly what was added.
 */
final class StatModifierEffects {

    private static final String STAMINA = "Stamina";
    private static final String HEALTH = "Health";

    private StatModifierEffects() {}

    static void applyHaste(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setHaste(true));
        StatHelper.applyStatModifier(ref, STAMINA, "Haste", 1.5f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    static void revertHaste(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setHaste(false));
        StatHelper.removeStatModifier(ref, STAMINA, "Haste");
    }

    static void applyMiningFatigue(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setMiningFatigue(true));
        StatHelper.applyStatModifier(ref, STAMINA, "Mining_Fatigue", 0.3f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    static void revertMiningFatigue(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setMiningFatigue(false));
        StatHelper.removeStatModifier(ref, STAMINA, "Mining_Fatigue");
    }

    static void applyStrength(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, HEALTH, "Strength", 20f,
                StaticModifier.CalculationType.ADDITIVE);
    }

    static void revertStrength(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, HEALTH, "Strength");
    }

    static void applyWeakness(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, HEALTH, "Weakness", -20f,
                StaticModifier.CalculationType.ADDITIVE);
    }

    static void revertWeakness(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, HEALTH, "Weakness");
    }

    static void applyResistance(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, HEALTH, "Resistance", 1.2f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    static void revertResistance(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, HEALTH, "Resistance");
    }
}
