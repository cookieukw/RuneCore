package com.cookie.runecore.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CombatStats {

    // Offensive
    private float physicalDamage = 0f;
    private float magicDamage = 0f;
    private float trueDamage = 0f;
    private float armorPenetration = 0f;
    private float magicPenetration = 0f;

    // Defensive
    private float armor = 0f;
    private float magicResist = 0f;
    private float damageReduction = 0f;
    private float shieldHP = 0f;
    private float maxShieldHP = 0f;

    // Modifiers: modifierId -> (stat, value)
    private final Map<String, StatModEntry> modifiers = new ConcurrentHashMap<>();

    public CombatStats() {}

    // ── Offensive getters (base + modifiers) ─────────────────────────────────

    public float getPhysicalDamage() {
        return Math.max(0, physicalDamage + sumModifiers("physicalDamage"));
    }

    public float getMagicDamage() {
        return Math.max(0, magicDamage + sumModifiers("magicDamage"));
    }

    public float getTrueDamage() {
        return Math.max(0, trueDamage + sumModifiers("trueDamage"));
    }

    public float getArmorPenetration() {
        return Math.max(0, armorPenetration + sumModifiers("armorPenetration"));
    }

    public float getMagicPenetration() {
        return Math.max(0, magicPenetration + sumModifiers("magicPenetration"));
    }

    // ── Defensive getters (base + modifiers) ─────────────────────────────────

    public float getArmor() {
        return Math.max(0, armor + sumModifiers("armor"));
    }

    public float getMagicResist() {
        return Math.max(0, magicResist + sumModifiers("magicResist"));
    }

    public float getDamageReduction() {
        return Math.max(0, Math.min(0.9f, damageReduction + sumModifiers("damageReduction")));
    }

    public float getShieldHP() { return shieldHP; }
    public float getMaxShieldHP() { return maxShieldHP; }

    // ── Base setters ─────────────────────────────────────────────────────────

    public void setPhysicalDamage(float v) { physicalDamage = v; }
    public void setMagicDamage(float v) { magicDamage = v; }
    public void setTrueDamage(float v) { trueDamage = v; }
    public void setArmorPenetration(float v) { armorPenetration = v; }
    public void setMagicPenetration(float v) { magicPenetration = v; }
    public void setArmor(float v) { armor = v; }
    public void setMagicResist(float v) { magicResist = v; }
    public void setDamageReduction(float v) { damageReduction = v; }

    public void setShieldHP(float current, float max) {
        this.maxShieldHP = Math.max(0, max);
        this.shieldHP = Math.max(0, Math.min(this.maxShieldHP, current));
    }

    public float absorbDamage(float damage) {
        if (shieldHP <= 0) return damage;
        if (damage <= shieldHP) {
            shieldHP -= damage;
            return 0;
        }
        float remaining = damage - shieldHP;
        shieldHP = 0;
        return remaining;
    }

    // ── Modifier system ──────────────────────────────────────────────────────

    public void addModifier(String id, String stat, float value) {
        modifiers.put(id, new StatModEntry(stat, value));
    }

    public void removeModifier(String id) {
        modifiers.remove(id);
    }

    public boolean hasModifier(String id) {
        return modifiers.containsKey(id);
    }

    public void clearModifiers() {
        modifiers.clear();
    }

    private float sumModifiers(String stat) {
        float sum = 0;
        for (StatModEntry entry : modifiers.values()) {
            if (entry.stat.equals(stat)) sum += entry.value;
        }
        return sum;
    }

    // ── Damage calculation ───────────────────────────────────────────────────

    public static float calcReducedDamage(float rawDamage, float defense, float penetration) {
        float effectiveDefense = Math.max(0, defense - penetration);
        return rawDamage * 100f / (100f + effectiveDefense);
    }

    public float calculateFinalDamage(CombatStats attacker) {
        return calculateFinalDamage(attacker, Offense.NONE);
    }

    /**
     * Resolves incoming damage against this entity's defences.
     * <p>
     * {@code bonus} carries offence that is not part of the attacker's persistent
     * {@link CombatStats} — in practice the weapon currently held. Without it the attacker's
     * offence came only from equipped armour, so a player swinging a sword contributed zero
     * damage.
     * <p>
     * <b>Note:</b> this method has a side effect — it drains {@link #getShieldHP()} through
     * {@link #absorbDamage}. Call it once per hit.
     */
    public float calculateFinalDamage(CombatStats attacker, Offense bonus) {
        float physRaw = attacker.getPhysicalDamage() + bonus.physical();
        float magRaw = attacker.getMagicDamage() + bonus.magic();
        float trueRaw = attacker.getTrueDamage() + bonus.trueDamage();

        float physFinal = calcReducedDamage(physRaw, getArmor(),
                attacker.getArmorPenetration() + bonus.armorPenetration());
        float magFinal = calcReducedDamage(magRaw, getMagicResist(),
                attacker.getMagicPenetration() + bonus.magicPenetration());

        float subtotal = physFinal + magFinal;
        subtotal *= (1f - getDamageReduction());

        float afterShield = absorbDamage(subtotal + trueRaw);
        return Math.max(0, afterShield);
    }

    /**
     * Offensive contribution from a transient source (the held weapon, a spell, ...).
     * Kept in the api package so nothing here depends on the item registry.
     */
    public record Offense(float physical, float magic, float trueDamage,
                          float armorPenetration, float magicPenetration) {

        public static final Offense NONE = new Offense(0f, 0f, 0f, 0f, 0f);
    }

    // ── Reset ────────────────────────────────────────────────────────────────

    public void reset() {
        physicalDamage = 0;
        magicDamage = 0;
        trueDamage = 0;
        armorPenetration = 0;
        magicPenetration = 0;
        armor = 0;
        magicResist = 0;
        damageReduction = 0;
        shieldHP = 0;
        maxShieldHP = 0;
        modifiers.clear();
    }

    private static class StatModEntry {
        final String stat;
        final float value;
        StatModEntry(String stat, float value) {
            this.stat = stat;
            this.value = value;
        }
    }
}
