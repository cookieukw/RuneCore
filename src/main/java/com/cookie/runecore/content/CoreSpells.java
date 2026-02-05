package com.cookie.runecore.content;

import com.cookie.runecore.api.RuneElement;
import com.cookie.runecore.api.Spell;
import com.cookie.runecore.system.RuneCore;

public class CoreSpells {
    public static void init() {
        RuneCore core = RuneCore.get();

        // "Fireball": Costs 20 mana, requires 1 fire essence, applies damage and burning.
        Spell fireball = new Spell("fireball", (ctx) -> ctx.target != null)
                .addCost("arcane_mana", 20)
                .addEssenceRequirement(RuneElement.FIRE, 1)
                .addEffect("damage_fire_instant")
                .addEffect("status_burning");

        // "Toxic Bolt": Costs 15 mana, requires 1 poison essence, applies poison.
        Spell toxicBolt = new Spell("toxic_bolt", (ctx) -> ctx.target != null)
                .addCost("arcane_mana", 15)
                .addEssenceRequirement(RuneElement.POISON, 1)
                .addEffect("status_poison");

        core.registerSpell(fireball);
        core.registerSpell(toxicBolt);
    }
}
