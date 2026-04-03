package com.cookie.runecore.systems;

import com.cookie.runecore.api.ActiveBuff;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side tick system that drives all timed status effects (buffs/debuffs).
 *
 * Usage:
 *   Call EffectTickSystem.getInstance().applyBuff(buff, entityRef) to register a buff.
 *   Call tick() from a recurring world.execute() loop or from a registered ECS system.
 */
public class EffectTickSystem {

    private static final EffectTickSystem INSTANCE = new EffectTickSystem();

    // Key: "entityUUID|effectId" -> active buff
    private final Map<String, ActiveBuff> activeBuffs = new ConcurrentHashMap<>();
    // Key: "entityUUID|effectId" -> entity ref (kept alive while the effect is active)
    private final Map<String, Ref<EntityStore>> entityRefs = new ConcurrentHashMap<>();

    private EffectTickSystem() {}

    public static EffectTickSystem getInstance() {
        return INSTANCE;
    }

    /**
     * Register a new buff. If the same entity already has that effectId active,
     * the old one is removed (onExpire NOT called) and replaced.
     */
    public void applyBuff(ActiveBuff buff, Ref<EntityStore> ref) {
        String key = buffKey(buff.playerId, buff.effectId);
        activeBuffs.put(key, buff);
        entityRefs.put(key, ref);
        System.out.println("[RuneCore-Effects] Buff applied: " + buff.effectId +
                " on " + buff.playerId + " for " + buff.remainingTicks + " ticks");
    }

    /**
     * Cancel and expire a buff early without calling onExpire.
     */
    public void cancelBuff(String playerId, String effectId) {
        String key = buffKey(playerId, effectId);
        activeBuffs.remove(key);
        entityRefs.remove(key);
    }

    /**
     * Cancel all buffs on a player (e.g., on death or logout).
     */
    public void cancelAllBuffs(String playerId) {
        activeBuffs.entrySet().removeIf(e -> e.getKey().startsWith(playerId + "|"));
        entityRefs.entrySet().removeIf(e -> e.getKey().startsWith(playerId + "|"));
    }

    public boolean hasBuff(String playerId, String effectId) {
        return activeBuffs.containsKey(buffKey(playerId, effectId));
    }

    /**
     * Called every game tick. Advances all active buffs by 1 tick.
     * Should be called from within a world.execute() block for thread safety.
     */
    public void tick() {
        Iterator<Map.Entry<String, ActiveBuff>> it = activeBuffs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ActiveBuff> entry = it.next();
            String key = entry.getKey();
            ActiveBuff buff = entry.getValue();
            Ref<EntityStore> ref = entityRefs.get(key);

            if (ref == null || !ref.isValid()) {
                it.remove();
                entityRefs.remove(key);
                continue;
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
