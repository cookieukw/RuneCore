package com.cookie.runecore.systems;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CreatureCombatRegistry {

    private static CreatureCombatRegistry instance;
    private final Map<String, CreatureCombatData> creatureStats = new ConcurrentHashMap<>();

    public CreatureCombatRegistry() {
        instance = this;
    }

    public static CreatureCombatRegistry get() {
        return instance;
    }

    /**
     * Defence applied to every creature registered from here on, until the next call.
     * <p>
     * Init-time DSL: {@link CreatureCombatDefaults} registers 196 creatures grouped by family,
     * so the tier is declared once per family instead of repeated on every line. Data that
     * already carries explicit defence (via {@link CreatureCombatData#withDefense}) is never
     * overwritten.
     */
    private CreatureCombatData groupDefense = null;

    /** Sets the default defence for the following registrations. Pass {@code null} to clear. */
    public void setGroupDefense(float armor, float magicResist, float damageReduction) {
        this.groupDefense = CreatureCombatData.physical()
                .withDefense(armor, magicResist, damageReduction);
    }

    public void clearGroupDefense() {
        this.groupDefense = null;
    }

    public void register(String entityId, CreatureCombatData data) {
        if (data != null && !data.hasExplicitDefense && groupDefense != null) {
            data = data.withDefense(groupDefense.armor, groupDefense.magicResist,
                    groupDefense.damageReduction);
        }
        creatureStats.put(entityId, data);
    }

    public CreatureCombatData getData(String entityId) {
        return creatureStats.get(entityId);
    }

    public boolean hasData(String entityId) {
        return creatureStats.containsKey(entityId);
    }

    public enum DamageProfile {
        PHYSICAL,
        MAGIC,
        HYBRID,
        TRUE
    }

    public static class CreatureCombatData {
        // ── Offence: how this creature deals damage ──────────────────────────
        public final DamageProfile profile;
        public final float magicRatio;
        public final float armorPenetration;
        public final float magicPenetration;

        // ── Defence: how this creature takes damage ──────────────────────────
        // These did not exist. The registry only described creatures as attackers, so a
        // creature could never mitigate anything and player weapon stats were meaningless
        // in PvE.
        public final float armor;
        public final float magicResist;
        public final float damageReduction;

        /** True when defence was set explicitly, so the group tier must not override it. */
        final boolean hasExplicitDefense;

        private CreatureCombatData(DamageProfile profile, float magicRatio, float armorPen, float magicPen) {
            this(profile, magicRatio, armorPen, magicPen, 0f, 0f, 0f, false);
        }

        private CreatureCombatData(DamageProfile profile, float magicRatio, float armorPen, float magicPen,
                                   float armor, float magicResist, float damageReduction,
                                   boolean hasExplicitDefense) {
            this.profile = profile;
            this.magicRatio = magicRatio;
            this.armorPenetration = armorPen;
            this.magicPenetration = magicPen;
            this.armor = armor;
            this.magicResist = magicResist;
            this.damageReduction = damageReduction;
            this.hasExplicitDefense = hasExplicitDefense;
        }

        /** Returns a copy with explicit defence, overriding whatever group tier is active. */
        public CreatureCombatData withDefense(float armor, float magicResist) {
            return withDefense(armor, magicResist, 0f);
        }

        public CreatureCombatData withDefense(float armor, float magicResist, float damageReduction) {
            return new CreatureCombatData(profile, magicRatio, armorPenetration, magicPenetration,
                    armor, magicResist, damageReduction, true);
        }

        public static CreatureCombatData physical() {
            return new CreatureCombatData(DamageProfile.PHYSICAL, 0f, 0f, 0f);
        }

        public static CreatureCombatData physical(float armorPen) {
            return new CreatureCombatData(DamageProfile.PHYSICAL, 0f, armorPen, 0f);
        }

        public static CreatureCombatData magic() {
            return new CreatureCombatData(DamageProfile.MAGIC, 1f, 0f, 0f);
        }

        public static CreatureCombatData magic(float magicPen) {
            return new CreatureCombatData(DamageProfile.MAGIC, 1f, 0f, magicPen);
        }

        public static CreatureCombatData hybrid(float magicRatio) {
            return new CreatureCombatData(DamageProfile.HYBRID, magicRatio, 0f, 0f);
        }

        public static CreatureCombatData hybrid(float magicRatio, float armorPen, float magicPen) {
            return new CreatureCombatData(DamageProfile.HYBRID, magicRatio, armorPen, magicPen);
        }

        public static CreatureCombatData trueDmg() {
            return new CreatureCombatData(DamageProfile.TRUE, 0f, 0f, 0f);
        }
    }
}
