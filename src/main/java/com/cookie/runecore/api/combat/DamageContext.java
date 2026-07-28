package com.cookie.runecore.api.combat;

import com.cookie.runecore.api.attribute.AttributeContainer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

/**
 * Everything a {@link DamageStage} needs to decide how to alter one hit.
 * <p>
 * Both sides are exposed as {@link AttributeContainer}, so a stage can read attributes RuneCore
 * has never heard of — that is the point of the extensible model.
 *
 * @param attacker      attacker attributes; never null, but may be empty for environmental damage
 * @param defender      defender attributes
 * @param attackerRef   attacker entity, null for environmental damage
 * @param defenderRef   defender entity
 * @param kind          how the damage was classified
 * @param rawDamage     the amount before any stage ran, kept for stages that scale off it
 */
public record DamageContext(
        AttributeContainer attacker,
        AttributeContainer defender,
        @Nullable Ref<EntityStore> attackerRef,
        Ref<EntityStore> defenderRef,
        DamageKind kind,
        float rawDamage
) {

    /** True when a player is on the giving end. */
    public boolean hasAttacker() {
        return attackerRef != null;
    }
}
