package com.cookie.runecore.api;

import com.cookie.runecore.systems.EffectTickSystem;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.function.Function;

public class RuneEffect {
    private final String id;
    private final int defaultDurationTicks;
    private final boolean isInstant;

    private Function<CastContext, ActiveBuff> buffFactory = null;

    private IEffectAction action = null;

    private String nativeEffectId = null;
    private String assetPath = null;
    private int amplifier = 0;


    public RuneEffect(String id, int defaultDurationTicks) {
        this.id = id;
        this.defaultDurationTicks = defaultDurationTicks;
        this.isInstant = false;
    }

    public RuneEffect(String id, boolean isInstant, int durationTicks, IEffectAction action) {
        this.id = id;
        this.isInstant = isInstant;
        this.defaultDurationTicks = durationTicks;
        this.action = action;
    }

    public RuneEffect(String id, String nativeEffectId, int durationTicks) {
        this.id = id;
        this.isInstant = false;
        this.defaultDurationTicks = durationTicks;
        this.nativeEffectId = nativeEffectId;
    }


    public RuneEffect withBuff(Function<CastContext, ActiveBuff> factory) {
        this.buffFactory = factory;
        return this;
    }

    public RuneEffect withAction(IEffectAction action) {
        this.action = action;
        return this;
    }

    public RuneEffect withNativeEffect(String nativeEffectId) {
        this.nativeEffectId = nativeEffectId;
        return this;
    }

    public RuneEffect withAsset(String assetPath) {
        this.assetPath = assetPath;
        if (this.nativeEffectId == null) {
            this.nativeEffectId = assetPath;
        }
        return this;
    }

    public RuneEffect withAmplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }


    public void execute(CastContext ctx) {
        if (this.action != null) {
            this.action.apply(ctx);
        }

        if (this.buffFactory != null) {
            @SuppressWarnings("unchecked")
            Ref<EntityStore> entityRef = (ctx.target instanceof Ref) ? (Ref<EntityStore>) ctx.target : null;
            ActiveBuff buff = this.buffFactory.apply(ctx);
            if (buff != null) {
                boolean refValid = (entityRef != null && entityRef.isValid());
                System.out.println("[RuneCore] Registering buff: " + buff.effectId
                        + " | player=" + buff.playerId
                        + " | duration=" + buff.remainingTicks
                        + " | interval=" + buff.intervalTicks
                        + " | refValid=" + refValid);
                EffectTickSystem.getInstance().applyBuff(buff, entityRef);
            } else {
                System.err.println("[RuneCore] buffFactory returned null for effect: " + id);
            }
        }

        if (nativeEffectId != null && ctx.target instanceof Ref<?> ref) {
            @SuppressWarnings("unchecked")
            Ref<EntityStore> entityRef = (Ref<EntityStore>) ref;
            applyNative(entityRef, ctx);
        }
    }

    private void applyNative(Ref<EntityStore> entityRef, CastContext ctx) {
        if (!entityRef.isValid()) return;

        Store<EntityStore> store = entityRef.getStore();
        if (store == null) return;

        EffectControllerComponent controller = store.getComponent(entityRef, EffectControllerComponent.getComponentType());
        if (controller == null) {
            controller = new EffectControllerComponent();
            store.putComponent(entityRef, EffectControllerComponent.getComponentType(), controller);
        }

        // Try bare name first, then fall back to runecore: namespace prefix
        EntityEffect nativeEffect = EntityEffect.getAssetMap().getAsset(nativeEffectId);
        int index = EntityEffect.getAssetMap().getIndex(nativeEffectId);

        if (nativeEffect == null || index < 0) {
            String namespacedId = "runecore:" + nativeEffectId;
            nativeEffect = EntityEffect.getAssetMap().getAsset(namespacedId);
            index = EntityEffect.getAssetMap().getIndex(namespacedId);
        }

        if (nativeEffect != null && index >= 0) {
            float durationSecs = defaultDurationTicks / 20.0f;
            System.out.println("[RuneCore] Applying native effect: " + nativeEffectId + " (Index: " + index + ", Duration: " + durationSecs + "s)");
            controller.addEffect(entityRef, index, nativeEffect, durationSecs, OverlapBehavior.OVERWRITE, store);
        } else {
            System.err.println("[RuneCore] Native effect not found (tried bare + runecore: prefix): " + nativeEffectId);
        }
    }


    public String getId() { return id; }
    public int getDurationTicks() { return defaultDurationTicks; }
    public boolean isInstant() { return isInstant; }
    public int getAmplifier() { return amplifier; }
    public String getAssetPath() { return assetPath; }
}
