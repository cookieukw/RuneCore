package com.cookie.runecore.content;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class CoreEssences {
    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEssence(new Essence("Essence_Fire", RuneElement.FIRE, 1));
        core.registerEssence(new Essence("Essence_Earth", RuneElement.EARTH, 1));
        core.registerEssence(new Essence("Essence_Wind", RuneElement.WIND, 1));
        core.registerEssence(new Essence("Essence_Water", RuneElement.WATER, 1));
        core.registerEssence(new Essence("Essence_Ice", RuneElement.ICE, 1));
        core.registerEssence(new Essence("Essence_Lightning", RuneElement.LIGHTNING, 1));
        core.registerEssence(new Essence("Essence_Light", RuneElement.LIGHT, 1));
        core.registerEssence(new Essence("Essence_Shadow", RuneElement.SHADOW, 1));
        core.registerEssence(new Essence("Essence_Life", RuneElement.LIFE, 1));
        core.registerEssence(new Essence("Essence_Death", RuneElement.DEATH, 1));
        core.registerEssence(new Essence("Essence_Mind", RuneElement.MIND, 1));
        core.registerEssence(new Essence("Essence_Blood", RuneElement.BLOOD, 1));
        core.registerEssence(new Essence("Essence_Chaos", RuneElement.CHAOS, 1));
        core.registerEssence(new Essence("Essence_Aether", RuneElement.AETHER, 1));
        core.registerEssence(new Essence("Essence_Void", RuneElement.VOID, 1));
        core.registerEssence(new Essence("Essence_Time", RuneElement.TIME, 1));
        core.registerEssence(new Essence("Essence_Metal", RuneElement.METAL, 1));
        core.registerEssence(new Essence("Essence_Crystal", RuneElement.CRYSTAL, 1));
        core.registerEssence(new Essence("Essence_Poison", RuneElement.POISON, 1));
        core.registerEssence(new Essence("Essence_Acid", RuneElement.ACID, 1));
    }
}
