package com.cookie.runecore.api.attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The registry is global static state shared by every mod, so the important guarantees are
 * that the built-ins are always there and that nobody can quietly take over someone else's id.
 * Ids here are namespaced under {@code regtest:} to avoid colliding with other tests.
 */
class AttributeRegistryTest {

    @Test
    @DisplayName("built-in attributes resolve without any explicit init step")
    void builtInsSelfRegister() {
        // Guards the circular class-init between AttributeRegistry and CoreAttributes: if that
        // ever breaks, lookups silently return null instead of failing loudly.
        assertNotNull(AttributeRegistry.get("runecore:armor"));
        assertNotNull(AttributeRegistry.get("runecore:magic_resist"));
        assertNotNull(AttributeRegistry.get("runecore:physical_damage"));
        assertTrue(AttributeRegistry.all().size() >= 8);
    }

    @Test
    @DisplayName("lookup is case-insensitive")
    void caseInsensitiveLookup() {
        assertSame(CoreAttributes.ARMOR, AttributeRegistry.get("RUNECORE:ARMOR"));
    }

    @Test
    @DisplayName("unknown and null ids resolve to null")
    void unknownId() {
        assertNull(AttributeRegistry.get("regtest:does_not_exist"));
        assertNull(AttributeRegistry.get(null));
    }

    @Test
    @DisplayName("a mod can register its own attribute")
    void registerCustom() {
        RuneAttribute lifesteal = AttributeRegistry.register(
                RuneAttribute.fraction("regtest:lifesteal", 1f));

        assertSame(lifesteal, AttributeRegistry.get("regtest:lifesteal"));
        assertTrue(AttributeRegistry.isRegistered("regtest:lifesteal"));
    }

    @Test
    @DisplayName("registering the identical definition twice is tolerated")
    void idempotentRegistration() {
        RuneAttribute first = RuneAttribute.positive("regtest:idempotent");
        AttributeRegistry.register(first);
        assertDoesNotThrow(() -> AttributeRegistry.register(RuneAttribute.positive("regtest:idempotent")));
    }

    @Test
    @DisplayName("hijacking an existing id with different values is rejected")
    void duplicateIdRejected() {
        AttributeRegistry.register(RuneAttribute.positive("regtest:taken"));

        // Silently replacing would corrupt the damage maths of whoever registered first.
        assertThrows(IllegalStateException.class,
                () -> AttributeRegistry.register(new RuneAttribute("regtest:taken", 5f, 0f, 10f)));
    }

    @Test
    @DisplayName("damage reduction is capped so nothing reaches immunity")
    void damageReductionCap() {
        assertEquals(0.9f, CoreAttributes.DAMAGE_REDUCTION.max());
    }
}
