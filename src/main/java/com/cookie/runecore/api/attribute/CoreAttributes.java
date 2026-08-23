package com.cookie.runecore.api.attribute;

import java.util.Map;

/**
 * The attributes RuneCore ships with.
 * <p>
 * These are the fields that used to live hardcoded on {@code CombatStats}. They are ordinary
 * registrations now — a mod's own attribute is not a second-class citizen.
 */
public final class CoreAttributes {

    // ── Offence ──────────────────────────────────────────────────────────────
    public static final RuneAttribute PHYSICAL_DAMAGE   = def(RuneAttribute.positive("runecore:physical_damage"));
    public static final RuneAttribute MAGIC_DAMAGE      = def(RuneAttribute.positive("runecore:magic_damage"));
    public static final RuneAttribute TRUE_DAMAGE       = def(RuneAttribute.positive("runecore:true_damage"));
    public static final RuneAttribute ARMOR_PENETRATION = def(RuneAttribute.positive("runecore:armor_penetration"));
    public static final RuneAttribute MAGIC_PENETRATION = def(RuneAttribute.positive("runecore:magic_penetration"));

    // ── Defence ──────────────────────────────────────────────────────────────
    public static final RuneAttribute ARMOR             = def(RuneAttribute.positive("runecore:armor"));
    public static final RuneAttribute MAGIC_RESIST      = def(RuneAttribute.positive("runecore:magic_resist"));
    /** Flat multiplier, capped at 0.9 so nothing can reach full immunity. */
    public static final RuneAttribute DAMAGE_REDUCTION  = def(RuneAttribute.fraction("runecore:damage_reduction", 0.9f));

    /**
     * Legacy modifier keys accepted by {@code CombatStats.addModifier}.
     * <p>
     * The old API took bare camelCase strings ({@code "magicResist"}) with no validation — a
     * typo produced a modifier that silently affected nothing. They keep working, mapped onto
     * the real attributes.
     */
    private static final Map<String, RuneAttribute> LEGACY_KEYS = Map.ofEntries(
            Map.entry("physicaldamage",   PHYSICAL_DAMAGE),
            Map.entry("magicdamage",      MAGIC_DAMAGE),
            Map.entry("truedamage",       TRUE_DAMAGE),
            Map.entry("armorpenetration", ARMOR_PENETRATION),
            Map.entry("magicpenetration", MAGIC_PENETRATION),
            Map.entry("armor",            ARMOR),
            Map.entry("magicresist",      MAGIC_RESIST),
            Map.entry("damagereduction",  DAMAGE_REDUCTION)
    );

    private CoreAttributes() {}

    /**
     * Resolves a name from the old string-keyed API, falling back to a full attribute id.
     *
     * @return the attribute, or {@code null} when the name means nothing
     */
    public static RuneAttribute resolveLegacy(String key) {
        if (key == null) return null;
        RuneAttribute legacy = LEGACY_KEYS.get(key.toLowerCase(java.util.Locale.ROOT));
        return legacy != null ? legacy : AttributeRegistry.get(key);
    }

    /**
     * Forces this class to initialise, and with it every built-in registration.
     * <p>
     * <b>This method must stay empty.</b> Class initialisation here is circular: the constants
     * above call {@code AttributeRegistry.register}, and {@code AttributeRegistry}'s own static
     * block calls back into this method. Whichever class the JVM loads first, the other is
     * mid-initialisation when it is re-entered — safe only because this body touches nothing.
     * Put a statement here and it will read half-initialised state.
     */
    static void touch() {
        // Intentionally empty — see above.
    }

    private static RuneAttribute def(RuneAttribute attribute) {
        return AttributeRegistry.register(attribute);
    }
}
