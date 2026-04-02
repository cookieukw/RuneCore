package com.cookie.runemagic;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.api.Spell;
import java.util.function.Predicate;

public class CombatSpell extends Spell {
    private float damage;
    private float range;
    private String projectileId;
    private boolean isAreaEffect;
    private float areaRadius;

    public CombatSpell(String id, Predicate<CastContext> conditions) {
        super(id, conditions);
        this.damage = 10.0f;
        this.range = 20.0f;
    }

    public CombatSpell setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public CombatSpell setRange(float range) {
        this.range = range;
        return this;
    }

    public CombatSpell setProjectileId(String projectileId) {
        this.projectileId = projectileId;
        return this;
    }

    public CombatSpell setAreaEffect(boolean areaEffect, float radius) {
        this.isAreaEffect = areaEffect;
        this.areaRadius = radius;
        return this;
    }

    public float getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }

    public String getProjectileId() {
        return projectileId;
    }

    public boolean isAreaEffect() {
        return isAreaEffect;
    }

    public float getAreaRadius() {
        return areaRadius;
    }
}
