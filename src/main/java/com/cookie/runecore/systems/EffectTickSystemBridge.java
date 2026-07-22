package com.cookie.runecore.systems;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.tick.TickingSystem;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class EffectTickSystemBridge extends TickingSystem<EntityStore> {

    private int lastStep = -1;

    @Override
    public void tick(float deltaTime, int step, Store<EntityStore> store) {
        if (step == lastStep) {
            return; // Already ticked this game step/tick
        }
        lastStep = step;

        System.out.println("[RuneCore-Bridge] Ticking step: " + step);

        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world != null) {
            EffectTickSystem.getInstance().tick(world);
        }
    }
}
