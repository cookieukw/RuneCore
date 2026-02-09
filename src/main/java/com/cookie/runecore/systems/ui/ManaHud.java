package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class ManaHud extends CustomUIHud {

    public ManaHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder builder) {
        try {
        // Load the UI layout using namespaced path
        builder.append("MANA/Mana.ui");
        
        // Initialize values
        //builder.set("#ManaText.Text", "Mana: 100/100");
     builder.set("#ManaBar.Value", 1.0f); // 0.0 to 1.0
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public void updateMana(int current, int max) {
         // This method would be called by the manager to send updates
         // dependent on how update() is exposed or if we re-send via builder in a refresh loop.
    }
}
