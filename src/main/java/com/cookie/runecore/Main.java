package com.cookie.runecore;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.RuneStatsCommand;
import com.cookie.runecore.commands.TestUICommand;
import com.cookie.runecore.systems.CastListener;
import com.cookie.runecore.systems.EffectTimerListener;
import com.cookie.runecore.systems.MobDropSystem;
import com.cookie.runecore.systems.ui.ManaHudManager;
import com.cookie.runemagic.MagicListener;
import com.cookie.runemagic.SwitchSpellCommand;
import com.cookie.runecore.commands.CustomTimeCommand;
import com.cookie.runecore.commands.RuneCommand;
import com.cookie.runecore.api.PlayerDataComponent;
import com.cookie.runecore.system.RuneCore;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

    public Main(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Initialize RuneCore engine defaults
        RuneCore.get().initDefaults();

        this.getCommandRegistry().registerCommand(new RuneStatsCommand());
        this.getCommandRegistry().registerCommand(new CustomTimeCommand());
        this.getCommandRegistry().registerCommand(new TestUICommand());
        this.getCommandRegistry().registerCommand(new SwitchSpellCommand());
        this.getCommandRegistry().registerCommand(new RuneCommand());

        // Register custom player data component
        PlayerDataComponent.TYPE = this.getEntityStoreRegistry().registerComponent(
                PlayerDataComponent.class,
                PlayerDataComponent.COMPONENT_ID,
                PlayerDataComponent.CODEC);

        this.getEntityStoreRegistry().registerSystem(new MobDropSystem());
        new ManaHudManager(this.getEventRegistry());
        new CastListener(this.getEventRegistry());
        new EffectTimerListener(this.getEventRegistry());
        new MagicListener(this.getEventRegistry());
    }
}
