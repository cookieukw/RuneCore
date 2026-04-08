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
    private boolean isBleeding = false;
    private boolean isBurning = false;
    private boolean isMiningFatigue = false;
    private boolean isHaste = false;

    public RuneCoreHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    protected void build(@Nonnull UICommandBuilder builder) {
        builder.append("MANA/Mana.ui");
        builder.set("#ManaBar.Value", this.manaValue);
        builder.set("#ManaText.Text", getFormattedManaText());

        if (isFrozen) {
            builder.append("Custom/Effects/FrozenOverlay.ui");
        }
        if (isBleeding) {
            builder.append("Custom/Effects/BleedingOverlay.ui");
        }
        if (isBurning) {
            builder.append("Custom/Effects/BurnOverlay.ui");
        }
        if (isMiningFatigue) {
            builder.append("Custom/Effects/MiningFatigueOverlay.ui");
        }
        if (isHaste) {
            builder.append("Custom/Effects/HasteOverlay.ui");
        }
    }

    public void setMana(float current, float max) {
        this.targetManaValue = Math.max(0, Math.min(1.0f, current / max));
        this.maxManaValue = max;
        
        // Simple Lerp
        this.manaValue = this.manaValue + (this.targetManaValue - this.manaValue) * 0.5f;
        
        UICommandBuilder b = new UICommandBuilder();
        b.set("#ManaBar.Value", this.manaValue);
        b.set("#ManaText.Text", getFormattedManaText());
        this.update(false, b); 
    }

    public void setFrozen(boolean frozen) {
        if (this.isFrozen == frozen) return;
        this.isFrozen = frozen;
        refreshHud();
    }

    public void setBleeding(boolean value) {
        if (this.isBleeding == value) return;
        this.isBleeding = value;
        refreshHud();
    }

    public void setBurning(boolean value) {
        if (this.isBurning == value) return;
        this.isBurning = value;
        refreshHud();
    }

    public void setMiningFatigue(boolean value) {
        if (this.isMiningFatigue == value) return;
        this.isMiningFatigue = value;
        refreshHud();
    }

    public void setHaste(boolean value) {
        if (this.isHaste == value) return;
        this.isHaste = value;
        refreshHud();
    }

    private void refreshHud() {
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
