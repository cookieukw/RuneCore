package com.cookie.runecore.systems;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CombatStatsRegistry {

    private static CombatStatsRegistry instance;
    private final Map<String, ItemCombatData> itemStats = new ConcurrentHashMap<>();

    public CombatStatsRegistry() {
        instance = this;
    }

    public static CombatStatsRegistry get() {
        return instance;
    }

    public void register(String itemId, ItemCombatData data) {
        itemStats.put(itemId, data);
    }

    public ItemCombatData getItemData(String itemId) {
        return itemStats.get(itemId);
    }

    public boolean hasItemData(String itemId) {
        return itemStats.containsKey(itemId);
    }

    public static class ItemCombatData {
        public float physicalDamage;
        public float magicDamage;
        public float trueDamage;
        public float armorPenetration;
        public float magicPenetration;
        public float armor;
        public float magicResist;
        public float damageReduction;

        public ItemCombatData() {}

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final ItemCombatData data = new ItemCombatData();

            public Builder physicalDamage(float v) { data.physicalDamage = v; return this; }
            public Builder magicDamage(float v) { data.magicDamage = v; return this; }
            public Builder trueDamage(float v) { data.trueDamage = v; return this; }
            public Builder armorPenetration(float v) { data.armorPenetration = v; return this; }
            public Builder magicPenetration(float v) { data.magicPenetration = v; return this; }
            public Builder armor(float v) { data.armor = v; return this; }
            public Builder magicResist(float v) { data.magicResist = v; return this; }
            public Builder damageReduction(float v) { data.damageReduction = v; return this; }

            public ItemCombatData build() { return data; }
        }
    }
}
