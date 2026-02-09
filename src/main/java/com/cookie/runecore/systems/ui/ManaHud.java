package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class ManaHud extends CustomUIHud {

    private float manaValue = 1.0f; // This is the displayed value
    private float targetManaValue = 1.0f; // This is the actual value we want to reach

    public ManaHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder builder) {
        try {
            // Load the UI layout using namespaced path
            builder.append("MANA/Mana.ui");
            
            // Set the mana bar value based on the current state
            builder.set("#ManaBar.Value", this.manaValue);
            System.out.println("[RuneCore-HUD] Building mana hud with value: " + this.manaValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateMana(float current, float max) {
        this.targetManaValue = Math.max(0, Math.min(1.0f, current / max));
        
        // If the displayed value is different from the target, move it closer
        if (Math.abs(this.manaValue - this.targetManaValue) > 0.001f) {
            
            // "Perco 1 por 1" - Let's use a step of 0.05 (5% of bar) per tick for speed and smoothness
            float step = 0.05f; 
            
            if (this.manaValue < this.targetManaValue) {
                this.manaValue = Math.min(this.targetManaValue, this.manaValue + step);
            } else {
                this.manaValue = Math.max(this.targetManaValue, this.manaValue - step);
            }

            // System.out.println("[RuneCore-HUD] Animating mana: " + this.manaValue + " -> " + this.targetManaValue);
            
            UICommandBuilder builder = new UICommandBuilder();
            builder.set("#ManaBar.Value", this.manaValue);
            // Use false for reset to update the existing UI without rebuilding the whole layout
            this.update(false, builder); 
        }
    }
}
