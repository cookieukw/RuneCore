package com.cookie.runecore.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Spell {
    private final String id;
    private final Map<String, Integer> resourceCost;
    private final Map<RuneElement, Integer> essenceCost;
    private final Predicate<CastContext> conditions;
    private final List<String> effectIds;

    public Spell(String id, Predicate<CastContext> conditions) {
        this.id = id;
        this.conditions = conditions;
        this.resourceCost = new HashMap<>();
        this.essenceCost = new HashMap<>();
        this.effectIds = new ArrayList<>();
    }

    public Spell addCost(String resourceId, int amount) {
        this.resourceCost.put(resourceId, amount);
        return this;
    }

    public Spell addEssenceRequirement(RuneElement element, int amount) {
        this.essenceCost.put(element, amount);
        return this;
    }

    public Spell addEffect(String effectId) {
        this.effectIds.add(effectId);
        return this;
    }

    public String getId() { return id; }
    public Map<String, Integer> getResourceCost() { return resourceCost; }
    public List<String> getEffectIds() { return effectIds; }
    public boolean checkConditions(CastContext ctx) { return conditions.test(ctx); }
}
