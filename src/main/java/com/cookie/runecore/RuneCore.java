package com.cookie.runecore;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.RuneStatsCommand;
import com.cookie.runecore.commands.TestUICommand;
import com.cookie.runecore.systems.MobDropSystem;
import com.cookie.runecore.systems.ui.ManaHudManager;
import com.cookie.runecore.commands.CustomTimeCommand;

import javax.annotation.Nonnull;

public class RuneCore extends JavaPlugin {

    public RuneCore(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new RuneStatsCommand());
        this.getCommandRegistry().registerCommand(new CustomTimeCommand());
        this.getCommandRegistry().registerCommand(new TestUICommand());
        this.getEntityStoreRegistry().registerSystem(new MobDropSystem());
        new ManaHudManager(this.getEventRegistry());
    }
}