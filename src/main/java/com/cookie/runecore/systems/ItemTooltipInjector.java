package com.cookie.runecore.systems;

import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.metadata.ItemDisplayMetadata;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.InventoryChangeEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemTooltipInjector extends EntityEventSystem<EntityStore, InventoryChangeEvent> {

    private static final String MARKER_KEY = "runecore_tooltip";

    public ItemTooltipInjector() {
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

        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (registry == null) return;

        ItemContainer container = event.getItemContainer();
        if (container == null) return;

        for (short slot = 0; slot < container.getCapacity(); slot++) {
            ItemStack item = container.getItemStack(slot);
            if (ItemStack.isEmpty(item)) continue;

            if (item.getFromMetadataOrNull(MARKER_KEY, Codec.BOOLEAN) != null) continue;

            ItemCombatData data = registry.getItemData(item.getItemId());
            if (data == null) continue;

            Message description = buildStatsDescription(data);
            if (description == null) continue;

            ItemDisplayMetadata displayMeta = new ItemDisplayMetadata(null, description);
            ItemStack newItem = item
                    .withMetadata(ItemDisplayMetadata.KEYED_CODEC, displayMeta)
                    .withMetadata(MARKER_KEY, Codec.BOOLEAN, true);

            container.setItemStackForSlot(slot, newItem);
        }
    }

    private static final String COLOR_HEADER = "#FFD700";
    private static final String COLOR_PHYS_DMG = "#FF9966";
    private static final String COLOR_MAGIC_DMG = "#BB86FC";
    private static final String COLOR_TRUE_DMG = "#FFFFFF";
    private static final String COLOR_ARMOR = "#FFE066";
    private static final String COLOR_MR = "#66CCFF";
    private static final String COLOR_DR = "#66FFAA";
    private static final String COLOR_PEN = "#FF6E6E";

    private Message buildStatsDescription(ItemCombatData data) {
        List<Message> lines = new ArrayList<>();

        if (data.physicalDamage > 0)
            lines.add(statLine("+%.0f Physical Damage", data.physicalDamage, COLOR_PHYS_DMG));
        if (data.magicDamage > 0)
            lines.add(statLine("+%.0f Magic Damage", data.magicDamage, COLOR_MAGIC_DMG));
        if (data.trueDamage > 0)
            lines.add(statLine("+%.0f True Damage", data.trueDamage, COLOR_TRUE_DMG));
        if (data.armorPenetration > 0)
            lines.add(statLine("+%.0f Armor Penetration", data.armorPenetration, COLOR_PEN));
        if (data.magicPenetration > 0)
            lines.add(statLine("+%.0f Magic Penetration", data.magicPenetration, COLOR_PEN));
        if (data.armor > 0)
            lines.add(statLine("+%.0f Armor", data.armor, COLOR_ARMOR));
        if (data.magicResist > 0)
            lines.add(statLine("+%.0f Magic Resist", data.magicResist, COLOR_MR));
        if (data.damageReduction > 0)
            lines.add(statLine("+%.0f%% Damage Reduction", data.damageReduction * 100f, COLOR_DR));

        if (lines.isEmpty()) return null;

        List<Message> parts = new ArrayList<>();
        parts.add(Message.raw("\n--- Combat Stats ---").color(COLOR_HEADER).bold(true));
        parts.addAll(lines);
        return Message.join(parts.toArray(new Message[0]));
    }

    private Message statLine(String format, float value, String color) {
        return Message.raw("\n" + String.format(format, value)).color(color);
    }
}
