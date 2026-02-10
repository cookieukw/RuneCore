package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class ManaHud extends CustomUIHud {

    private float manaValue = 1.0f; // This is the displayed ratio (0-1)
    private float targetManaValue = 1.0f; 
    private float maxManaValue = 100.0f; 
UICommandBuilder builder = new UICommandBuilder();
    public ManaHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder builder) {
        try {
            // Load the UI layout using namespaced path
            builder.append("MANA/Mana.ui");
            
            // Set the mana bar value and label text based on current state
            builder.set("#ManaBar.Value", this.manaValue);
            builder.set("#ManaText.Text", getFormattedManaText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateMana(float current, float max) {
        this.targetManaValue = Math.max(0, Math.min(1.0f, current / max));
        this.maxManaValue = max;
        
        // Use Lerp for smooth animation
        if (Math.abs(this.manaValue - this.targetManaValue) > 0.0001f) {
            float lerpFactor = 0.5f; 
            this.manaValue = this.manaValue + (this.targetManaValue - this.manaValue) * lerpFactor;
        } else {
            // Snap to target to avoid floating point inaccuracies (e.g. showing 99/100)
            this.manaValue = this.targetManaValue;
        }

        
        builder.set("#ManaBar.Value", this.manaValue);
        builder.set("#ManaText.Text", getFormattedManaText());
        
        // Use false for reset to update the existing UI without rebuilding the whole layout
        this.update(false, builder); 
    }

    private String getFormattedManaText() {
        int current = Math.round(this.manaValue * this.maxManaValue);
        int max = Math.round(this.maxManaValue);
        return current + "/" + max;
    }
}
