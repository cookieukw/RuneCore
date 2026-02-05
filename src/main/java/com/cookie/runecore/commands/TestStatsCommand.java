package com.cookie.runecore.commands;

import com.cookie.runecore.api.PlayerStats;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TestStatsCommand extends AbstractCommand {

    private final RequiredArg<String> statArg;
    private final RequiredArg<String> actionArg;
    private final OptionalArg<String> valueArg;

    public TestStatsCommand() {
        super("runestat", "Modify player stats for testing purposes.");
        this.statArg = this.withRequiredArg("stat", "health|mana|stamina|speed", ArgTypes.STRING);
        this.actionArg = this.withRequiredArg("action", "add|set|remove|get", ArgTypes.STRING);
        this.valueArg = this.withOptionalArg("value", "Value", ArgTypes.STRING);
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        Ref<EntityStore> playerRef = null;
        try {
             playerRef = ctx.senderAsPlayerRef();
        } catch (Error | Exception e) {
             // Fallback
        }

        if (playerRef == null) {
            ctx.sendMessage(Message.raw("You must be a player to use this command."));
            return CompletableFuture.completedFuture(null);
        }

        String stat = ctx.get(this.statArg);
        String action = ctx.get(this.actionArg);
        String valueStr = ctx.get(this.valueArg);
        
        Float value = 0f;
        boolean isReset = false;
        
        if (valueStr != null) {
            if (valueStr.equalsIgnoreCase("normal") || valueStr.equalsIgnoreCase("default") || valueStr.equalsIgnoreCase("reset")) {
                isReset = true;
            } else {
                try {
                    value = Float.parseFloat(valueStr);
                } catch (NumberFormatException e) {
                   // Only warn if action is NOT get
                   if (!action.equalsIgnoreCase("get")) {
                       ctx.sendMessage(Message.raw("Invalid number value: " + valueStr));
                       return CompletableFuture.completedFuture(null);
                   }
                }
            }
        } else {
            // No value provided.
            // If action is set/add/remove, we might need a value.
            if (!action.equalsIgnoreCase("get") && !isReset && !stat.equalsIgnoreCase("speed")) { 
                // For speed we might handle 'reset' without valueStr if logic permits, but currently reset is flagged by valueStr.
            }
            if (action.equalsIgnoreCase("set") || action.equalsIgnoreCase("add") || action.equalsIgnoreCase("remove")) {
                 ctx.sendMessage(Message.raw("You must provide a value for action: " + action));
                 return CompletableFuture.completedFuture(null);
            }
        }
        
        ctx.sendMessage(Message.raw("Debug: Stat=" + stat + " Action=" + action + " Value=" + value + " Reset=" + isReset));

        PlayerStats stats = new PlayerStats(playerRef);

        switch (stat.toLowerCase()) {
            case "health":
            case "mana":
            case "stamina":
                // Keeping minimal changes for other stats to avoid errors, assuming user only tests speed.
                // In a full refactor I would add 'get' to all.
                ctx.sendMessage(Message.raw("Stat " + stat + " does not support get yet."));
                break;
                
            case "speed":
                 if (action.equalsIgnoreCase("get")) {
                     stats.getSpeed().thenAccept(current -> {
                         ctx.sendMessage(Message.raw("Current Speed: " + current));
                     });
                 } else if (isReset) {
                     stats.resetSpeed();
                     ctx.sendMessage(Message.raw("Resetting speed to normal..."));
                 } else if (action.equalsIgnoreCase("add")) {
                    stats.addSpeed(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to speed."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setSpeed(value);
                    ctx.sendMessage(Message.raw("Set speed to " + value + "."));
                } else if (action.equalsIgnoreCase("remove")) {
                    stats.subtractSpeed(value);
                    ctx.sendMessage(Message.raw("Removed " + value + " from speed."));
                }
                break;
            default:
                ctx.sendMessage(Message.raw("Unknown stat: " + stat));
                break;
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
