package com.cookie.runecore.content;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class CoreEssences {
    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEssence(new Essence("essence_fire", RuneElement.FIRE, 1));
        core.registerEssence(new Essence("essence_earth", RuneElement.EARTH, 1));
        core.registerEssence(new Essence("essence_wind", RuneElement.WIND, 1));
        core.registerEssence(new Essence("essence_water", RuneElement.WATER, 1));
        core.registerEssence(new Essence("essence_ice", RuneElement.ICE, 1));
        core.registerEssence(new Essence("essence_lightning", RuneElement.LIGHTNING, 1));
        core.registerEssence(new Essence("essence_light", RuneElement.LIGHT, 1));
        core.registerEssence(new Essence("essence_shadow", RuneElement.SHADOW, 1));
        core.registerEssence(new Essence("essence_life", RuneElement.LIFE, 1));
        core.registerEssence(new Essence("essence_death", RuneElement.DEATH, 1));
        core.registerEssence(new Essence("essence_mind", RuneElement.MIND, 1));
        core.registerEssence(new Essence("essence_blood", RuneElement.BLOOD, 1));
        core.registerEssence(new Essence("essence_chaos", RuneElement.CHAOS, 1));
        core.registerEssence(new Essence("essence_aether", RuneElement.AETHER, 1));
        core.registerEssence(new Essence("essence_void", RuneElement.VOID, 1));
        core.registerEssence(new Essence("essence_time", RuneElement.TIME, 1));
        core.registerEssence(new Essence("essence_metal", RuneElement.METAL, 1));
        core.registerEssence(new Essence("essence_crystal", RuneElement.CRYSTAL, 1));
        core.registerEssence(new Essence("essence_poison", RuneElement.POISON, 1));
        core.registerEssence(new Essence("essence_acid", RuneElement.ACID, 1));
    }
}
