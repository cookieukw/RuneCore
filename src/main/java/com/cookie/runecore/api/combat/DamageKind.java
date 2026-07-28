package com.cookie.runecore.api.combat;

/**
 * How a hit was classified before the pipeline ran.
 * <p>
 * Replaces the loose string matching in {@code CombatDamageInterceptor}, which compared cause
 * ids against two hardcoded case-sensitive sets.
 */
public enum DamageKind {

    PHYSICAL,
    MAGIC,
    /** Split between physical and magic; the ratio lives on the creature data. */
    HYBRID,
    /** Ignores armour, resist and reduction alike. */
    TRUE,
    /** Fell through every classification — passes through unmitigated. */
    UNTYPED;

    public boolean bypassesDefenses() {
        return this == TRUE;
    }
}
