package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.api.attribute.AttributeContainer;
import com.cookie.runecore.api.combat.DamageContext;
import com.cookie.runecore.api.combat.DamageKind;
import com.cookie.runecore.api.combat.DamagePipeline;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData;
import com.cookie.runecore.systems.combat.CombatParticipants;
import com.cookie.runecore.systems.combat.DamageClassifier;
import com.cookie.runecore.systems.CreatureCombatRegistry.DamageProfile;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;

import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class CombatDamageInterceptor extends DamageEventSystem {

    private static final Logger LOG = Logger.getLogger("RuneCore|Combat");

    /**
     * Per-hit combat tracing. Was hardcoded {@code true}, so a shipped build logged up to five
     * INFO lines for every single damage event of every entity — console flood plus the string
     * concatenation cost on the hot path. Enable with {@code -Drunecore.combat.debug=true}.
     */
    private static final boolean DEBUG = Boolean.getBoolean("runecore.combat.debug");



    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return RootDependency.firstSet();
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        // Was Player only, so combat stats applied exclusively to damage *received by players*.
        // Creatures are matched through ModelComponent, which is also how they are identified
        // in CreatureCombatRegistry. Unregistered creatures are ignored further down, so the
        // effective blast radius stays limited to what CreatureCombatDefaults declares.
        return Query.or(
                Player.getComponentType(),
                ModelComponent.getComponentType()
        );
    }

    @Override
    public void handle(int index, @Nonnull ArchetypeChunk<EntityStore> chunk,
                       @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer,
                       @Nonnull Damage damage) {

        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null) return;

        PlayerRef targetPr = chunk.getComponent(index, PlayerRef.getComponentType());
        if (targetPr == null) {
            handleCreatureDefender(index, chunk, damage);
            return;
        }
        UUID targetUuid = targetPr.getUuid();
        if (targetUuid == null || !manager.hasStats(targetUuid)) return;

        CombatStats defenderStats = manager.getStats(targetUuid);
        DamageCause cause = damage.getCause();
        String causeId = (cause != null && cause.getId() != null) ? cause.getId() : "";
        Damage.Source source = damage.getSource();
        CreatureCombatData creatureData = CombatParticipants.creatureBehind(source);

        if (DEBUG) LOG.info("[DMG] Hit on player " + targetUuid + " | raw=" + damage.getAmount() + " | cause=" + causeId);

        DamageContext ctx = context(source, defenderStats.attributes(), chunk.getReferenceTo(index),
                DamageClassifier.classify(causeId, cause, creatureData), damage.getAmount());

        // Stages registered before MITIGATION adjust the engine's raw amount.
        float working = DamagePipeline.runBefore(ctx, damage.getAmount(), DamagePipeline.MITIGATION);

        float core;

        if (ctx.kind().bypassesDefenses()) {
            // True damage: only the shield absorbs.
            core = defenderStats.absorbDamage(working);
            if (DEBUG) LOG.info("[DMG] True damage, shield only");
        } else if (source instanceof Damage.EntitySource entitySource
                && CombatParticipants.attackerStats(entitySource, manager) != null) {
            // PvP: the amount comes from the attacker's stats plus the weapon in hand, which is
            // why `working` is discarded here — this model replaces the engine's number rather
            // than scaling it.
            CombatStats attackerStats = CombatParticipants.attackerStats(entitySource, manager);
            CombatStats.Offense weapon = CombatParticipants.weaponOffense(entitySource);
            core = defenderStats.calculateFinalDamage(attackerStats, weapon);
            if (DEBUG) LOG.info("[DMG] PvP | weaponPhys=" + weapon.physical() + " | core=" + core);
        } else {
            // PvE: creature or environment hitting a player. Mitigation only.
            float reduced;
            if (creatureData != null) {
                reduced = applyCreatureDamage(working, defenderStats, creatureData);
            } else if (ctx.kind() == DamageKind.MAGIC) {
                reduced = CombatStats.calcReducedDamage(working, defenderStats.getMagicResist(), 0);
            } else if (ctx.kind() == DamageKind.PHYSICAL) {
                reduced = CombatStats.calcReducedDamage(working, defenderStats.getArmor(), 0);
            } else {
                reduced = working;
            }
            reduced *= (1f - defenderStats.getDamageReduction());
            core = defenderStats.absorbDamage(reduced);
            if (DEBUG) LOG.info("[DMG] PvE | kind=" + ctx.kind() + " | raw=" + working + " | core=" + core);
        }

        // Stages at MITIGATION or later see the resolved amount: crits, lifesteal, and anything
        // else driven by an attribute RuneCore does not know about.
        damage.setAmount(DamagePipeline.runFrom(ctx, core, DamagePipeline.MITIGATION));
    }



    /** Builds the context stages receive. Attacker attributes are empty for environmental damage. */
    private DamageContext context(Damage.Source source, AttributeContainer defenderAttributes,
                                  Ref<EntityStore> defenderRef, DamageKind kind, float raw) {
        AttributeContainer attackerAttributes = new AttributeContainer();
        Ref<EntityStore> attackerRef = null;

        if (source instanceof Damage.EntitySource entitySource) {
            attackerRef = entitySource.getRef();
            CombatStatsManager manager = CombatStatsManager.get();
            if (manager != null) {
                UUID uuid = CombatParticipants.playerUuid(entitySource);
                if (uuid != null && manager.hasStats(uuid)) {
                    attackerAttributes = manager.getStats(uuid).attributes();
                }
            }
        }

        return new DamageContext(attackerAttributes, defenderAttributes, attackerRef, defenderRef, kind, raw);
    }

    /**
     * Damage taken by a creature. Mirrors the player path: when the attacker is a player, the
     * final number comes from the attacker's CombatStats plus the held weapon, resolved against
     * the creature's own armour/resist — the same symmetry PvP already had.
     * <p>
     * Creatures absent from {@link CreatureCombatRegistry} are left completely alone, so this
     * never touches damage the mod has no opinion about.
     */
    private void handleCreatureDefender(int index, ArchetypeChunk<EntityStore> chunk, Damage damage) {
        ModelComponent model = chunk.getComponent(index, ModelComponent.getComponentType());
        CreatureCombatData defender = CombatParticipants.creatureFor(model);
        if (defender == null) return;

        DamageCause cause = damage.getCause();
        String causeId = (cause != null && cause.getId() != null) ? cause.getId() : "";
        Damage.Source source = damage.getSource();

        CombatStats defenderStats = creatureAsDefender(defender);
        DamageKind kind = DamageClassifier.classify(causeId, cause, null);
        DamageContext ctx = context(source, defenderStats.attributes(), chunk.getReferenceTo(index),
                kind, damage.getAmount());

        float working = DamagePipeline.runBefore(ctx, damage.getAmount(), DamagePipeline.MITIGATION);
        float core;

        if (kind.bypassesDefenses()) {
            // True damage ignores creature defences entirely; creatures carry no shield.
            core = working;
        } else if (source instanceof Damage.EntitySource entitySource && CombatParticipants.playerUuid(entitySource) != null) {
            CombatStatsManager manager = CombatStatsManager.get();
            UUID attackerUuid = CombatParticipants.playerUuid(entitySource);
            CombatStats attackerStats = (manager != null && attackerUuid != null)
                    ? manager.getStats(attackerUuid) : null;
            if (attackerStats == null) attackerStats = new CombatStats();

            core = defenderStats.calculateFinalDamage(attackerStats, CombatParticipants.weaponOffense(entitySource));
            if (DEBUG) LOG.info("[DMG] Player -> creature | armor=" + defender.armor
                    + " | mr=" + defender.magicResist + " | core=" + core);
        } else {
            // Creature or environment hitting a creature: no offensive stat block exists for
            // the attacker, so the engine's amount stays the base and only mitigation runs.
            CreatureCombatData attacker = CombatParticipants.creatureBehind(source);
            if (attacker != null) {
                core = applyCreatureDamage(working, defenderStats, attacker);
            } else if (kind == DamageKind.MAGIC) {
                core = CombatStats.calcReducedDamage(working, defenderStats.getMagicResist(), 0);
            } else if (kind == DamageKind.PHYSICAL) {
                core = CombatStats.calcReducedDamage(working, defenderStats.getArmor(), 0);
            } else {
                core = working;
            }
            core *= (1f - defenderStats.getDamageReduction());
            if (DEBUG) LOG.info("[DMG] Non-player -> creature | raw=" + working + " | core=" + core);
        }

        damage.setAmount(DamagePipeline.runFrom(ctx, core, DamagePipeline.MITIGATION));
    }

    /** Builds a throwaway CombatStats carrying only this creature's defences. */
    private CombatStats creatureAsDefender(CreatureCombatData data) {
        CombatStats stats = new CombatStats();
        stats.setArmor(data.armor);
        stats.setMagicResist(data.magicResist);
        stats.setDamageReduction(data.damageReduction);
        return stats;
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



}
