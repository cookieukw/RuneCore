package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

/**
 * Generic splash potion interaction that dynamically determines the effect
 * from the interaction type ID (e.g. "runecore:potion_splash_speed" -> "speed").
 */
public class GenericPotionSplashInteraction extends SimpleInstantInteraction {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    public static final BuilderCodec<GenericPotionSplashInteraction> CODEC =
            BuilderCodec.builder(
                    GenericPotionSplashInteraction.class,
                    GenericPotionSplashInteraction::new,
                    SimpleInstantInteraction.CODEC
            ).build();

    public GenericPotionSplashInteraction() { super(); }
    public GenericPotionSplashInteraction(String id) { super(id); }

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType,
                            @Nonnull InteractionContext context,
                            @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> entityRef = context.getTargetEntity();
        if (entityRef == null || !entityRef.isValid()) {
            entityRef = context.getEntity();
        }
        if (entityRef == null || !entityRef.isValid()) return;

        // Extract effect name from interaction ID or original item
        String interactionId = this.getId() != null ? this.getId() : "";
        String effectName = "";
        if (interactionId.contains("potion_splash_") && !interactionId.endsWith("potion_splash_generic")) {
            effectName = interactionId.substring(interactionId.indexOf("potion_splash_") + "potion_splash_".length());
        }

        if (effectName.isEmpty() && context.getOriginalItemType() != null) {
            String itemId = context.getOriginalItemType().getId().toString();
            String lower = itemId.toLowerCase();
            if (lower.contains("potion_throwable_")) {
                effectName = lower.substring(lower.indexOf("potion_throwable_") + "potion_throwable_".length());
            }
        }

        if (effectName.isEmpty()) {
            LOG.warning("[RuneCore] GenericPotionSplashInteraction could not determine effectName from context!");
            return;
        }

        var store = entityRef.getStore();
        World world = store != null && store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        PlayerRef playerRef = store != null ? store.getComponent(entityRef, PlayerRef.getComponentType()) : null;

        LOG.fine("[RuneCore] GenericPotionSplash hit entity: " + entityRef + " for effect: " + effectName);

        CastContext ctx = new CastContext(playerRef, entityRef, world, 1.0);
        ctx.target = entityRef;

        var effect = RuneCore.get().getEffect(effectName);
        if (effect != null) {
            final String eff = effectName;
            final Ref<EntityStore> finalRef = entityRef;
            if (world != null) {
                world.execute(() -> {
                    if (finalRef.isValid()) {
                        RuneCore.get().getEffect(eff).execute(ctx);
                    }
                });
            } else {
                effect.execute(ctx);
            }
        } else {
            LOG.warning("[RuneCore] Effect '" + effectName + "' not found in RuneCore registry!");
        }
    }
}
