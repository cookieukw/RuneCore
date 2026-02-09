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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateMana(float current, float max) {
        this.manaValue = current / max;
        // The HUD system will call build() again when we trigger an update
        this.update(true, (UICommandBuilder) null); 
    }
}
