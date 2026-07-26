package com.cookie.runecore.api;

import java.util.UUID;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.ApplyLookType;
import com.hypixel.hytale.protocol.AttachedToType;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.RotationType;
import com.hypixel.hytale.protocol.ServerCameraSettings;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Helpers for gameplay status effects: bleeding, burn, nausea, haste,
 * mining fatigue, and invisibility.
 * <p>
 * Boilerplate for "get store → get PlayerRef → do something" is encapsulated
 * in {@link #withPlayerRef} and {@link #withWorldAndPlayer} to avoid repetition.
 */
public final class StatusEffectHelper {

    private StatusEffectHelper() {}

    // ── Bleeding ──────────────────────────────────────────────────────────────

    public static void applyBleeding(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBleeding(true));
    }

    public static void revertBleeding(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBleeding(false));
    }

    public static void onBleedingTick(Ref<EntityStore> ref) {
        System.out.println("[RuneCore-Bleed] Ticking bleeding for " + ref);
        // Spawn 2-3 drops of blood at different heights (chest/limbs)
        for (int i = 0; i < 2; i++) {
            double height = 0.8 + (Math.random() * 0.7); // Height between 0.8m and 1.5m (chest area)
            double spread = (Math.random() - 0.5) * 0.3; // Small horizontal spread
            // EffectHelper.spawnParticleEffect(ref, "runecore:Blood_Drop", spread, height, spread);
        }
    }

    // ── Burn ──────────────────────────────────────────────────────────────────

    public static void applyBurn(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBurning(true));
    }

    public static void revertBurn(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setBurning(false));
    }

    public static void onBurnTick(Ref<EntityStore> ref) {
        // Particles and visuals are handled by the native Burn.json entity effect
    }

    // ── Nausea ────────────────────────────────────────────────────────────────

    public static void applyNausea(Ref<EntityStore> ref) {
        System.out.println("[RuneCore-Nausea] applyNausea called for: " + ref);
        EffectHelper.updateHud(ref, hud -> hud.setNausea(true));
    }

    public static void revertNausea(Ref<EntityStore> ref) {
        System.out.println("[RuneCore-Nausea] revertNausea called for: " + ref);
        EffectHelper.updateHud(ref, hud -> hud.setNausea(false));
        withPlayerRef(ref, (store, pr) -> {
            ServerCameraSettings reset = new ServerCameraSettings();
            reset.attachedToType = AttachedToType.LocalPlayer;
            reset.eyeOffset = true;
            reset.isFirstPerson = true;
            pr.getPacketHandler().write(new SetServerCamera(ClientCameraView.FirstPerson, false, reset));

            EffectControllerComponent ctrl = store.getComponent(ref,
                    EffectControllerComponent.getComponentType());
            if (ctrl != null) {
                int idx = EffectHelper.getEffectIndex("Nausea");
                if (idx >= 0) ctrl.removeEffect(ref, idx, store);
            }
            System.out.println("[RuneCore-Nausea] Nausea reverted, camera reset packet sent.");
        });
    }

    public static void onNauseaTick(Ref<EntityStore> ref, float time) {
        withPlayerRef(ref, (store, pr) -> {
            System.out.println("[RuneCore-Nausea] onNauseaTick: sending camera sway at time " + time + " to: " + pr.getUsername());

            ServerCameraSettings settings = new ServerCameraSettings();
            settings.rotation = new Direction((time * 4.0f) % 360.0f,
                    (float) Math.sin(time * 0.15f) * 20.0f, 0.0f);
            settings.rotationType = RotationType.Custom;
            settings.applyLookType = ApplyLookType.LocalPlayerLookOrientation;
            settings.rotationLerpSpeed = 0.8f;
            settings.attachedToType = AttachedToType.LocalPlayer;
            settings.eyeOffset = true;
            settings.isFirstPerson = true;
            pr.getPacketHandler().write(new SetServerCamera(ClientCameraView.Custom, true, settings));
        });
    }

    // ── Haste ─────────────────────────────────────────────────────────────────

    public static void applyHaste(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setHaste(true));
        StatHelper.applyStatModifier(ref, "Stamina", "Haste", 1.5f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    public static void revertHaste(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setHaste(false));
        StatHelper.removeStatModifier(ref, "Stamina", "Haste");
    }

    // ── Mining Fatigue ────────────────────────────────────────────────────────

    public static void applyMiningFatigue(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setMiningFatigue(true));
        StatHelper.applyStatModifier(ref, "Stamina", "Mining_Fatigue", 0.3f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    public static void revertMiningFatigue(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setMiningFatigue(false));
        StatHelper.removeStatModifier(ref, "Stamina", "Mining_Fatigue");
    }

    // ── Water Breathing ───────────────────────────────────────────────────────

    public static void onWaterBreathingTick(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
        if (statMap == null) return;
        statMap.setStatValue(DefaultEntityStatTypes.getOxygen(), 100f);
    }

    // ── Strength ─────────────────────────────────────────────────────────────

    public static void applyStrength(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, "Health", "Strength",
                20f, StaticModifier.CalculationType.ADDITIVE);
    }

    public static void revertStrength(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, "Health", "Strength");
    }

    // ── Weakness ─────────────────────────────────────────────────────────────

    public static void applyWeakness(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, "Health", "Weakness",
                -20f, StaticModifier.CalculationType.ADDITIVE);
    }

    public static void revertWeakness(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, "Health", "Weakness");
    }

    // ── Resistance ───────────────────────────────────────────────────────────

    public static void applyResistance(Ref<EntityStore> ref) {
        StatHelper.applyStatModifier(ref, "Health", "Resistance",
                1.2f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    public static void revertResistance(Ref<EntityStore> ref) {
        StatHelper.removeStatModifier(ref, "Health", "Resistance");
    }

    // ── Invisibility ──────────────────────────────────────────────────────────

    public static void applyInvisibility(Ref<EntityStore> ref) {
        withWorldAndPlayer(ref, (world, uuid) ->
            world.execute(() -> {
                for (PlayerRef observer : world.getPlayerRefs()) {
                    observer.getHiddenPlayersManager().hidePlayer(uuid);
                }
            })
        );
    }

    public static void revertInvisibility(Ref<EntityStore> ref) {
        withWorldAndPlayer(ref, (world, uuid) ->
            world.execute(() -> {
                for (PlayerRef observer : world.getPlayerRefs()) {
                    observer.getHiddenPlayersManager().showPlayer(uuid);
                }
            })
        );
    }

    // ── Internal: Boilerplate reducers ───────────────────────────────────────

    @FunctionalInterface
    private interface StoreAction {
        void accept(Store<EntityStore> store);
    }

    @FunctionalInterface
    private interface PlayerRefAction {
        void accept(Store<EntityStore> store, PlayerRef pr);
    }

    @FunctionalInterface
    private interface WorldPlayerAction {
        void accept(World world, UUID uuid);
    }

    private static void withStore(Ref<EntityStore> ref, StoreAction action) {
        Store<EntityStore> store = ref.getStore();
        if (store != null) action.accept(store);
    }

    private static void withPlayerRef(Ref<EntityStore> ref, PlayerRefAction action) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr != null && pr.getPacketHandler() != null) action.accept(store, pr);
    }

    private static void withWorldAndPlayer(Ref<EntityStore> ref, WorldPlayerAction action) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr == null) return;
        action.accept(world, pr.getUuid());
    }
}
