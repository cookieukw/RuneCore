package com.cookie.runecore.content;

import com.cookie.runecore.api.RuneEffect;
import com.cookie.runecore.system.RuneCore;

public class CoreEffects {
    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEffect(new RuneEffect("haste", "Haste", 1200));
        core.registerEffect(new RuneEffect("strength", "Strength", 1200));
        core.registerEffect(new RuneEffect("jump_boost", "JumpBoost", 1200));
        core.registerEffect(new RuneEffect("regeneration", "Regeneration", 400));
        core.registerEffect(new RuneEffect("resistance", "Resistance", 1200));
        core.registerEffect(new RuneEffect("fire_resistance", "FireResistance", 1200));
        core.registerEffect(new RuneEffect("water_breathing", "WaterBreathing", 1200));
        core.registerEffect(new RuneEffect("night_vision", "NightVision", 1200));
        core.registerEffect(new RuneEffect("speed", "Speed", 1200));
        core.registerEffect(new RuneEffect("slow_falling", "SlowFalling", 1200));

        core.registerEffect(new RuneEffect("blindness", "Blindness", 200));
        core.registerEffect(new RuneEffect("nausea", "Nausea", 200));
        core.registerEffect(new RuneEffect("poison", "Poison", 400));
        core.registerEffect(new RuneEffect("wither", "Wither", 400));
        core.registerEffect(new RuneEffect("weakness", "Weakness", 600));
        core.registerEffect(new RuneEffect("slowness", "Freeze", 600));
        core.registerEffect(new RuneEffect("mining_fatigue", "MiningFatigue", 1200));
        core.registerEffect(new RuneEffect("darkness", "Darkness", 200));

        core.registerEffect(new RuneEffect("invisibility", "Invisibility", 1200));
        core.registerEffect(new RuneEffect("glowing", "Glowing", 1200));
        core.registerEffect(new RuneEffect("levitation", "Levitation", 200));

        RuneEffect fireDamage = new RuneEffect("damage_fire_instant", true, 0, (ctx) -> {
            System.out.println(">>> APPLYING DAMAGE: Target burned for " + (10 * ctx.power) + " damage!");
        });
        core.registerEffect(fireDamage);
    }
}
