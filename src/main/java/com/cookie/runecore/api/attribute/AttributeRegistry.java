package com.cookie.runecore.api.attribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Every attribute known to RuneCore, including those contributed by other mods.
 * <p>
 * Static and thread-safe on purpose: unlike the {@code instance = this} singletons elsewhere in
 * this codebase, there is no construction order to get wrong and {@link #get} never returns a
 * surprise {@code null} because the built-ins register themselves on class load.
 */
public final class AttributeRegistry {

    private static final Map<String, RuneAttribute> BY_ID = new ConcurrentHashMap<>();

    private AttributeRegistry() {}

    /**
     * Registers an attribute.
     *
     * @throws IllegalStateException if the id is already taken — silently replacing someone
     *                               else's attribute would corrupt their damage maths
     */
    public static RuneAttribute register(RuneAttribute attribute) {
        RuneAttribute previous = BY_ID.putIfAbsent(attribute.id(), attribute);
        if (previous != null && !previous.equals(attribute)) {
            throw new IllegalStateException("attribute already registered: " + attribute.id());
        }
        return attribute;
    }

    /** @return the attribute, or {@code null} when unknown. */
    public static RuneAttribute get(String id) {
        return id == null ? null : BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    public static boolean isRegistered(String id) {
        return get(id) != null;
    }

    /** Unmodifiable snapshot, handy for debug commands and tooltips. */
    public static Collection<RuneAttribute> all() {
        return Collections.unmodifiableCollection(BY_ID.values());
    }

    static {
        // Touching CoreAttributes registers the built-ins, so lookups work from any entry point
        // without callers needing to know about an init step.
        CoreAttributes.touch();
    }
}
