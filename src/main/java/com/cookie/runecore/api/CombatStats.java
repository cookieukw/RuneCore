package com.cookie.runecore.api;

import com.cookie.runecore.api.attribute.AttributeContainer;
import com.cookie.runecore.api.attribute.CoreAttributes;
import com.cookie.runecore.api.attribute.RuneAttribute;

/**
 * Typed view over an entity's combat attributes.
 * <p>
 * The fixed fields and the hand-rolled modifier map are gone: storage is an
 * {@link AttributeContainer}, so an attribute registered by another mod lives in the same place
 * as {@code armor}. This class stays as the convenient, discoverable accessor for the built-ins
 * — its public surface is unchanged.
 * <p>
 * Shield remains a plain field. It is consumable hit points rather than a stat, and
 * {@link #absorbDamage} mutates it.
 */
public class CombatStats {

    private final AttributeContainer attributes = new AttributeContainer();

    private float shieldHP = 0f;
    private float maxShieldHP = 0f;

    public CombatStats() {}

    /** The underlying container, for attributes this class has no typed accessor for. */
    public AttributeContainer attributes() {
        return attributes;
    }

    // ── Offensive getters (base + modifiers) ─────────────────────────────────

    public float getPhysicalDamage()   { return attributes.get(CoreAttributes.PHYSICAL_DAMAGE); }
    public float getMagicDamage()      { return attributes.get(CoreAttributes.MAGIC_DAMAGE); }
    public float getTrueDamage()       { return attributes.get(CoreAttributes.TRUE_DAMAGE); }
    public float getArmorPenetration() { return attributes.get(CoreAttributes.ARMOR_PENETRATION); }
    public float getMagicPenetration() { return attributes.get(CoreAttributes.MAGIC_PENETRATION); }

    // ── Defensive getters (base + modifiers) ─────────────────────────────────

    public float getArmor()           { return attributes.get(CoreAttributes.ARMOR); }
    public float getMagicResist()     { return attributes.get(CoreAttributes.MAGIC_RESIST); }
    public float getDamageReduction() { return attributes.get(CoreAttributes.DAMAGE_REDUCTION); }

    public float getShieldHP()    { return shieldHP; }
    public float getMaxShieldHP() { return maxShieldHP; }

    // ── Base setters ─────────────────────────────────────────────────────────

    public void setPhysicalDamage(float v)   { attributes.setBase(CoreAttributes.PHYSICAL_DAMAGE, v); }
    public void setMagicDamage(float v)      { attributes.setBase(CoreAttributes.MAGIC_DAMAGE, v); }
    public void setTrueDamage(float v)       { attributes.setBase(CoreAttributes.TRUE_DAMAGE, v); }
    public void setArmorPenetration(float v) { attributes.setBase(CoreAttributes.ARMOR_PENETRATION, v); }
    public void setMagicPenetration(float v) { attributes.setBase(CoreAttributes.MAGIC_PENETRATION, v); }
    public void setArmor(float v)            { attributes.setBase(CoreAttributes.ARMOR, v); }
    public void setMagicResist(float v)      { attributes.setBase(CoreAttributes.MAGIC_RESIST, v); }
    public void setDamageReduction(float v)  { attributes.setBase(CoreAttributes.DAMAGE_REDUCTION, v); }

    public void setShieldHP(float current, float max) {
        this.maxShieldHP = Math.max(0, max);
        this.shieldHP = Math.max(0, Math.min(this.maxShieldHP, current));
    }

    /** Drains the shield by {@code damage}; returns what got through. */
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

    /**
     * @param stat legacy camelCase name ({@code "magicResist"}) or a full attribute id
     *             ({@code "mymod:lifesteal"}). Unknown names are ignored — as before, except
     *             the miss is now at least well-defined.
     */
    public void addModifier(String id, String stat, float value) {
        RuneAttribute attribute = CoreAttributes.resolveLegacy(stat);
        if (attribute != null) attributes.addModifier(id, attribute, value);
    }

    public void addModifier(String id, RuneAttribute attribute, float value) {
        attributes.addModifier(id, attribute, value);
    }

    public void removeModifier(String id)   { attributes.removeModifier(id); }
    public boolean hasModifier(String id)   { return attributes.hasModifier(id); }
    public void clearModifiers()            { attributes.clearModifiers(); }

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
     * {@code bonus} carries offence that is not part of the attacker's persistent stats — in
     * practice the weapon currently held.
     * <p>
     * <b>Note:</b> this has a side effect — it drains {@link #getShieldHP()} through
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

    // ── Reset ────────────────────────────────────────────────────────────────

    public void reset() {
        attributes.reset();
        shieldHP = 0;
        maxShieldHP = 0;
    }

    /**
     * Offensive contribution from a transient source (the held weapon, a spell, ...).
     * Kept in the api package so nothing here depends on the item registry.
     */
    public record Offense(float physical, float magic, float trueDamage,
                          float armorPenetration, float magicPenetration) {

        public static final Offense NONE = new Offense(0f, 0f, 0f, 0f, 0f);
    }
}
