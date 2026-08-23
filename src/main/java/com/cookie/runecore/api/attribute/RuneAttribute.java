package com.cookie.runecore.api.attribute;

import java.util.Locale;

/**
 * Definition of a combat attribute.
 * <p>
 * Attributes used to be hardcoded fields on {@code CombatStats}, so the set was closed: adding
 * one meant editing the class, three command switches and the damage formula. A definition is
 * data now, and {@link AttributeRegistry} accepts new ones from any mod.
 *
 * @param id           namespaced identifier, e.g. {@code runecore:armor} or {@code mymod:lifesteal}
 * @param defaultValue value used when nothing has been set
 * @param min          lower clamp for the resolved value
 * @param max          upper clamp for the resolved value
 */
public record RuneAttribute(String id, float defaultValue, float min, float max) {

    public RuneAttribute {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("attribute id must not be blank");
        }
        if (min > max) {
            throw new IllegalArgumentException("attribute " + id + ": min " + min + " > max " + max);
        }
        id = id.toLowerCase(Locale.ROOT);
    }

    /** Unbounded above, never negative — the shape most offensive/defensive stats want. */
    public static RuneAttribute positive(String id) {
        return new RuneAttribute(id, 0f, 0f, Float.MAX_VALUE);
    }

    /** A 0..1 fraction, such as damage reduction. */
    public static RuneAttribute fraction(String id, float max) {
        return new RuneAttribute(id, 0f, 0f, max);
    }

    public float clamp(float value) {
        return Math.max(min, Math.min(max, value));
    }

    /** @return the part before {@code :}, or {@code runecore} when unqualified. */
    public String namespace() {
        int sep = id.indexOf(':');
        return sep > 0 ? id.substring(0, sep) : "runecore";
    }
}
