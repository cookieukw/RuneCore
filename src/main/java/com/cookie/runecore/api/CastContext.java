package com.cookie.runecore.api;

import java.util.HashMap;
import java.util.Map;

public class CastContext {
    public Object source;
    public Object target;
    public Object world;
    public double power;
    public Map<String, Object> metadata = new HashMap<>();

    public CastContext(Object source, Object target, Object world, double power) {
        this.source = source;
        this.target = target;
        this.world = world;
        this.power = power;
    }
}
