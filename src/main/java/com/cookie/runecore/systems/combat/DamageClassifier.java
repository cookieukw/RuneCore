package com.cookie.runecore.systems.combat;

import com.cookie.runecore.api.combat.DamageKind;
import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;

import java.util.Set;

/**
 * Decides what kind of damage a hit is.
 * <p>
 * Extracted from {@code CombatDamageInterceptor}, where the cause sets and the three separate
 * comparison helpers were interleaved with the damage maths.
 */
public final class DamageClassifier {

    private static final Set<String> MAGIC_CAUSES = Set.of(
            "Elemental", "Fire", "Ice", "Poison", "Magic"
    );

    private static final Set<String> PHYSICAL_CAUSES = Set.of(
            "Physical", "Projectile", "Bludgeoning", "Slashing"
    );

    private DamageClassifier() {}

    /**
     * @param creature the attacking creature's data, or null when the attacker is not a
     *                 registered creature; its profile takes priority over the cause id
     */
    public static DamageKind classify(String causeId, DamageCause cause, CreatureCombatData creature) {
        String id = causeId != null ? causeId : "";

        if (id.equals("True") || (cause != null && cause.doesBypassResistances())) {
            return DamageKind.TRUE;
        }
        if (creature != null) {
            return switch (creature.profile) {
                case PHYSICAL -> DamageKind.PHYSICAL;
                case MAGIC -> DamageKind.MAGIC;
                case HYBRID -> DamageKind.HYBRID;
                case TRUE -> DamageKind.TRUE;
            };
        }
        if (isMagic(id, cause)) return DamageKind.MAGIC;
        if (isPhysical(id)) return DamageKind.PHYSICAL;
        return DamageKind.UNTYPED;
    }

    private static boolean isMagic(String causeId, DamageCause cause) {
        if (MAGIC_CAUSES.contains(causeId)) return true;
        if (cause == null) return false;
        String inherits = cause.getInherits();
        return inherits != null && MAGIC_CAUSES.contains(inherits);
    }

    private static boolean isPhysical(String causeId) {
        return PHYSICAL_CAUSES.contains(causeId);
    }
}
