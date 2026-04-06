package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class RuneCoreHud extends CustomUIHud {
    private float manaValue = 1.0f;
    private float targetManaValue = 1.0f;
    private float maxManaValue = 100.0f;
    private boolean isFrozen = false;

    public RuneCoreHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder builder) {
        builder.append("MANA/Mana.ui");
        builder.set("#ManaBar.Value", this.manaValue);
        builder.set("#ManaText.Text", getFormattedManaText());

        if (isFrozen) {
            builder.append("Effects/FrozenOverlay.ui");
        }
    }

    public void setMana(float current, float max) {
        this.targetManaValue = Math.max(0, Math.min(1.0f, current / max));
        this.maxManaValue = max;
        
        // Simple Lerp - could be improved with timing but fine for 500ms updates
        this.manaValue = this.manaValue + (this.targetManaValue - this.manaValue) * 0.5f;
        
        UICommandBuilder b = new UICommandBuilder();
        b.set("#ManaBar.Value", this.manaValue);
        b.set("#ManaText.Text", getFormattedManaText());
        this.update(false, b); 
    }

    public void setFrozen(boolean frozen) {
        if (this.isFrozen == frozen) return;
        this.isFrozen = frozen;
        
        UICommandBuilder b = new UICommandBuilder();
        this.build(b);
        this.update(true, b);
    }

    private String getFormattedManaText() {
        int current = Math.round(this.manaValue * this.maxManaValue);
        int max = Math.round(this.maxManaValue);
        return current + "/" + max;
    }
}
