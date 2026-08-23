package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.ApplyLookType;
import com.hypixel.hytale.protocol.AttachedToType;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.RotationType;
import com.hypixel.hytale.protocol.ServerCameraSettings;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Nausea: the only effect that drives the client camera, which is why it gets its own file.
 */
final class NauseaEffect {

    /** How fast the view spins, in degrees per tick of effect time. */
    private static final float SPIN_SPEED = 4.0f;
    /** Peak vertical sway, in degrees. */
    private static final float SWAY_AMPLITUDE = 20.0f;
    private static final float SWAY_FREQUENCY = 0.15f;
    private static final float LERP_SPEED = 0.8f;

    private NauseaEffect() {}

    static void apply(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setNausea(true));
    }

    static void revert(Ref<EntityStore> ref) {
        EffectHelper.updateHud(ref, hud -> hud.setNausea(false));

        EffectTargets.withPlayerRef(ref, (store, playerRef) -> {
            ServerCameraSettings reset = new ServerCameraSettings();
            reset.attachedToType = AttachedToType.LocalPlayer;
            reset.eyeOffset = true;
            reset.isFirstPerson = true;
            playerRef.getPacketHandler().write(
                    new SetServerCamera(ClientCameraView.FirstPerson, false, reset));

            // Also drop the native effect, otherwise the client keeps its own visual running.
            EffectControllerComponent controller =
                    store.getComponent(ref, EffectControllerComponent.getComponentType());
            if (controller != null) {
                int index = EffectHelper.getEffectIndex("Nausea");
                if (index >= 0) controller.removeEffect(ref, index, store);
            }
        });
    }

    /** @param time seconds the effect has been running; drives the sway. */
    static void onTick(Ref<EntityStore> ref, float time) {
        EffectTargets.withPlayerRef(ref, (store, playerRef) -> {
            ServerCameraSettings settings = new ServerCameraSettings();
            settings.rotation = new Direction(
                    (time * SPIN_SPEED) % 360.0f,
                    (float) Math.sin(time * SWAY_FREQUENCY) * SWAY_AMPLITUDE,
                    0.0f);
            settings.rotationType = RotationType.Custom;
            settings.applyLookType = ApplyLookType.LocalPlayerLookOrientation;
            settings.rotationLerpSpeed = LERP_SPEED;
            settings.attachedToType = AttachedToType.LocalPlayer;
            settings.eyeOffset = true;
            settings.isFirstPerson = true;
            playerRef.getPacketHandler().write(
                    new SetServerCamera(ClientCameraView.Custom, true, settings));
        });

        // For mobs/NPCs (non-player entities), apply periodic stagger velocity to simulate nausea/stumbling
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            if (store != null && store.getComponent(ref, PlayerRef.getComponentType()) == null) {
                EffectHelper.worldExecute(ref, () -> {
                    Velocity vc =
                            store.getComponent(ref, Velocity.getComponentType());
                    if (vc != null) {
                        double pushX = Math.sin(time * 2.0) * 1.5;
                        double pushZ = Math.cos(time * 2.0) * 1.5;
                        vc.set(pushX, vc.getVelocity().y, pushZ);
                    }
                });
            }
        }
    }
}
