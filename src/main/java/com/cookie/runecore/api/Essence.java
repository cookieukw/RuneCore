package com.cookie.runecore.api;

import java.util.HashSet;
import java.util.Set;

public record Essence(String id, RuneElement element, int tier, double stability, Set<String> tags) {
    public Essence(String id, RuneElement element, int tier) {
        this(id, element, tier, 1.0, new HashSet<>());
    }
}
