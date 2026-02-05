package com.cookie.runecore.content;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class CoreEssences {
    public static void init() {
        RuneCore core = RuneCore.get();

        // Register Fire Essence
        core.registerEssence(new Essence("essence_fire", RuneElement.FIRE, 1));
        
        // Register Poison Essence
        core.registerEssence(new Essence("essence_poison", RuneElement.POISON, 1));
    }
}
