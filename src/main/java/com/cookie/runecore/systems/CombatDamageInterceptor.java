package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.api.attribute.AttributeContainer;
import com.cookie.runecore.api.combat.DamageContext;
import com.cookie.runecore.api.combat.DamageKind;
import com.cookie.runecore.api.combat.DamagePipeline;
import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
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

    private static final Set<String> MAGIC_CAUSES = Set.of(
            "Elemental", "Fire", "Ice", "Poison", "Magic"
    );

    private static final Set<String> PHYSICAL_CAUSES = Set.of(
            "Physical", "Projectile", "Bludgeoning", "Slashing"
    );

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
        CreatureCombatData creatureData = getCreatureData(source);

        if (DEBUG) LOG.info("[DMG] Hit on player " + targetUuid + " | raw=" + damage.getAmount() + " | cause=" + causeId);

        DamageContext ctx = context(source, defenderStats.attributes(), chunk.getReferenceTo(index),
                classify(causeId, cause, creatureData), damage.getAmount());

        // Stages registered before MITIGATION adjust the engine's raw amount.
        float working = DamagePipeline.runBefore(ctx, damage.getAmount(), DamagePipeline.MITIGATION);

        float core;

        if (ctx.kind().bypassesDefenses()) {
            // True damage: only the shield absorbs.
            core = defenderStats.absorbDamage(working);
            if (DEBUG) LOG.info("[DMG] True damage, shield only");
        } else if (source instanceof Damage.EntitySource entitySource
                && attackerStatsOf(entitySource, manager) != null) {
            // PvP: the amount comes from the attacker's stats plus the weapon in hand, which is
            // why `working` is discarded here — this model replaces the engine's number rather
            // than scaling it.
            CombatStats attackerStats = attackerStatsOf(entitySource, manager);
            CombatStats.Offense weapon = weaponOffense(entitySource);
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

    /** @return the attacker's stats when the source is a player RuneCore tracks, else null. */
    private CombatStats attackerStatsOf(Damage.EntitySource entitySource, CombatStatsManager manager) {
        UUID sourceUuid = getPlayerUuid(entitySource);
        return (sourceUuid != null && manager.hasStats(sourceUuid)) ? manager.getStats(sourceUuid) : null;
    }

    /** Classifies a hit once, replacing the scattered cause-string comparisons. */
    private DamageKind classify(String causeId, DamageCause cause, CreatureCombatData creature) {
        if (causeId.equals("True") || (cause != null && cause.doesBypassResistances())) {
            return DamageKind.TRUE;
        }
        if (creature != null) {
            return switch (creature.profile) {
                case PHYSICAL -> DamageKind.PHYSICAL;
                case MAGIC -> DamageKind.MAGIC;
                case HYBRID -> DamageKind.HYBRID;
                case TRUE -> DamageKind.TRUE;
            };
        }
        if (isMagicCause(causeId, cause)) return DamageKind.MAGIC;
        if (isPhysicalCause(causeId)) return DamageKind.PHYSICAL;
        return DamageKind.UNTYPED;
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
                UUID uuid = getPlayerUuid(entitySource);
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
        CreatureCombatData defender = lookupCreature(model);
        if (defender == null) return;

        DamageCause cause = damage.getCause();
        String causeId = (cause != null && cause.getId() != null) ? cause.getId() : "";
        Damage.Source source = damage.getSource();

        CombatStats defenderStats = creatureAsDefender(defender);
        DamageKind kind = classify(causeId, cause, null);
        DamageContext ctx = context(source, defenderStats.attributes(), chunk.getReferenceTo(index),
                kind, damage.getAmount());

        float working = DamagePipeline.runBefore(ctx, damage.getAmount(), DamagePipeline.MITIGATION);
        float core;

        if (kind.bypassesDefenses()) {
            // True damage ignores creature defences entirely; creatures carry no shield.
            core = working;
        } else if (source instanceof Damage.EntitySource entitySource && isPlayerSource(entitySource)) {
            CombatStatsManager manager = CombatStatsManager.get();
            UUID attackerUuid = getPlayerUuid(entitySource);
            CombatStats attackerStats = (manager != null && attackerUuid != null)
                    ? manager.getStats(attackerUuid) : null;
            if (attackerStats == null) attackerStats = new CombatStats();

            core = defenderStats.calculateFinalDamage(attackerStats, weaponOffense(entitySource));
            if (DEBUG) LOG.info("[DMG] Player -> creature | armor=" + defender.armor
                    + " | mr=" + defender.magicResist + " | core=" + core);
        } else {
            // Creature or environment hitting a creature: no offensive stat block exists for
            // the attacker, so the engine's amount stays the base and only mitigation runs.
            CreatureCombatData attacker = getCreatureData(source);
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

    private boolean isPlayerSource(Damage.EntitySource entitySource) {
        return getPlayerUuid(entitySource) != null;
    }

    /** Registry key for a creature: the model asset's file name, without path or namespace. */
    private CreatureCombatData lookupCreature(ModelComponent model) {
        CreatureCombatRegistry registry = CreatureCombatRegistry.get();
        if (registry == null || model == null || model.getModel() == null) return null;
        String assetId = model.getModel().getModelAssetId();
        if (assetId == null) return null;
        String name = assetId.contains("/") ? assetId.substring(assetId.lastIndexOf('/') + 1) : assetId;
        if (name.contains(":")) name = name.substring(name.indexOf(':') + 1);
        return registry.getData(name);
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

    /** Creature data for the <em>attacker</em> behind a damage source. */
    private CreatureCombatData getCreatureData(Damage.Source source) {
        if (!(source instanceof Damage.EntitySource entitySource)) return null;
        var ref = entitySource.getRef();
        if (ref == null || !ref.isValid()) return null;
        Store<EntityStore> s = ref.getStore();
        if (s == null) return null;
        ModelComponent model = s.getComponent(ref, ModelComponent.getComponentType());
        CreatureCombatData data = lookupCreature(model);
        if (DEBUG) LOG.info("[DMG] Creature lookup | found=" + (data != null));
        return data;
    }

    /** Offensive stats of the weapon the attacker is currently holding, or {@link CombatStats.Offense#NONE}. */
    private CombatStats.Offense weaponOffense(Damage.EntitySource entitySource) {
        CombatStatsRegistry registry = CombatStatsRegistry.get();
        if (registry == null) return CombatStats.Offense.NONE;

        var ref = entitySource.getRef();
        if (ref == null || !ref.isValid()) return CombatStats.Offense.NONE;

        ItemStack held = InventoryComponent.getItemInHand(ref.getStore(), ref);
        if (held == null || held.isEmpty()) return CombatStats.Offense.NONE;

        ItemCombatData data = registry.getItemData(held.getItemId());
        if (data == null) return CombatStats.Offense.NONE;

        return new CombatStats.Offense(data.physicalDamage, data.magicDamage, data.trueDamage,
                data.armorPenetration, data.magicPenetration);
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
