package com.cookie.runecore.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.ArchetypeTickingSystem;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.atomic.AtomicLong;

public class EffectTickSystemBridge extends ArchetypeTickingSystem<EntityStore> {

    private static final AtomicLong lastStep = new AtomicLong(-1);

    @Override
    public void tick(float deltaTime, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world == null) return;
        
        long step = world.getTick();
        
        // Atomic compare and set: only execute if we successfully update lastStep to the new step
        long currentLast = lastStep.get();
        if (step <= currentLast) {
            return;
        }
        if (!lastStep.compareAndSet(currentLast, step)) {
            return; // Another thread already ticked this step
        }

        EffectTickSystem.getInstance().tick(world);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
                TransformComponent.getComponentType()
        );
    }
}
