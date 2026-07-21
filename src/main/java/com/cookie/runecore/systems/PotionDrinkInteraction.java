package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PotionDrinkInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<PotionDrinkInteraction> CODEC =
            BuilderCodec.builder(
                    PotionDrinkInteraction.class,
                    PotionDrinkInteraction::new,
                    SimpleInstantInteraction.CODEC
            ).build();

    public PotionDrinkInteraction() {
        super();
    }

    public PotionDrinkInteraction(String id) {
        super(id);
    }

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType,
                            @Nonnull InteractionContext context,
                            @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> playerEntityRef = context.getEntity();
        if (playerEntityRef != null && playerEntityRef.isValid()) {
            Item itemType = context.getOriginalItemType();
            if (itemType != null) {
                String itemId = itemType.getId().toString();
                System.out.println("[RuneCore-PotionDrinkInteraction] Executing potion drink for item: " + itemId);
                if (itemId.toLowerCase().contains("potion_drinkable_")) {
                    String lowerId = itemId.toLowerCase();
                    int idx = lowerId.indexOf("potion_drinkable_");
                    String effectName = lowerId.substring(idx + "potion_drinkable_".length());

                    Store<EntityStore> store = playerEntityRef.getStore();
                    if (store != null) {
                        PlayerRef playerRef = (PlayerRef) store.getComponent(playerEntityRef, PlayerRef.getComponentType());
                        if (playerRef != null) {
                            World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
                            CastContext ctx = new CastContext(playerRef, playerEntityRef, world, 1.0);
                            if (RuneCore.get().getEffect(effectName) != null) {
                                RuneCore.get().getEffect(effectName).execute(ctx);
                                System.out.println("[RuneCore-PotionDrinkInteraction] Player drank potion, applying effect: " + effectName);
                                playerRef.sendMessage(Message.raw("§6[RuneCore] §fVocê tomou a poção de: §e" + effectName));
                            } else {
                                System.err.println("[RuneCore-PotionDrinkInteraction] Effect not found: " + effectName);
                            }
                        }
                    }
                }
            }
        }
    }
}
