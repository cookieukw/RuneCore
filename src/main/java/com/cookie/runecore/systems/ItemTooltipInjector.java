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

    private static final String COLOR_HEADER = "#b7c192ff";
    private static final String COLOR_PHYS_DMG = "#FF6B4A";
    private static final String COLOR_MAGIC_DMG = "#4f0af1ff";
    private static final String COLOR_TRUE_DMG = "#FFFFFF";
    private static final String COLOR_ARMOR = "#decf4aff";
    private static final String COLOR_MR = "#004164ff";
    private static final String COLOR_DR = "#01b673ff";
    private static final String COLOR_PEN = "#ff0000ff";

    private Message buildStatsDescription(ItemCombatData data) {
        List<Message> lines = new ArrayList<>();

        if (data.physicalDamage > 0)
            lines.add(statLine("runecore.tooltip.phys_damage", data.physicalDamage, COLOR_PHYS_DMG));
        if (data.magicDamage > 0)
            lines.add(statLine("runecore.tooltip.magic_damage", data.magicDamage, COLOR_MAGIC_DMG));
        if (data.trueDamage > 0)
            lines.add(statLine("runecore.tooltip.true_damage", data.trueDamage, COLOR_TRUE_DMG));
        if (data.armorPenetration > 0)
            lines.add(statLine("runecore.tooltip.armor_pen", data.armorPenetration, COLOR_PEN));
        if (data.magicPenetration > 0)
            lines.add(statLine("runecore.tooltip.magic_pen", data.magicPenetration, COLOR_PEN));
        if (data.armor > 0)
            lines.add(statLine("runecore.tooltip.armor", data.armor, COLOR_ARMOR));
        if (data.magicResist > 0)
            lines.add(statLine("runecore.tooltip.magic_resist", data.magicResist, COLOR_MR));
        if (data.damageReduction > 0)
            lines.add(statLine("runecore.tooltip.damage_reduction", data.damageReduction * 100f, COLOR_DR));

        if (lines.isEmpty()) return null;

        Message result = Message.translation("runecore.tooltip.header").color(COLOR_HEADER).bold(true);
        for (Message line : lines) {
            result = result.insert(line);
        }
        return result;
    }

    private Message statLine(String key, float value, String color) {
        return Message.translation(key).param("value", String.format("%.0f", value)).color(color);
    }
}
