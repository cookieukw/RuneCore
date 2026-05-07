package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Helpers for movement-based status effects: speed, jump, levitation, slow falling, frozen.
 */
public final class MovementHelper {

    private MovementHelper() {}

    // ── Removed Speed (handled by JSON natively) ──────────────────────────────

    public static void applySlowness(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.baseSpeed = Math.max(1.0f, s.baseSpeed - 2.0f));
    }

    public static void revertSlowness(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.baseSpeed += 2.0f);
    }

    // ── Jump ──────────────────────────────────────────────────────────────────

    public static void applyJumpBoost(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.jumpForce += 5.0f);
    }

    public static void revertJumpBoost(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.jumpForce = Math.max(EffectHelper.DEFAULT_JUMP_FORCE, s.jumpForce - 5.0f));
    }

    public static void applyHighJump(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.jumpForce += 10.0f);
    }

    public static void revertHighJump(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> s.jumpForce = Math.max(EffectHelper.DEFAULT_JUMP_FORCE, s.jumpForce - 10.0f));
    }

    // ── Slow Falling ──────────────────────────────────────────────────────────

    public static void applySlowFalling(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> {
            s.mass = 0.15f;
            s.airDragMax = 0.6f;
            s.airDragMaxSpeed = 0.5f;
        });
    }

    public static void revertSlowFalling(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> {
            s.mass = EffectHelper.DEFAULT_MASS;
            s.airDragMax = EffectHelper.DEFAULT_AIR_DRAG_MAX;
            s.airDragMaxSpeed = 6.0f;
        });
    }

    // ── Levitation ────────────────────────────────────────────────────────────

    public static void applyLevitation(Ref<EntityStore> ref) {
        EffectHelper.modifyMovement(ref, s -> {
            s.invertedGravity = true;
            s.mass = 0.05f;
            s.airDragMax = 3.5f;
            s.airDragMaxSpeed = 0.1f;
        });
    }

    public static void revertLevitation(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        // Step 1 — Reset physics settings to engine defaults and stop acceleration
        MovementManager mm = (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
        if (mm != null) {
            mm.applyDefaultSettings();
            MovementSettings settings = mm.getSettings();
            if (settings != null) {
                settings.baseSpeed = 0.0f;
                settings.invertedGravity = false;
                EffectHelper.syncSettings(store, ref, settings);
            }
        }

        // Step 2 — Clear ghost inputs to prevent the character from moving on its own
        clearMovementStates(store, ref);

        // Step 3 — Kill inertia and restore speed in next world tick
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world != null) {
            world.execute(() -> {
                if (!ref.isValid()) return;
                Velocity vc = store.getComponent(ref, Velocity.getComponentType());
                if (vc != null) vc.setZero();
                EffectHelper.modifyMovement(ref, m -> m.baseSpeed = EffectHelper.DEFAULT_SPEED);
            });
        }
    }

    // ── Frozen ────────────────────────────────────────────────────────────────

    public static void applyFrozen(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null) {
            data.setFrozen(true);
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                Vector3f rot = transform.getRotation();
                data.setFrozenRotX(rot.x);
                data.setFrozenRotY(rot.y);
                data.setFrozenRotZ(rot.z);
            }
        }

        EffectHelper.updateHud(ref, hud -> hud.setFrozen(true));

        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr != null) EffectHelper.sendCameraLock(pr, true);
    }

    public static void revertFrozen(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null) data.setFrozen(false);

        EffectHelper.updateHud(ref, hud -> hud.setFrozen(false));

        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (pr != null) EffectHelper.sendCameraLock(pr, false);
    }

    public static void onFrozenTick(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null && data.isFrozen()) {
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                transform.setRotation(new Vector3f(data.getFrozenRotX(), data.getFrozenRotY(), data.getFrozenRotZ()));
            }
        }

        EffectHelper.spawnParticleEffect(ref, "hytale:ice_shards");
    }

    // ── Internal ─────────────────────────────────────────────────────────────

    /** Sets movement states to idle (active=false) or active (active=true). */
    private static void clearMovementStates(Store<EntityStore> store, Ref<EntityStore> ref) {
        MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
        if (msc == null) return;
        MovementStates states = msc.getMovementStates();
        if (states == null) return;
        states.walking = false;
        states.running = false;
        states.sprinting = false;
        states.jumping = false;
        states.idle = true;
        states.horizontalIdle = true;
    }
}
