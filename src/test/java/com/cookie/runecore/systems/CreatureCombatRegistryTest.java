package com.cookie.runecore.systems;

import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData.hybrid;
import static com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData.magic;
import static com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData.physical;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Covers the group-defence DSL, which is stateful by design: {@code CreatureCombatDefaults}
 * declares a tier once per family instead of repeating it on all 196 registration lines.
 * Stateful init is exactly the kind of thing that drifts silently, hence these tests.
 */
class CreatureCombatRegistryTest {

    private static final float EPS = 0.001f;

    @Test
    @DisplayName("without a group tier, defence stays at zero")
    void noGroupDefense() {
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.register("Plain", physical());

        CreatureCombatData data = r.getData("Plain");
        assertEquals(0f, data.armor, EPS);
        assertEquals(0f, data.magicResist, EPS);
    }

    @Test
    @DisplayName("the active group tier is applied to everything registered after it")
    void groupDefenseApplies() {
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.setGroupDefense(34f, 12f, 0f);
        r.register("Golem_A", physical());
        r.register("Golem_B", physical(5));

        assertEquals(34f, r.getData("Golem_A").armor, EPS);
        assertEquals(12f, r.getData("Golem_B").magicResist, EPS);
        assertEquals(5f, r.getData("Golem_B").armorPenetration, EPS, "offence must survive");
    }

    @Test
    @DisplayName("switching tiers only affects later registrations")
    void tierSwitch() {
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.setGroupDefense(2f, 0f, 0f);
        r.register("Rabbit", physical());
        r.setGroupDefense(40f, 34f, 0.15f);
        r.register("Boss", magic(10));

        assertEquals(2f, r.getData("Rabbit").armor, EPS);
        assertEquals(40f, r.getData("Boss").armor, EPS);
        assertEquals(0.15f, r.getData("Boss").damageReduction, EPS);
    }

    @Test
    @DisplayName("explicit withDefense beats the group tier")
    void explicitDefenseWins() {
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.setGroupDefense(5f, 5f, 0f);
        r.register("Special", physical().withDefense(99f, 88f));

        assertEquals(99f, r.getData("Special").armor, EPS);
        assertEquals(88f, r.getData("Special").magicResist, EPS);
    }

    @Test
    @DisplayName("clearGroupDefense stops the tier leaking into later registrations")
    void clearStopsLeak() {
        // CreatureCombatDefaults clears at the end so a third-party mod registering afterwards
        // does not silently inherit the last family's armour.
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.setGroupDefense(40f, 34f, 0.15f);
        r.register("Boss", physical());
        r.clearGroupDefense();
        r.register("ThirdPartyMob", physical());

        assertEquals(40f, r.getData("Boss").armor, EPS);
        assertEquals(0f, r.getData("ThirdPartyMob").armor, EPS);
    }

    @Test
    @DisplayName("withDefense preserves the offensive profile")
    void withDefenseKeepsOffense() {
        CreatureCombatData data = hybrid(0.5f, 7f, 9f).withDefense(20f, 30f, 0.1f);

        assertEquals(CreatureCombatRegistry.DamageProfile.HYBRID, data.profile);
        assertEquals(0.5f, data.magicRatio, EPS);
        assertEquals(7f, data.armorPenetration, EPS);
        assertEquals(9f, data.magicPenetration, EPS);
        assertEquals(20f, data.armor, EPS);
        assertEquals(30f, data.magicResist, EPS);
        assertEquals(0.1f, data.damageReduction, EPS);
    }

    @Test
    @DisplayName("unknown creatures report as absent so the interceptor leaves them alone")
    void unknownCreature() {
        CreatureCombatRegistry r = new CreatureCombatRegistry();
        r.register("Known", physical());

        assertTrue(r.hasData("Known"));
        assertFalse(r.hasData("Unknown"));
        assertNull(r.getData("Unknown"));
    }
}
