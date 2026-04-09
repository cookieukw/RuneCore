package com.cookie.runecore.api;

import java.util.UUID;
import java.util.function.Consumer;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
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
import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.RotationType;
import com.hypixel.hytale.protocol.ApplyLookType;
import com.hypixel.hytale.protocol.AttachedToType;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.protocol.ColorLight;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;

public final class EffectHelper {

    public static final float DEFAULT_SPEED = 5.5f;
    public static final float DEFAULT_JUMP_FORCE = 8.0f;
    public static final float DEFAULT_MASS = 1.0f;
    public static final float DEFAULT_DRAG = 0.05f;
    public static final float DEFAULT_AIR_DRAG_MAX = 0.08f;

    private EffectHelper() {
    }

    private static void updateHud(Ref<EntityStore> ref, Consumer<RuneCoreHud> action) {
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef != null) {
            RuneCoreHud hud = RuneCoreHudManager.get().getHud(playerRef.getUuid());
            if (hud != null)
                action.accept(hud);
        }
    }

    public static void modifyMovement(Ref<EntityStore> ref, MovementModifier modifier) {
        if (ref == null)
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        World world = store.getExternalData().getWorld();
        if (world == null)
            return;

        world.execute(() -> {
            if (!ref.isValid())
                return;
            MovementManager moveManager = (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
            if (moveManager == null)
                return;

            MovementSettings settings = moveManager.getSettings();
            if (settings == null)
                return;

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
        Store<EntityStore> store = ref.getStore();
        if (store == null) return;

        // Step 1 — Reset physics settings to engine defaults
        MovementManager moveManager = (MovementManager) store.getComponent(ref, MovementManager.getComponentType());
        if (moveManager != null) {
            moveManager.applyDefaultSettings();
            
            // Explicitly kill baseSpeed for one tick to ensure a clean state
            MovementSettings settings = moveManager.getSettings();
            if (settings != null) {
                settings.baseSpeed = 0.0f;
                settings.invertedGravity = false;
                syncSettings(store, ref, settings);
            }
        }

        // Step 2 — Reset input states to prevent "ghost movement"
        MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
        if (msc != null) {
            MovementStates states = msc.getMovementStates();
            if (states != null) {
                states.walking = false;
                states.running = false;
                states.sprinting = false;
                states.jumping = false;
                states.idle = true;
                states.horizontalIdle = true;
            }
        }

        // Step 3 — Kill physical inertia and restore speed in next tick
        World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
        if (world != null) {
            world.execute(() -> {
                if (!ref.isValid()) return;

                // Stop all physical momentum
                Velocity vc = store.getComponent(ref, Velocity.getComponentType());
                if (vc != null) {
                    vc.setZero();
                }

                // Restore default movement speed
                modifyMovement(ref, m -> m.baseSpeed = DEFAULT_SPEED);
            });
        }
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
                TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
                if (transform != null) {
                    Vector3f rot = transform.getRotation();
                    data.setFrozenRotX(rot.x);
                    data.setFrozenRotY(rot.y);
                    data.setFrozenRotZ(rot.z);
                }
            }

            MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
            if (msc != null) {
                MovementStates states = msc.getMovementStates();
                states.idle = true;
                states.walking = false;
                states.running = false;
                states.sprinting = false;
                states.jumping = false;
            }

            updateHud(ref, hud -> hud.setFrozen(true));

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
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
            if (data != null)
                data.setFrozen(false);

            MovementStatesComponent msc = store.getComponent(ref, MovementStatesComponent.getComponentType());
            if (msc != null) {
                MovementStates states = msc.getMovementStates();
                states.idle = false;
            }

            updateHud(ref, hud -> hud.setFrozen(false));

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null) {
                sendCameraLock(playerRef, false);
            }
        }
    }

    private static void sendCameraLock(PlayerRef playerRef, boolean locked) {
        if (playerRef == null || playerRef.getPacketHandler() == null)
            return;

        ServerCameraSettings settings = new ServerCameraSettings();
        if (locked) {
            settings.lookMultiplier = new Vector2f(0.0f, 0.0f);
        }

        SetServerCamera packet = new SetServerCamera(
                ClientCameraView.FirstPerson,
                locked,
                settings);
        playerRef.getPacketHandler().write(packet);
    }

    public static void onFrozenTick(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;

        PlayerDataComponent data = store.getComponent(ref, PlayerDataComponent.TYPE);
        if (data != null && data.isFrozen()) {
            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
            if (transform != null) {
                transform.setRotation(new Vector3f(data.getFrozenRotX(), data.getFrozenRotY(), data.getFrozenRotZ()));
            }
        }

        spawnParticleEffect(ref, "hytale:ice_shards");
    }

    public static void applyBleeding(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setBleeding(true));
    }

    public static void revertBleeding(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setBleeding(false));
    }

    public static void onBleedingTick(Ref<EntityStore> ref) {
        spawnParticleEffect(ref, "hytale:blood_impact");
    }

    public static void applyBurn(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setBurning(true));
    }

    public static void revertBurn(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setBurning(false));
    }

    public static void onBurnTick(Ref<EntityStore> ref) {
        spawnParticleEffect(ref, "hytale:fire");
    }

    public static void applyMiningFatigue(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setMiningFatigue(true));
        applyStatModifier(ref, "hytale:attack_speed", "Mining_Fatigue", 0.3f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
        applyStatModifier(ref, "hytale:mining_speed", "Mining_Fatigue", 0.3f,
                StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    public static void revertMiningFatigue(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setMiningFatigue(false));
        removeStatModifier(ref, "hytale:attack_speed", "Mining_Fatigue");
        removeStatModifier(ref, "hytale:mining_speed", "Mining_Fatigue");
    }

    public static void applyHaste(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setHaste(true));
        applyStatModifier(ref, "hytale:attack_speed", "Haste", 1.5f, StaticModifier.CalculationType.MULTIPLICATIVE);
        applyStatModifier(ref, "hytale:mining_speed", "Haste", 1.5f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    public static void revertHaste(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setHaste(false));
        removeStatModifier(ref, "hytale:attack_speed", "Haste");
        removeStatModifier(ref, "hytale:mining_speed", "Haste");
    }

    public static void applyNausea(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setNausea(true));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) {
                data.setNausea(true);
                data.setNauseaTime(0.0f);
            }
        }
    }

    public static void revertNausea(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setNausea(false));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null)
                data.setNausea(false);

            PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef != null && playerRef.getPacketHandler() != null) {
                ServerCameraSettings resetSettings = new ServerCameraSettings();
                resetSettings.attachedToType = AttachedToType.LocalPlayer;
                resetSettings.eyeOffset = true;
                resetSettings.isFirstPerson = true;

                SetServerCamera packet = new SetServerCamera(ClientCameraView.FirstPerson, false, resetSettings);
                playerRef.getPacketHandler().write(packet);
            }

            EffectControllerComponent controller = store.getComponent(ref,
                    EffectControllerComponent.getComponentType());
            if (controller != null) {
                int index = getEffectIndex("Nausea");
                if (index >= 0)
                    controller.removeEffect(ref, index, store);
            }
        }
    }

    public static void onNauseaTick(Ref<EntityStore> ref) {
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
        if (data == null || !data.isNausea())
            return;

        float time = data.getNauseaTime() + 1.0f;
        data.setNauseaTime(time);

        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef != null && playerRef.getPacketHandler() != null) {

            float yawSpin = (time * 4.0f) % 360.0f;
            float pitchWobble = (float) Math.sin(time * 0.15f) * 20.0f;

            ServerCameraSettings settings = new ServerCameraSettings();
            settings.rotation = new Direction(yawSpin, pitchWobble, 0.0f);
            settings.rotationType = RotationType.Custom;
            settings.applyLookType = ApplyLookType.LocalPlayerLookOrientation;
            settings.rotationLerpSpeed = 0.8f;

            settings.attachedToType = AttachedToType.LocalPlayer;
            settings.eyeOffset = true;
            settings.isFirstPerson = true;

            SetServerCamera packet = new SetServerCamera(
                    ClientCameraView.Custom,
                    true,
                    settings);
            playerRef.getPacketHandler().write(packet);
        }
    }

    private static int getEffectIndex(String id) {
        int index = EntityEffect.getAssetMap().getIndex(id);
        if (index < 0) {
            index = EntityEffect.getAssetMap().getIndex("runecore:" + id);
        }
        return index;
    }

    public static void applyBlindness(Ref<EntityStore> ref) {

        updateHud(ref, hud -> hud.setBlinded(true));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) {
                data.setBlinded(true);
            }
        }
    }

    public static void revertBlindness(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setBlinded(false));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null) {
                data.setBlinded(false);
            }

            // Forced sync: remove the native icon now
            EffectControllerComponent controller = store.getComponent(ref,
                    EffectControllerComponent.getComponentType());
            if (controller != null) {
                int index = getEffectIndex("Blindness");
                if (index >= 0)
                    controller.removeEffect(ref, index, store);
            }
        }
    }

    public static void applyGlowing(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setGlowing(true));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null)
                data.setGlowing(true);

            // Subtle local lighting (Radius 1, Low Intensity Yellow)
            ColorLight light = new ColorLight((byte) 1, (byte) 32, (byte) 32, (byte) 0);
            store.putComponent(ref, DynamicLight.getComponentType(), new DynamicLight(light));
            // Status icon is applied by RuneEffect.applyNative() with correct duration + namespace fallback
        }
    }

    public static void revertGlowing(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setGlowing(false));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null)
                data.setGlowing(false);

            store.removeComponent(ref, DynamicLight.getComponentType());

            EffectControllerComponent controller = (EffectControllerComponent) store.getComponent(ref,
                    EffectControllerComponent.getComponentType());
            if (controller != null) {
                int effectIndex = getEffectIndex("Glowing");
                if (effectIndex >= 0) {
                    controller.removeEffect(ref, effectIndex, store);
                }
            }
        }
    }

    public static void applyNightVision(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setNightVision(true));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.ensureAndGetComponent(ref, PlayerDataComponent.TYPE);
            if (data != null)
                data.setNightVision(true);

            // Radius -1 (Global/FullBright), White (R: 255, G: 255, B: 255)
            ColorLight light = new ColorLight((byte) -1, (byte) -1, (byte) -1, (byte) -1);
            store.putComponent(ref, DynamicLight.getComponentType(), new DynamicLight(light));
            // Status icon is applied by RuneEffect.applyNative() with correct duration + namespace fallback
        }
    }

    public static void revertNightVision(Ref<EntityStore> ref) {
        updateHud(ref, hud -> hud.setNightVision(false));
        Store<EntityStore> store = ref.getStore();
        if (store != null) {
            PlayerDataComponent data = (PlayerDataComponent) store.getComponent(ref, PlayerDataComponent.TYPE);
            if (data != null)
                data.setNightVision(false);
            store.removeComponent(ref, DynamicLight.getComponentType());

            EffectControllerComponent controller = (EffectControllerComponent) store.getComponent(ref,
                    EffectControllerComponent.getComponentType());
            if (controller != null) {
                int index = getEffectIndex("NightVision");
                if (index >= 0) {
                    controller.removeEffect(ref, index, store);
                }
            }
        }
    }

    private static void applyStatModifier(Ref<EntityStore> ref, String statId, String modifierId, float value,
            StaticModifier.CalculationType type) {
        EntityStatMap statMap = (EntityStatMap) ref.getStore().getComponent(ref,
                EntityStatsModule.get().getEntityStatMapComponentType());
        if (statMap == null)
            return;

        int index = EntityStatType.getAssetMap().getIndex(statId);
        if (index >= 0) {
            statMap.putModifier(index, modifierId, new StaticModifier(Modifier.ModifierTarget.MAX, type, value));
        }
    }

    private static void removeStatModifier(Ref<EntityStore> ref, String statId, String modifierId) {
        EntityStatMap statMap = (EntityStatMap) ref.getStore().getComponent(ref,
                EntityStatsModule.get().getEntityStatMapComponentType());
        if (statMap == null)
            return;

        int index = EntityStatType.getAssetMap().getIndex(statId);
        if (index >= 0) {
            statMap.removeModifier(index, modifierId);
        }
    }

    private static void spawnParticleEffect(Ref<EntityStore> ref, String particleId) {
        if (ref == null || !ref.isValid())
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;

        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform == null)
            return;

        Vector3d pos = transform.getPosition();
        ParticleUtil.spawnParticleEffect(particleId, pos, store);
    }

    public static void addHealth(Ref<EntityStore> ref, float amount) {
        if (ref == null)
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        World world = store.getExternalData().getWorld();
        if (world == null)
            return;

        world.execute(() -> {
            if (!ref.isValid())
                return;
            EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap == null)
                return;
            EntityStatValue hp = statMap.get(DefaultEntityStatTypes.getHealth());
            if (hp == null)
                return;
            float newHp = Math.min(100f, hp.get() + amount);
            statMap.setStatValue(DefaultEntityStatTypes.getHealth(), newHp);
        });
    }

    public static void subtractHealth(Ref<EntityStore> ref, float amount) {
        if (ref == null)
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        World world = store.getExternalData().getWorld();
        if (world == null)
            return;

        world.execute(() -> {
            if (!ref.isValid())
                return;
            EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (statMap == null)
                return;
            EntityStatValue hp = statMap.get(DefaultEntityStatTypes.getHealth());
            if (hp == null)
                return;
            float newHp = Math.max(0f, hp.get() - amount);
            statMap.setStatValue(DefaultEntityStatTypes.getHealth(), newHp);
        });
    }

    @FunctionalInterface
    public interface MovementModifier {
        void apply(MovementSettings settings);
    }

    public static void applyInvisibility(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid())
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        World world = store.getExternalData().getWorld();
        if (world == null)
            return;

        PlayerRef playerRefComp = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRefComp == null)
            return;
        UUID targetUuid = playerRefComp.getUuid();

        world.execute(() -> {
            for (PlayerRef observer : world.getPlayerRefs()) {
                observer.getHiddenPlayersManager().hidePlayer(targetUuid);
            }
        });
    }

    public static void revertInvisibility(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid())
            return;
        Store<EntityStore> store = ref.getStore();
        if (store == null)
            return;
        World world = store.getExternalData().getWorld();
        if (world == null)
            return;

        PlayerRef playerRefComp = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRefComp == null)
            return;
        UUID targetUuid = playerRefComp.getUuid();

        world.execute(() -> {
            for (PlayerRef observer : world.getPlayerRefs()) {
                observer.getHiddenPlayersManager().showPlayer(targetUuid);
            }
        });
    }
}
