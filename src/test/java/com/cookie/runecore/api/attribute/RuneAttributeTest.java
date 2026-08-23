package com.cookie.runecore.api.attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuneAttributeTest {

    private static final float EPS = 0.001f;

    @Test
    @DisplayName("clamp holds the value inside the declared range")
    void clamp() {
        RuneAttribute a = new RuneAttribute("test:clamped", 0f, 5f, 10f);

        assertEquals(5f, a.clamp(-100f), EPS);
        assertEquals(10f, a.clamp(100f), EPS);
        assertEquals(7f, a.clamp(7f), EPS);
    }

    @Test
    @DisplayName("ids are normalised to lower case")
    void idNormalised() {
        assertEquals("test:mixedcase", new RuneAttribute("Test:MixedCase", 0f, 0f, 1f).id());
    }

    @Test
    @DisplayName("namespace is the part before the colon, defaulting to runecore")
    void namespace() {
        assertEquals("mymod", new RuneAttribute("mymod:crit", 0f, 0f, 1f).namespace());
        assertEquals("runecore", new RuneAttribute("bare", 0f, 0f, 1f).namespace());
    }

    @Test
    @DisplayName("positive() is unbounded above and never negative")
    void positiveFactory() {
        RuneAttribute a = RuneAttribute.positive("test:positive");
        assertEquals(0f, a.clamp(-50f), EPS);
        assertEquals(9999f, a.clamp(9999f), EPS);
    }

    @Test
    @DisplayName("fraction() caps at the given ceiling")
    void fractionFactory() {
        RuneAttribute a = RuneAttribute.fraction("test:fraction", 0.9f);
        assertEquals(0.9f, a.clamp(5f), EPS);
    }

    @Test
    @DisplayName("a malformed definition fails at construction, not at first use")
    void invalidDefinitions() {
        assertThrows(IllegalArgumentException.class, () -> new RuneAttribute("", 0f, 0f, 1f));
        assertThrows(IllegalArgumentException.class, () -> new RuneAttribute(null, 0f, 0f, 1f));
        assertThrows(IllegalArgumentException.class, () -> new RuneAttribute("test:bad", 0f, 10f, 1f));
    }
}
