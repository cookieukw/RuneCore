package com.cookie.runecore.api;

import com.cookie.runecore.api.attribute.AttributeRegistry;
import com.cookie.runecore.api.attribute.CoreAttributes;
import com.cookie.runecore.api.attribute.RuneAttribute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatStatsTest {

    private static final float EPS = 0.001f;

    @Nested
    @DisplayName("mitigation formula")
    class Mitigation {

        @Test
        @DisplayName("zero defence lets the full amount through")
        void noDefense() {
            assertEquals(100f, CombatStats.calcReducedDamage(100f, 0f, 0f), EPS);
        }

        @Test
        @DisplayName("100 armour halves the damage")
        void hundredArmorHalves() {
            assertEquals(50f, CombatStats.calcReducedDamage(100f, 100f, 0f), EPS);
        }

        @Test
        @DisplayName("penetration is subtracted from the defence")
        void penetration() {
            assertEquals(
                    CombatStats.calcReducedDamage(100f, 50f, 0f),
                    CombatStats.calcReducedDamage(100f, 100f, 50f),
                    EPS);
        }

        @Test
        @DisplayName("penetration beyond the defence does not turn into a bonus")
        void penetrationCannotOverShoot() {
            // Effective defence floors at 0, so this must not exceed the raw amount.
            assertEquals(100f, CombatStats.calcReducedDamage(100f, 10f, 999f), EPS);
        }
    }

    @Nested
    @DisplayName("shield")
    class Shield {

        @Test
        @DisplayName("absorbs fully while it lasts, then lets the remainder through")
        void absorb() {
            CombatStats stats = new CombatStats();
            stats.setShieldHP(30f, 30f);

            assertEquals(0f, stats.absorbDamage(10f), EPS);
            assertEquals(20f, stats.getShieldHP(), EPS);

            assertEquals(5f, stats.absorbDamage(25f), EPS, "20 absorbed, 5 through");
            assertEquals(0f, stats.getShieldHP(), EPS);

            assertEquals(15f, stats.absorbDamage(15f), EPS, "no shield left, full pass-through");
        }

        @Test
        @DisplayName("current is clamped to max")
        void clampedToMax() {
            CombatStats stats = new CombatStats();
            stats.setShieldHP(999f, 50f);
            assertEquals(50f, stats.getShieldHP(), EPS);
        }
    }

    @Nested
    @DisplayName("modifiers")
    class Modifiers {

        @Test
        @DisplayName("legacy camelCase keys still resolve")
        void legacyKeys() {
            // The old string-keyed API must keep working for existing consumers.
            CombatStats stats = new CombatStats();
            stats.addModifier("equip", "magicResist", 25f);
            assertEquals(25f, stats.getMagicResist(), EPS);

            stats.addModifier("equip2", "armorPenetration", 10f);
            assertEquals(10f, stats.getArmorPenetration(), EPS);
        }

        @Test
        @DisplayName("an unknown key is ignored instead of throwing")
        void unknownKeyIgnored() {
            CombatStats stats = new CombatStats();
            stats.addModifier("typo", "magicRessist", 25f);
            assertEquals(0f, stats.getMagicResist(), EPS);
            assertFalse(stats.hasModifier("typo"));
        }

        @Test
        @DisplayName("damage reduction never reaches full immunity")
        void reductionCapped() {
            CombatStats stats = new CombatStats();
            stats.setDamageReduction(5f);
            assertEquals(0.9f, stats.getDamageReduction(), EPS);
        }

        @Test
        @DisplayName("attributes registered by other mods live in the same container")
        void customAttribute() {
            RuneAttribute crit = AttributeRegistry.register(
                    RuneAttribute.fraction("statstest:crit", 1f));

            CombatStats stats = new CombatStats();
            stats.attributes().setBase(crit, 0.25f);

            assertEquals(0.25f, stats.attributes().get(crit), EPS);
            assertEquals(0f, stats.getArmor(), EPS, "must not bleed into a built-in");
        }

        @Test
        @DisplayName("reset clears attributes and shield alike")
        void reset() {
            CombatStats stats = new CombatStats();
            stats.setArmor(50f);
            stats.addModifier("m", "armor", 10f);
            stats.setShieldHP(20f, 20f);

            stats.reset();

            assertEquals(0f, stats.getArmor(), EPS);
            assertEquals(0f, stats.getShieldHP(), EPS);
            assertTrue(AttributeRegistry.isRegistered(CoreAttributes.ARMOR.id()),
                    "reset must not unregister definitions");
        }
    }

    @Nested
    @DisplayName("final damage")
    class FinalDamage {

        @Test
        @DisplayName("REGRESSION: a weapon still lands damage on an attacker with no stats")
        void weaponOffenseReachesTheDefender() {
            // The shipped bug: PvP damage came only from the attacker's CombatStats, which are
            // created empty on join and only ever filled from the ARMOUR container. A player
            // swinging a sword therefore dealt exactly 0.
            CombatStats attacker = new CombatStats();
            CombatStats defender = new CombatStats();

            float withoutWeapon = defender.calculateFinalDamage(attacker);
            assertEquals(0f, withoutWeapon, EPS, "no stats and no weapon really is zero");

            CombatStats.Offense ironSword = new CombatStats.Offense(22f, 0f, 0f, 0f, 0f);
            float withWeapon = defender.calculateFinalDamage(attacker, ironSword);

            assertEquals(22f, withWeapon, EPS, "an unarmoured defender takes the full swing");
            assertTrue(withWeapon > 0f, "the whole point of the fix");
        }

        @Test
        @DisplayName("defender armour reduces weapon damage")
        void armorReducesWeapon() {
            CombatStats attacker = new CombatStats();
            CombatStats defender = new CombatStats();
            defender.setArmor(100f);

            float dmg = defender.calculateFinalDamage(attacker, new CombatStats.Offense(22f, 0f, 0f, 0f, 0f));
            assertEquals(11f, dmg, EPS, "100 armour halves it");
        }

        @Test
        @DisplayName("weapon penetration adds to the attacker's own")
        void penetrationStacks() {
            CombatStats attacker = new CombatStats();
            attacker.setArmorPenetration(50f);
            CombatStats defender = new CombatStats();
            defender.setArmor(100f);

            float dmg = defender.calculateFinalDamage(attacker, new CombatStats.Offense(22f, 0f, 0f, 50f, 0f));
            assertEquals(22f, dmg, EPS, "50 + 50 penetration cancels 100 armour");
        }

        @Test
        @DisplayName("true damage bypasses armour and reduction but not the shield")
        void trueDamage() {
            CombatStats attacker = new CombatStats();
            CombatStats defender = new CombatStats();
            defender.setArmor(500f);
            defender.setDamageReduction(0.5f);

            float dmg = defender.calculateFinalDamage(attacker, new CombatStats.Offense(0f, 0f, 40f, 0f, 0f));
            assertEquals(40f, dmg, EPS, "armour and reduction must not touch true damage");

            defender.setShieldHP(15f, 15f);
            float shielded = defender.calculateFinalDamage(attacker, new CombatStats.Offense(0f, 0f, 40f, 0f, 0f));
            assertEquals(25f, shielded, EPS, "the shield still absorbs it");
        }

        @Test
        @DisplayName("previewFinalDamage is a pure function and does not drain the shield")
        void previewIsPure() {
            CombatStats attacker = new CombatStats();
            CombatStats defender = new CombatStats();
            defender.setShieldHP(100f, 100f);

            CombatStats.Offense hit = new CombatStats.Offense(30f, 0f, 0f, 0f, 0f);
            assertEquals(0f, defender.previewFinalDamage(attacker, hit), EPS);
            assertEquals(100f, defender.getShieldHP(), EPS, "shield remains intact after preview");
            assertEquals(0f, defender.previewFinalDamage(attacker, hit), EPS, "can be called repeatedly");
        }

        @Test
        @DisplayName("calculateFinalDamage drains the shield, so it is not idempotent")
        void notIdempotent() {
            // Documented side effect. Pinned so nobody 'optimises' by calling it twice.
            CombatStats attacker = new CombatStats();
            CombatStats defender = new CombatStats();
            defender.setShieldHP(100f, 100f);

            CombatStats.Offense hit = new CombatStats.Offense(30f, 0f, 0f, 0f, 0f);
            assertEquals(0f, defender.calculateFinalDamage(attacker, hit), EPS);
            assertEquals(70f, defender.getShieldHP(), EPS);
        }
    }
}
