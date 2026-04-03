package com.cookie.runecore.api;

import com.cookie.runecore.systems.EffectTickSystem;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.function.Function;

/**
 * Represents a status effect in RuneCore.
 *
 * An effect can be:
 *   1. A custom buff (via buffFactory) — functional effects that tick over time.
 *   2. A native Hytale entity effect (via nativeEffectId) — visual/cosmetic effects.
 *   3. An instant action (via action).
 *   4. Any combination of the above.
 */
public class RuneEffect {
    private final String id;
    private final int defaultDurationTicks;
    private final boolean isInstant;

    // Functional buff factory: creates an ActiveBuff when the effect is applied
    private Function<CastContext, ActiveBuff> buffFactory = null;

    // Optional instant callback (instant effects like direct damage)
    private IEffectAction action = null;

    // Optional native Hytale effect (cosmetic/visual)
    private String nativeEffectId = null;
    private String assetPath = null;
    private int amplifier = 0;

    // ─── Constructors ──────────────────────────────────────────────────────────

    public RuneEffect(String id, int defaultDurationTicks) {
        this.id = id;
        this.defaultDurationTicks = defaultDurationTicks;
        this.isInstant = false;
    }

    /** Instant effect with a custom action */
    public RuneEffect(String id, boolean isInstant, int durationTicks, IEffectAction action) {
        this.id = id;
        this.isInstant = isInstant;
        this.defaultDurationTicks = durationTicks;
        this.action = action;
    }

    /** Native Hytale effect (cosmetic only) */
    public RuneEffect(String id, String nativeEffectId, int durationTicks) {
        this.id = id;
        this.isInstant = false;
        this.defaultDurationTicks = durationTicks;
        this.nativeEffectId = nativeEffectId;
    }

    // ─── Builder Methods ───────────────────────────────────────────────────────

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

    /**
     * Links a Hytale JSON asset to this effect.
     * Developers can use this to provide icons, tints, and particles.
     */
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

    // ─── Execution ─────────────────────────────────────────────────────────────

    public void execute(CastContext ctx) {
        // 1. Run instant actions
        if (this.action != null) {
            this.action.apply(ctx);
        }

        // 2. Register a functional buff if factory is set
        if (this.buffFactory != null && ctx.target instanceof Ref<?> ref) {
            @SuppressWarnings("unchecked")
            Ref<EntityStore> entityRef = (Ref<EntityStore>) ref;
            if (entityRef.isValid()) {
                ActiveBuff buff = this.buffFactory.apply(ctx);
                EffectTickSystem.getInstance().applyBuff(buff, entityRef);
            }
        }

        // 3. Apply native Hytale effect if defined
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

        EntityEffect nativeEffect = EntityEffect.getAssetMap().getAsset(nativeEffectId);
        int index = EntityEffect.getAssetMap().getIndex(nativeEffectId);

        if (nativeEffect != null) {
            float durationSecs = defaultDurationTicks / 20.0f;
            controller.addEffect(entityRef, index, nativeEffect, durationSecs, OverlapBehavior.EXTEND, store);
            System.out.println("[RuneCore] Applied native effect " + nativeEffectId + " (Amp: " + amplifier + ")");
        } else {
            System.err.println("[RuneCore] Native effect not found: " + nativeEffectId);
        }
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public String getId() { return id; }
    public int getDurationTicks() { return defaultDurationTicks; }
    public boolean isInstant() { return isInstant; }
    public int getAmplifier() { return amplifier; }
    public String getAssetPath() { return assetPath; }
}
