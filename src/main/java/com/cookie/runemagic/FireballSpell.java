package com.cookie.runemagic;

import com.cookie.runecore.api.RuneElement;

public class FireballSpell extends CombatSpell {
    public FireballSpell() {
        super("fireball", (ctx) -> true);
        this.setDamage(15.0f);
        this.setRange(20.0f);
        this.setProjectileId("hytale:fireball");
        this.setAreaEffect(true, 3.0f);
        this.addCost("mana", 20);
        this.addEssenceRequirement(RuneElement.FIRE, 1);
        this.addEffect("damage_fire_instant");
        this.addEffect("status_burning");
    }
}
