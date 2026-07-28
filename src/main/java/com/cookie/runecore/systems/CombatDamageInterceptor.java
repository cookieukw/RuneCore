package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
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

        if (DEBUG) LOG.info("[DMG] Hit on player " + targetUuid + " | raw=" + damage.getAmount() + " | cause=" + causeId);

        // True damage: only shield absorbs
        if (causeId.equals("True") || (cause != null && cause.doesBypassResistances())) {
            if (DEBUG) LOG.info("[DMG] True damage, shield only");
            damage.setAmount(defenderStats.absorbDamage(damage.getAmount()));
            return;
        }

        // PvP: full combat stats calculation
        Damage.Source source = damage.getSource();
        if (source instanceof Damage.EntitySource entitySource) {
            UUID sourceUuid = getPlayerUuid(entitySource);
            if (sourceUuid != null && manager.hasStats(sourceUuid)) {
                CombatStats attackerStats = manager.getStats(sourceUuid);
                // EquipmentStatsListener only tracks the ARMOUR container, so the attacker's
                // CombatStats never carried weapon damage. calculateFinalDamage is driven purely
                // by attacker offence, which meant an unarmoured player dealt exactly 0 in PvP
                // no matter what sword they were holding. The held weapon is resolved here, at
                // hit time, so hotbar swaps are always reflected.
                CombatStats.Offense weapon = weaponOffense(entitySource);
                float finalDamage = defenderStats.calculateFinalDamage(attackerStats, weapon);
                if (DEBUG) LOG.info("[DMG] PvP | attacker=" + sourceUuid + " | weaponPhys=" + weapon.physical() + " | final=" + finalDamage);
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
            if (DEBUG) LOG.info("[DMG] PvE creature registry | profile=" + creatureData.profile + " | magicRatio=" + creatureData.magicRatio + " | armorPen=" + creatureData.armorPenetration + " | magicPen=" + creatureData.magicPenetration + " | raw=" + raw + " | reduced=" + reduced);
        } else if (isMagicCause(causeId, cause)) {
            reduced = CombatStats.calcReducedDamage(raw, defenderStats.getMagicResist(), 0);
            if (DEBUG) LOG.info("[DMG] PvE fallback MAGIC | cause=" + causeId + " | raw=" + raw + " | reduced=" + reduced);
        } else if (isPhysicalCause(causeId)) {
            reduced = CombatStats.calcReducedDamage(raw, defenderStats.getArmor(), 0);
            if (DEBUG) LOG.info("[DMG] PvE fallback PHYSICAL | cause=" + causeId + " | raw=" + raw + " | reduced=" + reduced);
        } else {
            reduced = raw;
            if (DEBUG) LOG.info("[DMG] PvE fallback UNTYPED | cause=" + causeId + " | raw=" + raw);
        }

        reduced *= (1f - defenderStats.getDamageReduction());
        float finalDmg = defenderStats.absorbDamage(reduced);
        if (DEBUG) LOG.info("[DMG] After DR(" + defenderStats.getDamageReduction() + ") + shield: " + finalDmg);
        damage.setAmount(finalDmg);
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
        float raw = damage.getAmount();

        // True damage ignores creature defences entirely; creatures carry no shield.
        if (causeId.equals("True") || (cause != null && cause.doesBypassResistances())) {
            if (DEBUG) LOG.info("[DMG] Creature hit, true damage, untouched | raw=" + raw);
            return;
        }

        CombatStats defenderStats = creatureAsDefender(defender);
        Damage.Source source = damage.getSource();
        float finalDmg;

        if (source instanceof Damage.EntitySource entitySource
                && isPlayerSource(entitySource)) {
            CombatStatsManager manager = CombatStatsManager.get();
            UUID attackerUuid = getPlayerUuid(entitySource);
            CombatStats attackerStats = (manager != null && attackerUuid != null)
                    ? manager.getStats(attackerUuid) : null;
            if (attackerStats == null) attackerStats = new CombatStats();

            finalDmg = defenderStats.calculateFinalDamage(attackerStats, weaponOffense(entitySource));
            if (DEBUG) LOG.info("[DMG] Player -> creature | attacker=" + attackerUuid
                    + " | armor=" + defender.armor + " | mr=" + defender.magicResist
                    + " | raw=" + raw + " | final=" + finalDmg);
        } else {
            // Creature or environment hitting a creature: no offensive stat block exists for
            // the attacker, so the engine's raw damage stays the base and only mitigation runs.
            CreatureCombatData attacker = getCreatureData(source);
            if (attacker != null) {
                finalDmg = applyCreatureDamage(raw, defenderStats, attacker);
            } else if (isMagicCause(causeId, cause)) {
                finalDmg = CombatStats.calcReducedDamage(raw, defenderStats.getMagicResist(), 0);
            } else if (isPhysicalCause(causeId)) {
                finalDmg = CombatStats.calcReducedDamage(raw, defenderStats.getArmor(), 0);
            } else {
                finalDmg = raw;
            }
            finalDmg *= (1f - defenderStats.getDamageReduction());
            if (DEBUG) LOG.info("[DMG] Non-player -> creature | raw=" + raw + " | final=" + finalDmg);
        }

        damage.setAmount(Math.max(0f, finalDmg));
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
