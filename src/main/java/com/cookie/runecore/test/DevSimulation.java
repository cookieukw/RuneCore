package com.cookie.runecore.test;

import com.cookie.runecore.api.*;
import com.cookie.runecore.system.RuneCore;

public class DevSimulation {

    // Simulation
    public void simulateGameLoop() {
        RuneCore core = RuneCore.get();
        core.initDefaults(); // Make sure defaults are loaded
        
        System.out.println("--- Simulating Player casting Fireball ---");
        
        Object player = new Object();
        Object goblin = new Object();
        Object world = new Object();
        
        CastContext ctx = new CastContext(player, goblin, world, 1.0);

        core.castSpell("fireball", ctx);

        System.out.println("\n--- Simulating Player casting Toxic Bolt ---");
        core.castSpell("toxic_bolt", ctx);
    }
    
    public static void main(String[] args) {
        DevSimulation sim = new DevSimulation();
        sim.simulateGameLoop();
    }
}
