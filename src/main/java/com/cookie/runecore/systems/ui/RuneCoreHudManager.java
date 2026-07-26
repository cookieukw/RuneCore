package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.protocol.packets.interface_.HudComponent;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.entity.effect.ActiveEntityEffect;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.cookie.runecore.api.ActiveBuff;
import com.cookie.runecore.system.RuneCore;
import com.cookie.runecore.systems.EffectTickSystem;

public class RuneCoreHudManager {

    private static RuneCoreHudManager instance;
    private final Map<UUID, RuneCoreHud> activeHuds = new ConcurrentHashMap<>();
    private final Timer updateTimer = new Timer("RuneCore-HudManager", true);

    public RuneCoreHudManager(EventRegistry eventRegistry) {
        instance = this;
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllHuds();
            }
        }, 1000, 50);
    }

    public static RuneCoreHudManager get() {
        return instance;
    }

    public RuneCoreHud getHud(UUID uuid) {
        return activeHuds.get(uuid);
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> entityRef = event.getPlayerRef();
        if (entityRef == null || !entityRef.isValid()) return;

        Store<EntityStore> store = entityRef.getStore();
        if (store == null) return;

        PlayerRef playerRef = (PlayerRef) store.getComponent(entityRef, PlayerRef.getComponentType());
        if (playerRef == null) return;

        Player player = event.getPlayer();
        if (player == null) return;

        // Hide original Hytale HUDs (Keep Health visible since we only implement Mana for now)
        System.out.println("[RuneCore-HUD] Hiding vanilla components for " + playerRef.getUuid());
        player.getHudManager().hideHudComponents(playerRef, HudComponent.Mana, HudComponent.StatusIcons);

        RuneCoreHud hud = new RuneCoreHud(playerRef);
        activeHuds.put(playerRef.getUuid(), hud);
        hud.show();
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        activeHuds.remove(event.getPlayerRef().getUuid());
    }

    private void updateAllHuds() {
        if (activeHuds.isEmpty()) return;

        for (RuneCoreHud hud : activeHuds.values()) {
            PlayerRef playerRef = hud.getPlayerRef();
            if (playerRef == null || !playerRef.isValid()) continue;

            Ref<EntityStore> ref = playerRef.getReference();
            if (ref == null || !ref.isValid()) continue;

            Store<EntityStore> store = ref.getStore();
            if (store == null) continue;

            EntityStore entityStore = store.getExternalData();
            if (entityStore == null) continue;

            World world = entityStore.getWorld();
            if (world == null) continue;

            world.execute(() -> {
                if (!ref.isValid()) return;
                EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
                if (statMap == null) return;
                
                EntityStatValue manaVal = statMap.get(DefaultEntityStatTypes.getMana());
                if (manaVal != null) {
                    hud.setMana(manaVal.get(), 100f);
                }

                // Sync Buffs directly from Hytale's EffectControllerComponent
                EffectControllerComponent controller = 
                    (EffectControllerComponent) store.getComponent(ref, EffectControllerComponent.getComponentType());
                List<ActiveBuff> buffs = new java.util.ArrayList<>();
                if (controller != null) {
                    ActiveEntityEffect[] activeEffects = controller.getAllActiveEntityEffects();
                    if (activeEffects != null) {
                        for (ActiveEntityEffect activeEffect : activeEffects) {
                            if (activeEffect == null) continue;
                            int index = activeEffect.getEntityEffectIndex();
                            EntityEffect nativeEffect = 
                                EntityEffect.getAssetMap().getAsset(index);
                            if (nativeEffect != null && nativeEffect.getId() != null) {
                                String nativeId = nativeEffect.getId();
                                String assetName = nativeId.replace("runecore:", "");
                                String effectName = RuneCore.get().getEffectIdByAsset(assetName);
                                if (effectName == null) {
                                    effectName = toSnakeCase(assetName);
                                }

                                if (RuneCore.get().getEffect(effectName) != null) {
                                    int remainingTicks = (int) (activeEffect.getRemainingDuration() * 20.0f);
                                    if (remainingTicks > 0) {
                                        ActiveBuff buff = ActiveBuff.builder(playerRef.getUuid().toString(), effectName, remainingTicks).build();
                                        buffs.add(buff);
                                    }
                                }
                            }
                        }
                    }
                }
                hud.setBuffs(buffs);
            });
        }
    }

    private static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) return input;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0 && input.charAt(i - 1) != '_') sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
