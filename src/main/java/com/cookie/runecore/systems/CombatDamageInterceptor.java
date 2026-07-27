package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class CombatDamageInterceptor extends DamageEventSystem {

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Override
    public void handle(int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
                       @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer,
                       @Nonnull Damage damage) {

        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null) return;

        PlayerRef targetPr = chunk.getComponent(index, PlayerRef.getComponentType());
        if (targetPr == null) return;
        UUID targetUuid = targetPr.getUuid();
        if (targetUuid == null || !manager.hasStats(targetUuid)) return;

        CombatStats defenderStats = manager.getStats(targetUuid);

        if (damage.getCause() != null && damage.getCause().getId() != null
                && damage.getCause().getId().equals("True")) {
            float afterShield = defenderStats.absorbDamage(damage.getAmount());
            damage.setAmount(afterShield);
            return;
        }

        Damage.Source source = damage.getSource();
        if (source instanceof Damage.EntitySource entitySource) {
            UUID sourceUuid = getPlayerUuid(entitySource);
            if (sourceUuid != null && manager.hasStats(sourceUuid)) {
                CombatStats attackerStats = manager.getStats(sourceUuid);
                float finalDamage = defenderStats.calculateFinalDamage(attackerStats);
                damage.setAmount(finalDamage);
                return;
            }
        }

        float raw = damage.getAmount();
        float afterReduction = raw * (1f - defenderStats.getDamageReduction());
        float afterShield = defenderStats.absorbDamage(afterReduction);
        damage.setAmount(afterShield);
    }

    private UUID getPlayerUuid(Damage.EntitySource entitySource) {
        var ref = entitySource.getRef();
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> store = ref.getStore();
        if (store == null) return null;
        PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        return (pr != null) ? pr.getUuid() : null;
    }
}
