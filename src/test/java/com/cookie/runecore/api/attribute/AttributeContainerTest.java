package com.cookie.runecore.api.attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The container replaced {@code CombatStats}'s hand-rolled modifier map with incrementally
 * maintained totals. Incremental sums are easy to get subtly wrong, so the arithmetic is
 * pinned down here.
 */
class AttributeContainerTest {

    private static final RuneAttribute ARMOR = new RuneAttribute("test:armor", 0f, 0f, 1000f);
    private static final RuneAttribute CAPPED = new RuneAttribute("test:capped", 0f, 0f, 0.9f);
    private static final RuneAttribute WITH_DEFAULT = new RuneAttribute("test:defaulted", 7f, 0f, 100f);

    @Test
    @DisplayName("an untouched attribute reads its declared default")
    void defaultValue() {
        assertEquals(7f, new AttributeContainer().get(WITH_DEFAULT));
        assertEquals(0f, new AttributeContainer().get(ARMOR));
    }

    @Test
    @DisplayName("base and modifiers add together")
    void baseAndModifiers() {
        AttributeContainer c = new AttributeContainer();
        c.setBase(ARMOR, 10f);
        c.addModifier("helmet", ARMOR, 5f);
        c.addModifier("boots", ARMOR, 3f);

        assertEquals(18f, c.get(ARMOR));
        assertEquals(10f, c.getBase(ARMOR), "getBase must ignore modifiers");
    }

    @Test
    @DisplayName("removing a modifier subtracts exactly its own contribution")
    void removeModifier() {
        AttributeContainer c = new AttributeContainer();
        c.addModifier("a", ARMOR, 5f);
        c.addModifier("b", ARMOR, 3f);

        c.removeModifier("a");

        assertEquals(3f, c.get(ARMOR));
        assertFalse(c.hasModifier("a"));
        assertTrue(c.hasModifier("b"));
    }

    @Test
    @DisplayName("re-registering an id replaces it instead of stacking")
    void replaceModifier() {
        AttributeContainer c = new AttributeContainer();
        c.addModifier("equip", ARMOR, 5f);
        c.addModifier("equip", ARMOR, 20f);

        assertEquals(20f, c.get(ARMOR), "the first contribution must be undone, not added to");
    }

    @Test
    @DisplayName("a modifier moved to another attribute leaves nothing behind")
    void modifierChangingAttribute() {
        AttributeContainer c = new AttributeContainer();
        c.addModifier("shift", ARMOR, 9f);
        c.addModifier("shift", WITH_DEFAULT, 1f);

        assertEquals(0f, c.get(ARMOR), "old attribute must drop back to its base");
        assertEquals(8f, c.get(WITH_DEFAULT), "7 default + 1");
    }

    @Test
    @DisplayName("removing an unknown id is a no-op, not a corruption")
    void removeUnknown() {
        AttributeContainer c = new AttributeContainer();
        c.addModifier("real", ARMOR, 4f);
        c.removeModifier("ghost");
        c.removeModifier(null);

        assertEquals(4f, c.get(ARMOR));
    }

    @Test
    @DisplayName("the resolved value is clamped to the attribute bounds")
    void clamping() {
        AttributeContainer c = new AttributeContainer();
        c.setBase(CAPPED, 5f);
        assertEquals(0.9f, c.get(CAPPED), "must not exceed the declared max");

        c.setBase(ARMOR, -50f);
        assertEquals(0f, c.get(ARMOR), "must not fall below the declared min");
    }

    @Test
    @DisplayName("negative modifiers reduce and can be undone")
    void negativeModifiers() {
        AttributeContainer c = new AttributeContainer();
        c.setBase(ARMOR, 20f);
        c.addModifier("curse", ARMOR, -8f);
        assertEquals(12f, c.get(ARMOR));

        c.removeModifier("curse");
        assertEquals(20f, c.get(ARMOR));
    }

    @Test
    @DisplayName("clearModifiers keeps base values, reset drops everything")
    void clearAndReset() {
        AttributeContainer c = new AttributeContainer();
        c.setBase(ARMOR, 10f);
        c.addModifier("m", ARMOR, 5f);

        c.clearModifiers();
        assertEquals(10f, c.get(ARMOR));

        c.reset();
        assertEquals(0f, c.get(ARMOR));
    }

    @Test
    @DisplayName("a null attribute reads as zero rather than throwing")
    void nullSafety() {
        AttributeContainer c = new AttributeContainer();
        assertEquals(0f, c.get(null));
        c.setBase(null, 5f);
        c.addModifier("x", null, 5f);
        assertFalse(c.hasModifier("x"));
    }
}
