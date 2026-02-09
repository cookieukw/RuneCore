package com.cookie.runecore;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.TestStatsCommand;
import com.cookie.runecore.commands.TestUICommand;
import com.cookie.runecore.commands.CustomTimeCommand;
import com.cookie.runecore.systems.MobDropSystem;
import com.cookie.runecore.systems.ui.ManaHudManager;

import javax.annotation.Nonnull;

public class ExamplePlugin extends JavaPlugin {

    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new TestStatsCommand());
        this.getCommandRegistry().registerCommand(new CustomTimeCommand());
        this.getEntityStoreRegistry().registerSystem(new MobDropSystem());
        this.getCommandRegistry().registerCommand(new TestUICommand());
        new ManaHudManager(this.getEventRegistry());
        
        
    }
}