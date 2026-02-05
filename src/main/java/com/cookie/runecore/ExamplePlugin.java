package com.cookie.runecore;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.ExampleCommand;
import com.cookie.runecore.commands.TestStatsCommand;
import com.cookie.runecore.commands.CustomTimeCommand;

import javax.annotation.Nonnull;

public class ExamplePlugin extends JavaPlugin {

    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getCommandRegistry().registerCommand(new TestStatsCommand());
        this.getCommandRegistry().registerCommand(new CustomTimeCommand());
        System.out.println("ExamplePlugin enabled and scheduler started.");
    }
}