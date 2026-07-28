package com.cookie.runecore.api.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The attribute values held by one entity: a base value plus named modifiers.
 * <p>
 * Generalises what {@code CombatStats} did with fixed fields. Two things changed beyond making
 * the attribute set open:
 * <ul>
 *   <li>Modifier sums are kept incrementally. {@code CombatStats.sumModifiers} scanned every
 *       modifier looking for the ones matching a stat, and a damage calculation reads about six
 *       stats — six full scans per hit. Reads are now a map lookup.</li>
 *   <li>Clamping comes from the attribute definition instead of being open-coded per getter.</li>
 * </ul>
 */
public class AttributeContainer {

    private final Map<String, Float> base = new ConcurrentHashMap<>();
    /** attributeId -> running total of every modifier touching it. */
    private final Map<String, Float> modifierTotals = new ConcurrentHashMap<>();
    /** modifierId -> what it contributes, so it can be removed exactly. */
    private final Map<String, Entry> modifiers = new ConcurrentHashMap<>();

    /** @return base + modifiers, clamped to the attribute's declared bounds. */
    public float get(RuneAttribute attribute) {
        if (attribute == null) return 0f;
        float raw = base.getOrDefault(attribute.id(), attribute.defaultValue())
                + modifierTotals.getOrDefault(attribute.id(), 0f);
        return attribute.clamp(raw);
    }

    /** @return the base value only, ignoring modifiers. */
    public float getBase(RuneAttribute attribute) {
        if (attribute == null) return 0f;
        return base.getOrDefault(attribute.id(), attribute.defaultValue());
    }

    public void setBase(RuneAttribute attribute, float value) {
        if (attribute == null) return;
        base.put(attribute.id(), value);
    }

    /**
     * Adds or replaces a named modifier. Replacing under the same id is safe — the previous
     * contribution is subtracted from the running total first.
     */
    public void addModifier(String modifierId, RuneAttribute attribute, float value) {
        if (modifierId == null || attribute == null) return;
        removeModifier(modifierId);
        modifiers.put(modifierId, new Entry(attribute.id(), value));
        modifierTotals.merge(attribute.id(), value, Float::sum);
    }

    public void removeModifier(String modifierId) {
        if (modifierId == null) return;
        Entry previous = modifiers.remove(modifierId);
        if (previous != null) {
            modifierTotals.merge(previous.attributeId, -previous.value, Float::sum);
        }
    }

    public boolean hasModifier(String modifierId) {
        return modifierId != null && modifiers.containsKey(modifierId);
    }

    public void clearModifiers() {
        modifiers.clear();
        modifierTotals.clear();
    }

    /** Drops base values and modifiers alike. */
    public void reset() {
        base.clear();
        clearModifiers();
    }

    private record Entry(String attributeId, float value) {}
}
