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

    public void register(String entityId, CreatureCombatData data) {
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
        public final DamageProfile profile;
        public final float magicRatio;
        public final float armorPenetration;
        public final float magicPenetration;

        private CreatureCombatData(DamageProfile profile, float magicRatio, float armorPen, float magicPen) {
            this.profile = profile;
            this.magicRatio = magicRatio;
            this.armorPenetration = armorPen;
            this.magicPenetration = magicPen;
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
