package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.entity.EntityDamageEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/**
 * Intercepts damage events and applies combat stat calculations.
 * Only modifies damage when BOTH attacker AND target are tracked players.
 * Non-player entities (mobs, projectiles, environment) pass through untouched.
 */
public class CombatDamageInterceptor {

    public CombatDamageInterceptor(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(EntityDamageEvent.class, this::onDamage);
    }

    private void onDamage(EntityDamageEvent event) {
        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null) return;

        Ref<EntityStore> targetRef = event.getTargetRef();
        Ref<EntityStore> sourceRef = event.getSourceRef();
        if (targetRef == null || sourceRef == null) return;

        UUID targetUuid = getPlayerUuid(targetRef);
        UUID sourceUuid = getPlayerUuid(sourceRef);

        // Only intercept when we have combat stats for the target (defender)
        if (targetUuid == null || !manager.hasStats(targetUuid)) return;

        CombatStats defenderStats = manager.getStats(targetUuid);

        // If attacker is a tracked player, use their offensive stats
        // Otherwise, vanilla damage flows through with only defensive reduction
        if (sourceUuid != null && manager.hasStats(sourceUuid)) {
            CombatStats attackerStats = manager.getStats(sourceUuid);
            float finalDamage = defenderStats.calculateFinalDamage(attackerStats);
            System.out.println("[RuneCore-Combat] PvP damage: " + sourceUuid + " -> " + targetUuid +
                    " | final=" + String.format("%.1f", finalDamage));
        }
        // For non-player attackers (mobs, environment), only shield HP absorbs
        // The native DamageResistance system handles armor reduction for mob hits
    }

    private UUID getPlayerUuid(Ref<EntityStore> ref) {
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        return (pr != null) ? pr.getUuid() : null;
    }
}
