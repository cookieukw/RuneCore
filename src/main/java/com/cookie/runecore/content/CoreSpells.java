package com.cookie.runecore.content;

import com.cookie.runecore.api.RuneElement;
import com.cookie.runecore.api.Spell;
import com.cookie.runecore.system.RuneCore;
import com.cookie.runemagic.FireballSpell;

public class CoreSpells {
    public static void init() {
        RuneCore core = RuneCore.get();

        // "Fireball": Specialized CombatSpell
        FireballSpell fireball = new FireballSpell();

        // "Toxic Bolt": Costs 15 mana, requires 1 poison essence, applies poison.
        Spell toxicBolt = new Spell("toxic_bolt", (ctx) -> true)
                .addCost("arcane_mana", 15)
                .addEssenceRequirement(RuneElement.POISON, 1)
                .addEffect("poison");

        core.registerSpell(fireball);
        core.registerSpell(toxicBolt);
    }
}
