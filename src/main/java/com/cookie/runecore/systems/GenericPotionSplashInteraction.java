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

/**
 * Generic splash potion interaction that dynamically determines the effect
 * from the interaction type ID (e.g. "runecore:potion_splash_speed" -> "speed").
 */
public class GenericPotionSplashInteraction extends SimpleInstantInteraction {

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

        // Extract effect name from interaction ID or fallback
        String interactionId = this.getId() != null ? this.getId() : "";
        String effectName = "";
        if (interactionId.contains("potion_splash_")) {
            effectName = interactionId.substring(interactionId.indexOf("potion_splash_") + "potion_splash_".length());
        }

        if (effectName.isEmpty()) {
            System.err.println("[RuneCore] GenericPotionSplashInteraction could not determine effectName from ID: " + interactionId);
            return;
        }

        var store = entityRef.getStore();
        World world = store != null && store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        PlayerRef playerRef = store != null ? store.getComponent(entityRef, PlayerRef.getComponentType()) : null;

        System.out.println("[RuneCore] GenericPotionSplash hit entity: " + entityRef + " for effect: " + effectName);

        CastContext ctx = new CastContext(playerRef, entityRef, world, 1.0);
        ctx.target = entityRef;

        var effect = RuneCore.get().getEffect(effectName);
        if (effect != null) {
            effect.execute(ctx);
        } else {
            System.err.println("[RuneCore] Effect '" + effectName + "' not found in RuneCore registry!");
        }
    }
}
