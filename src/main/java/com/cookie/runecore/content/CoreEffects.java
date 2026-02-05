package com.cookie.runecore.content;

import com.cookie.runecore.api.RuneEffect;
import com.cookie.runecore.system.RuneCore;

public class CoreEffects {
    public static void init() {
        RuneCore core = RuneCore.get();

        // Instant Fire Damage
        RuneEffect fireDamage = new RuneEffect("damage_fire_instant", true, 0, (ctx) -> {
            // Real game logic: ctx.target.damage(10 * ctx.power)
            System.out.println(">>> APPLYING DAMAGE: Target burned for " + (10 * ctx.power) + " damage!");
        });
        
        // Burning Status (DoT)
        RuneEffect applyBurning = new RuneEffect("status_burning", false, 100, (ctx) -> {
            System.out.println(">>> STATUS: Target is burning for 5 seconds.");
        });

        // Poison Status (DoT)
        RuneEffect applyPoison = new RuneEffect("status_poison", false, 200, (ctx) -> {
           System.out.println(">>> STATUS: Target is poisoned for 10 seconds. Taking periodic damage.");
        });

        core.registerEffect(fireDamage);
        core.registerEffect(applyBurning);
        core.registerEffect(applyPoison);
    }
}
