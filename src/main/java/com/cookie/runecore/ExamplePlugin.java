package com.cookie.runecore;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.ExampleCommand;
import com.cookie.runecore.commands.TestStatsCommand;
import com.cookie.runecore.commands.CustomTimeCommand;
import com.cookie.runecore.systems.MobDropSystem;

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
         this.getEntityStoreRegistry().registerSystem(new MobDropSystem());
        
        // Debug API availability
        System.out.println("Debugging JavaPlugin API:");
        for (java.lang.reflect.Method m : this.getClass().getSuperclass().getMethods()) {
             if (m.getName().contains("get") || m.getName().contains("register")) {
                 System.out.println("Method: " + m.getName());
             }
        }
    }
}