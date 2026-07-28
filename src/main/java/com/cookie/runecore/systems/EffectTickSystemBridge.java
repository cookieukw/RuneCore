package com.cookie.runecore.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.ArchetypeTickingSystem;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EffectTickSystemBridge extends ArchetypeTickingSystem<EntityStore> {

    /**
     * Last processed game step, <b>per world</b>.
     * <p>
     * This used to be one static {@code AtomicLong} shared by every world. Each world keeps its
     * own tick counter, so whichever world reached a given step first won the compare-and-set
     * and every other world was rejected by the {@code step <= currentLast} guard — buffs on
     * entities outside that one world never ticked at all, and which world "won" depended on
     * tick ordering. Keying by world keeps the once-per-step dedup (this system is invoked once
     * per archetype chunk) without starving the others.
     */
    private static final ConcurrentHashMap<String, AtomicLong> lastStepByWorld = new ConcurrentHashMap<>();

    @Override
    public void tick(float deltaTime, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world == null) return;

        long step = world.getTick();
        AtomicLong lastStep = lastStepByWorld.computeIfAbsent(world.getName(), k -> new AtomicLong(-1));

        // Ensure this only ticks exactly once per game step, for this world
        long currentLast = lastStep.get();
        if (step <= currentLast) {
            return;
        }
        if (!lastStep.compareAndSet(currentLast, step)) {
            return;
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
