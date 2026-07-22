package com.cookie.runecore.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.ArchetypeTickingSystem;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class EffectTickSystemBridge extends ArchetypeTickingSystem<EntityStore> {

    private long lastStep = -1;

    @Override
    public void tick(float deltaTime, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world == null) return;
        
        long step = world.getTick();
        if (step == lastStep) {
            return;
        }
        lastStep = step;

        EffectTickSystem.getInstance().tick(world);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
                TransformComponent.getComponentType()
        );
    }
}
