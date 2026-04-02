package com.cookie.runecore.api;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import java.util.HashMap;
import java.util.Map;

public class CastContext {
    public PlayerRef source;
    public Object target;
    public World world;
    public double power;
    public Map<String, Object> metadata = new HashMap<>();

    public CastContext(PlayerRef source, Object target, World world, double power) {
        this.source = source;
        this.target = target;
        this.world = world;
        this.power = power;
    }

    public PlayerRef getOwner() {
        return source;
    }
}
