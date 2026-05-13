package com.cookie.runecore.content;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class CoreEssences {
    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEssence(new Essence("Ingredient_Fire_Essence", RuneElement.FIRE, 1));
        core.registerEssence(new Essence("Ingredient_Earth_Essence", RuneElement.EARTH, 1));
        core.registerEssence(new Essence("Ingredient_Wind_Essence", RuneElement.WIND, 1));
        core.registerEssence(new Essence("Ingredient_Water_Essence", RuneElement.WATER, 1));
        core.registerEssence(new Essence("Ingredient_Ice_Essence", RuneElement.ICE, 1));
        core.registerEssence(new Essence("Ingredient_Lightning_Essence", RuneElement.LIGHTNING, 1));
        core.registerEssence(new Essence("Ingredient_Light_Essence", RuneElement.LIGHT, 1));
        core.registerEssence(new Essence("Ingredient_Shadow_Essence", RuneElement.SHADOW, 1));
        core.registerEssence(new Essence("Ingredient_Life_Essence", RuneElement.LIFE, 1));
        core.registerEssence(new Essence("Ingredient_Death_Essence", RuneElement.DEATH, 1));
        core.registerEssence(new Essence("Ingredient_Mind_Essence", RuneElement.MIND, 1));
        core.registerEssence(new Essence("Ingredient_Blood_Essence", RuneElement.BLOOD, 1));
        core.registerEssence(new Essence("Ingredient_Chaos_Essence", RuneElement.CHAOS, 1));
        core.registerEssence(new Essence("Ingredient_Aether_Essence", RuneElement.AETHER, 1));
        core.registerEssence(new Essence("Ingredient_Void_Essence", RuneElement.VOID, 1));
        core.registerEssence(new Essence("Ingredient_Time_Essence", RuneElement.TIME, 1));
        core.registerEssence(new Essence("Ingredient_Metal_Essence", RuneElement.METAL, 1));
        core.registerEssence(new Essence("Ingredient_Crystal_Essence", RuneElement.CRYSTAL, 1));
        core.registerEssence(new Essence("Ingredient_Poison_Essence", RuneElement.POISON, 1));
        core.registerEssence(new Essence("Ingredient_Acid_Essence", RuneElement.ACID, 1));
    }
}
