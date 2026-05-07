package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.cookie.runecore.api.ActiveBuff;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
    private List<ActiveBuff> activeBuffsList = new ArrayList<>();

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

        // Status Effects List
        builder.append("StatusEffects.ui");
        for (int i = 0; i < 8; i++) {
            String prefix = "#Effect" + (i + 1);
            if (i < activeBuffsList.size()) {
                ActiveBuff buff = activeBuffsList.get(i);
                builder.set(prefix + ".Visible", true);
                builder.set(prefix + " #" + getIconName(buff.effectId) + "Icon.Visible", true);
                builder.set(prefix + " #Time.Text", formatTicksToTime(buff.remainingTicks));
            } else {
                builder.set(prefix + ".Visible", false);
            }
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
        if (this.isFrozen == frozen)
            return;
        this.isFrozen = frozen;
        refreshHud();
    }

    public void setBleeding(boolean value) {
        if (this.isBleeding == value)
            return;
        this.isBleeding = value;
        refreshHud();
    }

    public void setBurning(boolean value) {
        if (this.isBurning == value)
            return;
        this.isBurning = value;
        refreshHud();
    }

    public void setMiningFatigue(boolean value) {
        if (this.isMiningFatigue == value)
            return;
        this.isMiningFatigue = value;
        refreshHud();
    }

    public void setHaste(boolean value) {
        if (this.isHaste == value)
            return;
        this.isHaste = value;
        refreshHud();
    }

    public void setNausea(boolean value) {
        if (this.isNausea == value)
            return;
        this.isNausea = value;
        refreshHud();
    }

    public void setBlinded(boolean value) {
        if (this.isBlinded == value)
            return;
        this.isBlinded = value;
        refreshHud();
    }

    public void setNightVision(boolean value) {
        if (this.isNightVision == value)
            return;
        this.isNightVision = value;
        refreshHud();
    }

    public void setGlowing(boolean value) {
        if (this.isGlowing == value)
            return;
        this.isGlowing = value;
        refreshHud();
    }

    public void setBuffs(List<ActiveBuff> buffs) {
        // Only refresh if the list changed meaningfully (e.g. size or first elements)
        // For simplicity, we'll just check size and IDs for now
        boolean changed = buffs.size() != this.activeBuffsList.size();
        if (!changed) {
            for (int i = 0; i < buffs.size(); i++) {
                if (!buffs.get(i).effectId.equals(this.activeBuffsList.get(i).effectId)) {
                    changed = true;
                    break;
                }
            }
        }

        this.activeBuffsList = new ArrayList<>(buffs);

        // Always update timers, but only full refresh (update(true, ...)) if layout
        // changed
        if (changed) {
            refreshHud();
        } else {
            UICommandBuilder b = new UICommandBuilder();
            for (int i = 0; i < Math.min(activeBuffsList.size(), 8); i++) {
                ActiveBuff buff = activeBuffsList.get(i);
                b.set("#Effect" + (i + 1) + " #Time.Text", formatTicksToTime(buff.remainingTicks));
            }
            this.update(false, b);
        }
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

    private String formatTicksToTime(int ticks) {
        int seconds = ticks / 20;
        if (seconds < 60)
            return seconds + "s";
        int minutes = seconds / 60;
        return minutes + "m";
    }

    private String getIconName(String id) {
        // Handle special cases
        if (id.equalsIgnoreCase("night_vision"))
            return "NightVision";
        if (id.equalsIgnoreCase("jump_boost"))
            return "JumpBoost";
        if (id.equalsIgnoreCase("high_jump"))
            return "HighJump";
        if (id.equalsIgnoreCase("slow_falling"))
            return "SlowFalling";
        if (id.equalsIgnoreCase("mining_fatigue"))
            return "MiningFatigue";
        if (id.equalsIgnoreCase("water_breathing"))
            return "WaterBreathing";
        if (id.equalsIgnoreCase("fire_resistance"))
            return "FireResistance";

        // Default: Capitalize first letter (e.g. speed -> Speed)
        if (id.length() > 0) {
            return id.substring(0, 1).toUpperCase() + id.substring(1).toLowerCase();
        }
        return id;
    }
}
