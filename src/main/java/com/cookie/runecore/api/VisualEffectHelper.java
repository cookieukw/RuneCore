package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.ColorLight;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.cookie.runecore.systems.ui.RuneCoreHud;

/**
 * Helpers for visual status effects that affect entity appearance and native effect icons.
 * <p>
 * DynamicLight creation/removal is centralised in {@link #applyDynamicLight} and
 * {@link #removeDynamicLight} to avoid duplication across Glowing and NightVision.
 * Native icon removal is handled by {@link #removeNativeEffect}.
 */
public final class VisualEffectHelper {

    private VisualEffectHelper() {}

    // ── Glowing ───────────────────────────────────────────────────────────────

    public static void applyGlowing(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            EffectHelper.updateHud(ref, hud -> hud.setGlowing(true));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setGlowing(true);

            // Subtle local lighting: Radius 1, Low-intensity yellow
            applyDynamicLight(store, ref, (byte) 1, (byte) 32, (byte) 32, (byte) 0);
        });
    }

    public static void revertGlowing(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            EffectHelper.updateHud(ref, hud -> hud.setGlowing(false));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setGlowing(false);

            removeDynamicLight(store, ref);
            removeNativeEffect(store, ref, "Glowing");
        });
    }

    // ── Night Vision ──────────────────────────────────────────────────────────

    public static void applyNightVision(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            EffectHelper.updateHud(ref, hud -> hud.setNightVision(true));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setNightVision(true);

            // Global/FullBright: Radius -1, White (R:-1, G:-1, B:-1)
            applyDynamicLight(store, ref, (byte) -1, (byte) -1, (byte) -1, (byte) -1);
        });
    }

    public static void revertNightVision(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            EffectHelper.updateHud(ref, hud -> hud.setNightVision(false));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setNightVision(false);

            removeDynamicLight(store, ref);
            removeNativeEffect(store, ref, "NightVision");
        });
    }

    // ── Blindness ─────────────────────────────────────────────────────────────

    public static void applyBlindness(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            System.out.println("[RuneCore] Applying blindness visual to " + ref);
            EffectHelper.updateHud(ref, hud -> hud.setBlinded(true));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setBlinded(true);
        });
    }

    public static void revertBlindness(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            EffectHelper.updateHud(ref, hud -> hud.setBlinded(false));
            Store<EntityStore> store = ref.getStore();
            PlayerDataComponent data = store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setBlinded(false);

            removeNativeEffect(store, ref, "Blindness");
        });
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static void applyDynamicLight(Store<EntityStore> store, Ref<EntityStore> ref,
            byte radius, byte r, byte g, byte b) {
        store.putComponent(ref, DynamicLight.getComponentType(),
                new DynamicLight(new ColorLight(radius, r, g, b)));
    }

    private static void removeDynamicLight(Store<EntityStore> store, Ref<EntityStore> ref) {
        store.removeComponent(ref, DynamicLight.getComponentType());
    }

    private static void removeNativeEffect(Store<EntityStore> store, Ref<EntityStore> ref,
            String effectName) {
        EffectControllerComponent controller = (EffectControllerComponent) store.getComponent(ref,
                EffectControllerComponent.getComponentType());
        if (controller == null) return;
        int index = EffectHelper.getEffectIndex(effectName);
        if (index >= 0) controller.removeEffect(ref, index, store);
    }
}
