package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.InventoryChangeEvent;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class EquipmentStatsListener extends EntityEventSystem<EntityStore, InventoryChangeEvent> {

    public EquipmentStatsListener() {
        super(InventoryChangeEvent.class);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void handle(int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
                       @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer,
                       @Nonnull InventoryChangeEvent event) {

        if (event.getComponentType() != InventoryComponent.Armor.getComponentType()) return;

        CombatStatsManager manager = CombatStatsManager.get();
        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (manager == null || registry == null) return;

        PlayerRef pr = chunk.getComponent(index, PlayerRef.getComponentType());
        if (pr == null) return;
        UUID uuid = pr.getUuid();
        if (uuid == null) return;

        CombatStats stats = manager.getOrCreate(uuid);

        stats.removeModifier("equip_armor");
        stats.removeModifier("equip_magicResist");
        stats.removeModifier("equip_damageReduction");
        stats.removeModifier("equip_physicalDamage");
        stats.removeModifier("equip_magicDamage");
        stats.removeModifier("equip_trueDamage");
        stats.removeModifier("equip_armorPenetration");
        stats.removeModifier("equip_magicPenetration");

        ItemContainer armorContainer = event.getItemContainer();
        float totalArmor = 0, totalMR = 0, totalDR = 0;
        float totalPhys = 0, totalMag = 0, totalTrue = 0;
        float totalArmorPen = 0, totalMagPen = 0;

        for (short slot = 0; slot < armorContainer.getCapacity(); slot++) {
            ItemStack item = armorContainer.getItemStack(slot);
            if (item == null || item.isEmpty()) continue;

            ItemCombatData data = registry.getItemData(item.getItemId());
            if (data == null) continue;

            totalArmor += data.armor;
            totalMR += data.magicResist;
            totalDR += data.damageReduction;
            totalPhys += data.physicalDamage;
            totalMag += data.magicDamage;
            totalTrue += data.trueDamage;
            totalArmorPen += data.armorPenetration;
            totalMagPen += data.magicPenetration;
        }

        if (totalArmor != 0) stats.addModifier("equip_armor", "armor", totalArmor);
        if (totalMR != 0) stats.addModifier("equip_magicResist", "magicResist", totalMR);
        if (totalDR != 0) stats.addModifier("equip_damageReduction", "damageReduction", totalDR);
        if (totalPhys != 0) stats.addModifier("equip_physicalDamage", "physicalDamage", totalPhys);
        if (totalMag != 0) stats.addModifier("equip_magicDamage", "magicDamage", totalMag);
        if (totalTrue != 0) stats.addModifier("equip_trueDamage", "trueDamage", totalTrue);
        if (totalArmorPen != 0) stats.addModifier("equip_armorPenetration", "armorPenetration", totalArmorPen);
        if (totalMagPen != 0) stats.addModifier("equip_magicPenetration", "magicPenetration", totalMagPen);
    }
}
