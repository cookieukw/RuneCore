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
    private boolean isNausea = false;
    private boolean isBlinded = false;
    private boolean isNightVision = false;
    private boolean isGlowing = false;

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
        if (isBleeding) {
            builder.append("Effects/BleedingOverlay.ui");
        }
        if (isBurning) {
            builder.append("Effects/BurnOverlay.ui");
        }
        if (isMiningFatigue) {
            builder.append("Effects/MiningFatigueOverlay.ui");
        }
        if (isHaste) {
            builder.append("Effects/HasteOverlay.ui");
        }
        if (isNausea) {
            builder.append("Effects/NauseaOverlay.ui");
        }
        if (isBlinded) {
            builder.append("Effects/BlindnessOverlay.ui");
        }
        if (isNightVision) {
            builder.append("Effects/NightVisionOverlay.ui");
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

    public void setNausea(boolean value) {
        if (this.isNausea == value) return;
        this.isNausea = value;
        refreshHud();
    }

    public void setBlinded(boolean value) {
        if (this.isBlinded == value) return;
        this.isBlinded = value;
        refreshHud();
    }

    public void setNightVision(boolean value) {
        if (this.isNightVision == value) return;
        this.isNightVision = value;
        refreshHud();
    }

    public void setGlowing(boolean value) {
        if (this.isGlowing == value) return;
        this.isGlowing = value;
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
