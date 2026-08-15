package com.cookie.runecore.api;

import com.cookie.runecore.systems.ui.RuneCoreHud;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.ColorLight;
import com.hypixel.hytale.protocol.packets.interface_.HudComponent;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Helpers for visual status effects that affect entity appearance and native effect icons.
 * <p>
 * All apply/revert flows share the same shape: update HUD flag, update
 * {@link PlayerDataComponent} flag, and optionally touch DynamicLight / native effect icons.
 * That shared shape is centralised in {@link #setHudAndData} so each effect only declares
 * what's different about it.
 */
public final class VisualEffectHelper {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private VisualEffectHelper() {}

    // ── Glowing ───────────────────────────────────────────────────────────────

    public static void applyGlowing(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            setHudAndData(ref, true, hud -> hud.setGlowing(true), data -> data.setGlowing(true));
            // Subtle local lighting: Radius 1, Low-intensity yellow
            applyDynamicLight(ref.getStore(), ref, (byte) 1, (byte) 32, (byte) 32, (byte) 0);
        });
    }

    public static void revertGlowing(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            setHudAndData(ref, false, hud -> hud.setGlowing(false), data -> data.setGlowing(false));
            removeDynamicLight(ref.getStore(), ref);
            removeNativeEffect(ref.getStore(), ref, "Glowing");
        });
    }

    // ── Night Vision ──────────────────────────────────────────────────────────

    public static void applyNightVision(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            setHudAndData(ref, true, hud -> hud.setNightVision(true), data -> data.setNightVision(true));
            // Global/FullBright: Radius -1, White (R:-1, G:-1, B:-1)
            applyDynamicLight(ref.getStore(), ref, (byte) -1, (byte) -1, (byte) -1, (byte) -1);
        });
    }

    public static void revertNightVision(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            setHudAndData(ref, false, hud -> hud.setNightVision(false), data -> data.setNightVision(false));
            removeDynamicLight(ref.getStore(), ref);
            removeNativeEffect(ref.getStore(), ref, "NightVision");
        });
    }

    // ── Blindness ─────────────────────────────────────────────────────────────

    public static void applyBlindness(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            LOG.fine("[RuneCore] Applying blindness visual to " + ref);
            setHudAndData(ref, true, hud -> hud.setBlinded(true), data -> data.setBlinded(true));
            // Slow down non-player entities when blinded to simulate confusion/loss of sight
            StatHelper.applyStatModifier(ref, "WalkSpeed", "Blindness", 0.4f,
                    com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier.CalculationType.MULTIPLICATIVE);
        });
    }

    public static void revertBlindness(Ref<EntityStore> ref) {
        EffectHelper.worldExecute(ref, () -> {
            setHudAndData(ref, true, hud -> hud.setBlinded(false), data -> data.setBlinded(false));
            StatHelper.removeStatModifier(ref, "WalkSpeed", "Blindness");
            removeNativeEffect(ref.getStore(), ref, "Blindness");
        });
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /**
     * Updates the HUD flag and mirrors it onto {@link PlayerDataComponent}.
     *
     * @param ensure if true, uses {@code ensureAndGetComponent} (creates the component if
     *               missing); if false, uses {@code getComponent} (no-op if it's not there,
     *               used by most revert paths since the effect being removed implies the
     *               component already exists).
     */
    private static void setHudAndData(Ref<EntityStore> ref, boolean ensure,
            Consumer<RuneCoreHud> hudSetter, Consumer<PlayerDataComponent> dataSetter) {
        EffectHelper.updateHud(ref, hudSetter);

        Store<EntityStore> store = ref.getStore();
        PlayerDataComponent data = ensure
                ? store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE)
                : store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null) {
            dataSetter.accept(data);
            store.putComponent(ref, PlayerDataComponent.TYPE, data);
        }
    }

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