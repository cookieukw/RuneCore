package com.hypixel.hytale.server.core.event.events.entity;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class EntityDamageEvent {
    public String getDamageCause() {
        return null; // Mock
    }

    public Ref<EntityStore> getTargetRef() {
        return null; // Mock
    }

    public Ref<EntityStore> getSourceRef() {
        return null; // Mock
    }
}
