package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import com.cookie.runecore.systems.CreatureCombatRegistry.DamageProfile;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class CombatDamageInterceptor extends DamageEventSystem {

    private static final Set<String> MAGIC_CAUSES = Set.of(
            "Elemental", "Fire", "Ice", "Poison", "Magic"
    );

    private static final Set<String> PHYSICAL_CAUSES = Set.of(
            "Physical", "Projectile", "Bludgeoning", "Slashing"
    );

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
        DamageCause cause = damage.getCause();
        String causeId = (cause != null && cause.getId() != null) ? cause.getId() : "";

        // True damage: only shield absorbs
        if (causeId.equals("True") || (cause != null && cause.doesBypassResistances())) {
            damage.setAmount(defenderStats.absorbDamage(damage.getAmount()));
            return;
        }

        // PvP: full combat stats calculation
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

        // PvE: creature/environment -> player
        float raw = damage.getAmount();
        float reduced;

        CreatureCombatData creatureData = getCreatureData(source);
        if (creatureData != null) {
            reduced = applyCreatureDamage(raw, defenderStats, creatureData);
        } else if (isMagicCause(causeId, cause)) {
            reduced = CombatStats.calcReducedDamage(raw, defenderStats.getMagicResist(), 0);
        } else if (isPhysicalCause(causeId)) {
            reduced = CombatStats.calcReducedDamage(raw, defenderStats.getArmor(), 0);
        } else {
            reduced = raw;
        }

        reduced *= (1f - defenderStats.getDamageReduction());
        damage.setAmount(defenderStats.absorbDamage(reduced));
    }

    private boolean isMagicCause(String causeId, DamageCause cause) {
        if (MAGIC_CAUSES.contains(causeId)) return true;
        if (cause == null) return false;
        String inherits = cause.getInherits();
        return inherits != null && MAGIC_CAUSES.contains(inherits);
    }

    private boolean isPhysicalCause(String causeId) {
        return PHYSICAL_CAUSES.contains(causeId);
    }

    private float applyCreatureDamage(float raw, CombatStats defender, CreatureCombatData creature) {
        return switch (creature.profile) {
            case PHYSICAL -> CombatStats.calcReducedDamage(raw, defender.getArmor(), creature.armorPenetration);
            case MAGIC -> CombatStats.calcReducedDamage(raw, defender.getMagicResist(), creature.magicPenetration);
            case HYBRID -> {
                float physPortion = raw * (1f - creature.magicRatio);
                float magicPortion = raw * creature.magicRatio;
                float physReduced = CombatStats.calcReducedDamage(physPortion, defender.getArmor(), creature.armorPenetration);
                float magicReduced = CombatStats.calcReducedDamage(magicPortion, defender.getMagicResist(), creature.magicPenetration);
                yield physReduced + magicReduced;
            }
            case TRUE -> raw;
        };
    }

    private CreatureCombatData getCreatureData(Damage.Source source) {
        if (!(source instanceof Damage.EntitySource entitySource)) return null;
        CreatureCombatRegistry registry = CreatureCombatRegistry.get();
        if (registry == null) return null;
        var ref = entitySource.getRef();
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> s = ref.getStore();
        if (s == null) return null;
        ModelComponent model = s.getComponent(ref, ModelComponent.getComponentType());
        if (model == null || model.getModel() == null) return null;
        String assetId = model.getModel().getModelAssetId();
        if (assetId == null) return null;
        String name = assetId.contains("/") ? assetId.substring(assetId.lastIndexOf('/') + 1) : assetId;
        if (name.contains(":")) name = name.substring(name.indexOf(':') + 1);
        return registry.getData(name);
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
