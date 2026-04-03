package com.cookie.runecore.api;

import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

public class RuneEffect {
    private final String id;
    private final IEffectAction action;
    private final int durationTicks;
    private final boolean isInstant;
    private String nativeEffectId = null;

    public RuneEffect(String id, boolean isInstant, int durationTicks, IEffectAction action) {
        this.id = id;
        this.isInstant = isInstant;
        this.durationTicks = durationTicks;
        this.action = action;
    }

    public RuneEffect(String id, String nativeEffectId, int durationTicks) {
        this(id, false, durationTicks, null);
        this.nativeEffectId = nativeEffectId;
    }

    public void execute(CastContext ctx) {
        // Run custom logic if exists
        if (this.action != null) {
            this.action.apply(ctx);
        }

        // Apply native Hytale effect if defined
        if (nativeEffectId != null && ctx.target instanceof Ref<?> ref) {
            applyNative(ref, ctx);
        }
    }

    @SuppressWarnings("unchecked")
    private void applyNative(Ref<?> targetRef, CastContext ctx) {
        if (!targetRef.isValid()) return;

        Ref<EntityStore> entityRef = (Ref<EntityStore>) targetRef;
        Store<EntityStore> store = entityRef.getStore();
        if (store == null) return;

        EffectControllerComponent controller = store.getComponent(entityRef, EffectControllerComponent.getComponentType());
        if (controller == null) {
            controller = new EffectControllerComponent();
            store.putComponent(entityRef, EffectControllerComponent.getComponentType(), controller);
        }

        EntityEffect nativeEffect = EntityEffect.getAssetMap().getAsset(nativeEffectId);
        int index = EntityEffect.getAssetMap().getIndex(nativeEffectId);

        if (nativeEffect != null) {
            float durationSecs = durationTicks / 20.0f;
            controller.addEffect(entityRef, index, nativeEffect, durationSecs, OverlapBehavior.EXTEND, store);
            System.out.println("[RuneCore] Applied native effect " + nativeEffectId + " to " + entityRef);
        } else {
            System.err.println("[RuneCore] Native effect not found: " + nativeEffectId);
        }
    }
    
    public String getId() { return id; }

    public int getDurationTicks() { return durationTicks; }

    public boolean isInstant() { return isInstant; }
}
