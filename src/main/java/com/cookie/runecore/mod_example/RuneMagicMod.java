package com.cookie.runecore.mod_example;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class RuneMagicMod {

    public void init() {
        RuneCore core = RuneCore.get();

        // 1. Register Resource
        GameResource mana = new GameResource("arcane_mana", 100.0, 5.0, "burnout");
        core.registerResource(mana);

        // 2. Register Fire Essence
        Essence fireEssence = new Essence("essence_fire", RuneElement.FIRE, 1);
        core.registerEssence(fireEssence);

        // 3. Create Effects
        // Instant Fire Damage
        RuneEffect fireDamage = new RuneEffect("damage_fire_instant", true, 0, (ctx) -> {
            // Real game logic: ctx.target.damage(10 * ctx.power)
            System.out.println(">>> APPLYING DAMAGE: Target burned for " + (10 * ctx.power) + " damage!");
        });
        
        // Burning Status (DoT)
        RuneEffect applyBurning = new RuneEffect("status_burning", false, 100, (ctx) -> {
            System.out.println(">>> STATUS: Target is burning for 5 seconds.");
        });

        core.registerEffect(fireDamage);
        core.registerEffect(applyBurning);

        // 4. Create Spell
        // "Fireball": Costs 20 mana, requires 1 fire essence, applies damage and burning.
        Spell fireball = new Spell("fireball", (ctx) -> ctx.target != null)
                .addCost("arcane_mana", 20)
                .addEssenceRequirement(RuneElement.FIRE, 1)
                .addEffect("damage_fire_instant")
                .addEffect("status_burning");

        core.registerSpell(fireball);

        // 5. Hook Example
        core.on("spell:pre_cast", (ctx) -> {
            // Example: Amplifying power if holding a specific item
            System.out.println("[Hook] Staff detected! Amplifying power...");
            ctx.power *= 1.5;
        });
    }

    // Simulation
    public void simulateGameLoop() {
        RuneCore core = RuneCore.get();
        
        System.out.println("--- Simulating Player casting Fireball ---");
        
        Object player = new Object();
        Object goblin = new Object();
        Object world = new Object();
        
        CastContext ctx = new CastContext(player, goblin, world, 1.0);

        core.castSpell("fireball", ctx);
    }
    
    public static void main(String[] args) {
        RuneMagicMod mod = new RuneMagicMod();
        mod.init();
        mod.simulateGameLoop();
    }
}