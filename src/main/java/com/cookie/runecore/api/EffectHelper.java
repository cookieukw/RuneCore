package com.cookie.runecore.api;

import java.util.UUID;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.cookie.runecore.systems.ui.RuneCoreHudManager;
import com.cookie.runecore.systems.ui.RuneCoreHud;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.ServerCameraSettings;
import com.hypixel.hytale.protocol.Vector2f;
import com.cookie.runecore.api.PlayerDataComponent;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.math.vector.Vector3f;

public final class EffectHelper {

    public static final float DEFAULT_SPEED = 5.5f;
    public static final float DEFAULT_JUMP_FORCE = 8.0f;
    public static final float DEFAULT_MASS = 1.0f;
    public static final float DEFAULT_DRAG = 0.05f;
    public static final float DEFAULT_AIR_DRAG_MAX = 0.08f;

    private EffectHelper() {}


    public static void modifyMovement(Ref<EntityStore> ref, MovementModifier modifier) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        world.execute(() -> {
            if (!ref.isValid()) return;
            MovementManager moveManager = (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
            if (moveManager == null) return;

            MovementSettings settings = moveManager.getSettings();
            if (settings == null) return;

            modifier.apply(settings);
            syncSettings(store, ref, settings);
        });
    }

    private static void syncSettings(Store<EntityStore> store, Ref<EntityStore> ref, MovementSettings settings) {
        PlayerRef playerRefComp = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRefComp != null) {
            PacketHandler ph = playerRefComp.getPacketHandler();
            if (ph != null) {
                ph.write(new UpdateMovementSettings(settings));
            }
        }
    }


    public static void applySpeed(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.baseSpeed += 2.75f);
    }

    public static void revertSpeed(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.baseSpeed = Math.max(DEFAULT_SPEED, s.baseSpeed - 2.75f));
    }

    public static void applySlowness(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.baseSpeed = Math.max(1.0f, s.baseSpeed - 2.0f));
    }

    public static void revertSlowness(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.baseSpeed += 2.0f);
    }


    public static void applyJumpBoost(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.jumpForce += 5.0f);
    }

    public static void revertJumpBoost(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.jumpForce = Math.max(DEFAULT_JUMP_FORCE, s.jumpForce - 5.0f));
    }

    public static void applyHighJump(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.jumpForce += 10.0f);
    }

    public static void revertHighJump(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> s.jumpForce = Math.max(DEFAULT_JUMP_FORCE, s.jumpForce - 10.0f));
    }


    public static void applySlowFalling(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.mass = 0.15f;
            s.airDragMax = 0.6f;
            s.airDragMaxSpeed = 0.5f;
        });
    }

    public static void revertSlowFalling(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.mass = DEFAULT_MASS;
            s.airDragMax = DEFAULT_AIR_DRAG_MAX;
            s.airDragMaxSpeed = 6.0f;
        });
    }


    public static void applyLevitation(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.invertedGravity = true;
            s.mass = 0.05f;
            s.airDragMax = 3.5f;
            s.airDragMaxSpeed = 0.1f;
        });
    }

    public static void revertLevitation(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.invertedGravity = false;
            s.wishDirectionGravityY = 0.0f;
            s.mass = DEFAULT_MASS;
            s.airDragMax = DEFAULT_AIR_DRAG_MAX;
            s.airDragMaxSpeed = 6.0f;
        });
    }


    public static void applyFrozen(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed = 0.0f;
            s.jumpForce = 0.0f;
            s.swimJumpForce = 0.0f;
            s.fallJumpForce = 0.0f;
            s.autoJumpDisableJumping = true;
            s.jumpBufferDuration = 0.0f;
            s.jumpBufferMaxYVelocity = 0.0f;
        });

        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) {
                data.setFrozen(true);
                
                // Save current rotation to lock it every tick
                TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
                if (transform != null) {
                    Vector3f rot = transform.getRotation();
                    data.setFrozenRotX(rot.x);
                    data.setFrozenRotY(rot.y);
                    data.setFrozenRotZ(rot.z);
                }
            }

            // Force Movement State to Idle
            MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
            if (msc != null) {
                MovementStates states = msc.getMovementStates();
                states.idle = true;
                states.walking = false;
                states.running = false;
                states.sprinting = false;
                states.jumping = false;
            }

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setFrozen(true);
                
                // Lock Camera (with zero look sensitivity as backup)
                sendCameraLock(playerRef, true);
            }
        }
    }

    public static void revertFrozen(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed = DEFAULT_SPEED;
            s.jumpForce = DEFAULT_JUMP_FORCE;
            s.swimJumpForce = DEFAULT_JUMP_FORCE;
            s.fallJumpForce = DEFAULT_JUMP_FORCE;
            s.autoJumpDisableJumping = false;
            s.jumpBufferDuration = 0.5f; 
        });

        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) data.setFrozen(false);

            // Restore Movement State
            MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
            if (msc != null) {
                MovementStates states = msc.getMovementStates();
                states.idle = false; // Let the server/client recalculate
            }

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setFrozen(false);
                
                // Unlock Camera
                sendCameraLock(playerRef, false);
            }
        }
    }

    private static void sendCameraLock(PlayerRef playerRef, boolean locked) {
        if (playerRef == null || playerRef.getPacketHandler() == null) return;
        
        ServerCameraSettings settings = new ServerCameraSettings();
        if (locked) {
            // Effectively disable mouse look by zeroing the sensitivity/multiplier
            settings.lookMultiplier = new Vector2f(0.0f, 0.0f);
        }
        
        SetServerCamera packet = new SetServerCamera(
            ClientCameraView.FirstPerson, 
            locked, 
            settings
        );
        playerRef.getPacketHandler().write(packet);
    }

    public static void onFrozenTick(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        // 1. Force Rotation every tick (Server-side look lock)
        PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null && data.isFrozen()) {
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                transform.setRotation(new Vector3f(data.getFrozenRotX(), data.getFrozenRotY(), data.getFrozenRotZ()));
            }
        }

        // 2. Particles
        spawnParticleEffect(ref, "hytale:ice_shards");
    }

    public static void applyBleeding(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setBleeding(true);
            }
        }
    }

    public static void revertBleeding(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setBleeding(false);
            }
        }
    }

    public static void onBleedingTick(Ref<EntityStore> ref) {
        // Particles
        spawnParticleEffect(ref, "hytale:blood_impact");
        // Mechanical effect (damage) is handled by the buff onTick in CoreEffects, 
        // but we can add secondary effects here if needed.
    }

    public static void applyBurn(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setBurning(true);
            }
        }
    }

    public static void revertBurn(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
                if (hud != null) hud.setBurning(false);
            }
        }
    }

    public static void onBurnTick(Ref<EntityStore> ref) {
        // Particles
        spawnParticleEffect(ref, "hytale:fire");
    }

    private static void spawnParticleEffect(Ref<EntityStore> ref, String particleId) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform == null) return;

        Vector3d pos = transform.getPosition();
        ParticleUtil.spawnParticleEffect(particleId, pos, store);
    }


    public static void applyHaste(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed += 1.0f;
            s.forwardRunSpeedMultiplier += 0.2f;
        });
    }

    public static void revertHaste(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed = Math.max(DEFAULT_SPEED, s.baseSpeed - 1.0f);
            s.forwardRunSpeedMultiplier = Math.max(1.0f, s.forwardRunSpeedMultiplier - 0.2f);
        });
    }

    public static void applyMiningFatigue(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed = Math.max(2.0f, s.baseSpeed - 1.5f);
            s.forwardRunSpeedMultiplier = Math.max(0.5f, s.forwardRunSpeedMultiplier - 0.3f);
        });
    }

    public static void revertMiningFatigue(Ref<EntityStore> ref) {
        modifyMovement(ref, s -> {
            s.baseSpeed += 1.5f;
            s.forwardRunSpeedMultiplier += 0.3f;
        });
    }


    public static void subtractHealth(Ref<EntityStore> ref, float amount) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        world.execute(() -> {
            if (!ref.isValid()) return;
            EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap == null) return;
            EntityStatValue hp = statMap.get(DefaultEntityStatTypes.getHealth());
            if (hp == null) return;
            float newHp = Math.max(0f, hp.get() - amount);
            statMap.setStatValue(DefaultEntityStatTypes.getHealth(), newHp);
        });
    }

    public static void addHealth(Ref<EntityStore> ref, float amount) {
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        world.execute(() -> {
            if (!ref.isValid()) return;
            EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap == null) return;
            EntityStatValue hp = statMap.get(DefaultEntityStatTypes.getHealth());
            if (hp == null) return;
            float newHp = Math.min(100f, hp.get() + amount);
            statMap.setStatValue(DefaultEntityStatTypes.getHealth(), newHp);
        });
    }


    @FunctionalInterface
    public interface MovementModifier {
        void apply(MovementSettings settings);
    }

    public static void applyInvisibility(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        PlayerRef playerRefComp = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRefComp == null) return;
        UUID targetUuid = playerRefComp.getUuid();

        world.execute(() -> {
            for (PlayerRef observer : world.getPlayerRefs()) {
                observer.getHiddenPlayersManager().hidePlayer(targetUuid);
            }
        });
    }

    public static void revertInvisibility(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;
        World world = store.getExternalData().getWorld();
        if (world == null) return;

        PlayerRef playerRefComp = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRefComp == null) return;
        UUID targetUuid = playerRefComp.getUuid();

        world.execute(() -> {
            for (PlayerRef observer : world.getPlayerRefs()) {
                observer.getHiddenPlayersManager().showPlayer(targetUuid);
            }
        });
    }
}
