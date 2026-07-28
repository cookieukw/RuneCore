package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This table used to be spelled out three times, once per command branch. These tests exist so
 * the single copy stays faithful to what the command accepted before.
 */
class CombatStatOptionTest {

    private static final float EPS = 0.001f;

    @Test
    @DisplayName("every canonical name resolves")
    void canonicalNames() {
        for (CombatStatOption option : CombatStatOption.values()) {
            assertSame(option, CombatStatOption.from(option.id), option.id);
        }
    }

    @Test
    @DisplayName("the short aliases the command has always accepted still work")
    void aliases() {
        assertSame(CombatStatOption.MAGIC_RESIST, CombatStatOption.from("mr"));
        assertSame(CombatStatOption.DAMAGE_REDUCTION, CombatStatOption.from("dr"));
        assertSame(CombatStatOption.PHYSICAL_DAMAGE, CombatStatOption.from("phys"));
        assertSame(CombatStatOption.MAGIC_DAMAGE, CombatStatOption.from("mag"));
        assertSame(CombatStatOption.TRUE_DAMAGE, CombatStatOption.from("true"));
        assertSame(CombatStatOption.ARMOR_PEN, CombatStatOption.from("apen"));
        assertSame(CombatStatOption.MAGIC_PEN, CombatStatOption.from("mpen"));
    }

    @Test
    @DisplayName("lookup ignores case")
    void caseInsensitive() {
        assertSame(CombatStatOption.ARMOR, CombatStatOption.from("ARMOR"));
        assertSame(CombatStatOption.MAGIC_RESIST, CombatStatOption.from("MR"));
    }

    @Test
    @DisplayName("unknown and null input resolve to null")
    void unknown() {
        assertNull(CombatStatOption.from("banana"));
        assertNull(CombatStatOption.from(null));
    }

    @Test
    @DisplayName("set writes the value through to the stats")
    void setApplies() {
        CombatStats stats = new CombatStats();
        CombatStatOption.ARMOR.set(stats, 42f);
        assertEquals(42f, stats.getArmor(), EPS);
    }

    @Test
    @DisplayName("damage reduction is typed as a percentage and stored as a fraction")
    void reductionScaling() {
        // The command takes 15 and the engine wants 0.15 — the conversion used to be an
        // open-coded `/ 100f` repeated in two branches.
        CombatStats stats = new CombatStats();
        CombatStatOption.DAMAGE_REDUCTION.set(stats, 15f);

        assertEquals(0.15f, stats.getDamageReduction(), EPS);
        assertEquals(0.15f, CombatStatOption.DAMAGE_REDUCTION.toStored(15f), EPS);
    }

    @Test
    @DisplayName("reset zeroes the attribute")
    void resetZeroes() {
        CombatStats stats = new CombatStats();
        CombatStatOption.MAGIC_RESIST.set(stats, 30f);
        CombatStatOption.MAGIC_RESIST.reset(stats);
        assertEquals(0f, stats.getMagicResist(), EPS);
    }

    @Test
    @DisplayName("the modifier key each option carries is one CombatStats understands")
    void modifierKeysAreValid() {
        // A typo here would produce a modifier that silently affects nothing.
        for (CombatStatOption option : CombatStatOption.values()) {
            CombatStats stats = new CombatStats();
            stats.addModifier("probe", option.modifierKey, 5f);
            assertNotNull(option.modifierKey);
            assertTrue(stats.hasModifier("probe"),
                    "modifier key not recognised: " + option.modifierKey);
        }
    }
}
