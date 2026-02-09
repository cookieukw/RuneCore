package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class ManaHud extends CustomUIHud {

    private float manaValue = 1.0f;

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
        float newValue = Math.max(0, Math.min(1.0f, current / max));
        
        // Only update if it actually changed significantly to avoid spam
        if (Math.abs(this.manaValue - newValue) > 0.0001f) {
            this.manaValue = newValue;
            System.out.println("[RuneCore-HUD] Mana update triggered: " + current + "/" + max + " (" + this.manaValue + ")");
            
            UICommandBuilder builder = new UICommandBuilder();
            builder.set("#ManaBar.Value", this.manaValue);
            // Use false for reset to update the existing UI without rebuilding the whole layout
            this.update(false, builder); 
        }
    }
}
