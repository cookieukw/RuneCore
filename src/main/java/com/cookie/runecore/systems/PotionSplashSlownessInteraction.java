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
 * Applied to each entity hit in the AOE when a slowness splash potion lands.
 * Registered as "runecore:potion_splash_slowness".
 */
public class PotionSplashSlownessInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<PotionSplashSlownessInteraction> CODEC =
            BuilderCodec.builder(
                    PotionSplashSlownessInteraction.class,
                    PotionSplashSlownessInteraction::new,
                    SimpleInstantInteraction.CODEC
            ).build();

    public PotionSplashSlownessInteraction() { super(); }
    public PotionSplashSlownessInteraction(String id) { super(id); }

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType,
                            @Nonnull InteractionContext context,
                            @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> entityRef = context.getTargetEntity();
        if (entityRef == null || !entityRef.isValid()) {
            entityRef = context.getEntity();
        }
        if (entityRef == null || !entityRef.isValid()) return;

        System.out.println("[RuneCore] PotionSplashSlowness hit target entity: " + entityRef);
        
        var store = entityRef.getStore();
        World world = store != null && store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        PlayerRef playerRef = store != null ? store.getComponent(entityRef, PlayerRef.getComponentType()) : null;

        System.out.println("[RuneCore] Target PlayerRef: " + (playerRef != null ? playerRef.getUuid() : "null (not a player)"));

        CastContext ctx = new CastContext(playerRef, entityRef, world, 1.0);
        ctx.target = entityRef;

        var effect = RuneCore.get().getEffect("slowness");
        if (effect != null) {
            effect.execute(ctx);
        } else {
            System.err.println("[RuneCore] Effect 'slowness' not found!");
        }
    }
}
