package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;

import java.util.Locale;
import java.util.function.BiConsumer;

/**
 * The combat attributes reachable from {@code /combatstats}, with their aliases.
 * <p>
 * {@code handleSet}, {@code handleAdd} and {@code handleReset} each carried their own copy of
 * this table — the same nine stats and the same aliases spelled out three times, so adding an
 * attribute meant editing three switch statements and forgetting one was silent. The name,
 * aliases, setter and modifier key now live together in one row.
 * <p>
 * Shield is deliberately absent: it takes a current/max pair and has its own messages, so the
 * command handles it explicitly.
 */
enum CombatStatOption {

    ARMOR            ("armor",       "armor",            CombatStats::setArmor,            1f),
    MAGIC_RESIST     ("magicresist", "magicResist",      CombatStats::setMagicResist,      1f,   "mr"),
    /** Stored as a 0..1 fraction but typed by the player as a percentage. */
    DAMAGE_REDUCTION ("reduction",   "damageReduction",  CombatStats::setDamageReduction,  0.01f, "dr"),
    PHYSICAL_DAMAGE  ("physdmg",     "physicalDamage",   CombatStats::setPhysicalDamage,   1f,   "phys"),
    MAGIC_DAMAGE     ("magdmg",      "magicDamage",      CombatStats::setMagicDamage,      1f,   "mag"),
    TRUE_DAMAGE      ("truedmg",     "trueDamage",       CombatStats::setTrueDamage,       1f,   "true"),
    ARMOR_PEN        ("armorpen",    "armorPenetration", CombatStats::setArmorPenetration, 1f,   "apen"),
    MAGIC_PEN        ("magicpen",    "magicPenetration", CombatStats::setMagicPenetration, 1f,   "mpen");

    /** Canonical name, also what gets echoed back in messages. */
    final String id;
    /** Key used by {@link CombatStats#addModifier}. */
    final String modifierKey;

    private final BiConsumer<CombatStats, Float> setter;
    private final float scale;
    private final String[] aliases;

    CombatStatOption(String id, String modifierKey, BiConsumer<CombatStats, Float> setter,
                     float scale, String... aliases) {
        this.id = id;
        this.modifierKey = modifierKey;
        this.setter = setter;
        this.scale = scale;
        this.aliases = aliases;
    }

    /** @return the option matching {@code input} (canonical name or alias), or null. */
    static CombatStatOption from(String input) {
        if (input == null) return null;
        String key = input.toLowerCase(Locale.ROOT);
        for (CombatStatOption option : values()) {
            if (option.id.equals(key)) return option;
            for (String alias : option.aliases) {
                if (alias.equals(key)) return option;
            }
        }
        return null;
    }

    /** Applies the player-facing value, converting to the stored unit. */
    void set(CombatStats stats, float displayValue) {
        setter.accept(stats, displayValue * scale);
    }

    void reset(CombatStats stats) {
        setter.accept(stats, 0f);
    }

    /** Converts a player-facing value into the unit {@link CombatStats#addModifier} expects. */
    float toStored(float displayValue) {
        return displayValue * scale;
    }
}
