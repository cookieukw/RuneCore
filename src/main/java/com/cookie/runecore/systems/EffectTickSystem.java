package com.cookie.runecore.systems;

import com.cookie.runecore.api.ActiveBuff;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EffectTickSystem {

    private static final EffectTickSystem INSTANCE = new EffectTickSystem();

    private final Map<String, ActiveBuff> activeBuffs = new ConcurrentHashMap<>();
    private final Map<String, Ref<EntityStore>> entityRefs = new ConcurrentHashMap<>();

    private EffectTickSystem() {}

    public static EffectTickSystem getInstance() {
        return INSTANCE;
    }

    public void applyBuff(ActiveBuff buff, Ref<EntityStore> ref) {
        String key = buffKey(buff.playerId, buff.effectId);
        activeBuffs.put(key, buff);
        if (ref != null) {
            entityRefs.put(key, ref);
        }
        System.out.println("[RuneCore-Effects] Buff applied: " + buff.effectId +
                " on " + buff.playerId + " for " + buff.remainingTicks + " ticks");
    }

    public void cancelBuff(String playerId, String effectId) {
        String key = buffKey(playerId, effectId);
        activeBuffs.remove(key);
        entityRefs.remove(key);
    }

    public void cancelAllBuffs(String playerId) {
        activeBuffs.entrySet().removeIf(e -> e.getKey().startsWith(playerId + "|"));
        entityRefs.entrySet().removeIf(e -> e.getKey().startsWith(playerId + "|"));
    }

    public boolean hasBuff(String playerId, String effectId) {
        return activeBuffs.containsKey(buffKey(playerId, effectId));
    }

    public ActiveBuff getBuff(String playerId, String effectId) {
        return activeBuffs.get(buffKey(playerId, effectId));
    }

    public void tick(com.hypixel.hytale.server.core.universe.world.World world) {
        Iterator<Map.Entry<String, ActiveBuff>> it = activeBuffs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ActiveBuff> entry = it.next();
            String key = entry.getKey();
            ActiveBuff buff = entry.getValue();
            Ref<EntityStore> ref = entityRefs.get(key);

            if (ref == null) {
                if (world != null) {
                    it.remove();
                    entityRefs.remove(key);
                    continue;
                }
            } else if (!ref.isValid()) {
                it.remove();
                entityRefs.remove(key);
                continue;
            }

            if (world != null && ref != null) {
                com.hypixel.hytale.server.core.universe.world.World refWorld = ref.getStore().getExternalData().getWorld();
                if (refWorld == null || !refWorld.equals(world)) {
                    continue;
                }
            }

            boolean alive = buff.tick(ref);
            if (!alive) {
                it.remove();
                entityRefs.remove(key);
                System.out.println("[RuneCore-Effects] Buff expired: " + buff.effectId + " on " + buff.playerId);
            }
        }
    }

    private String buffKey(String playerId, String effectId) {
        return playerId + "|" + effectId;
    }
}
