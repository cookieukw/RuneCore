package com.cookie.runecore.commands;

import com.cookie.runecore.api.PlayerStats;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TestStatsCommand extends AbstractCommand {

    private final RequiredArg<String> statArg;
    private final RequiredArg<String> actionArg;
    private final RequiredArg<String> valueArg;

    public TestStatsCommand() {
        super("runestat", "Modify player stats for testing purposes.");
        this.statArg = this.withRequiredArg("stat", "health|mana|stamina|speed", ArgTypes.STRING);
        this.actionArg = this.withRequiredArg("action", "add|set|remove", ArgTypes.STRING);
        this.valueArg = this.withRequiredArg("value", "Value", ArgTypes.STRING);
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
                    ctx.sendMessage(Message.raw("Invalid number value: " + valueStr));
                    return CompletableFuture.completedFuture(null);
                }
            }
        }

        PlayerStats stats = new PlayerStats(playerRef);

        switch (stat.toLowerCase()) {
            case "health":
                if (action.equalsIgnoreCase("add")) {
                    stats.addHealth(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to health."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setHealth(value);
                    ctx.sendMessage(Message.raw("Set health to " + value + "."));
                } else if (action.equalsIgnoreCase("remove")) {
                    stats.subtractHealth(value);
                    ctx.sendMessage(Message.raw("Removed " + value + " from health."));
                }
                break;
            case "mana":
                if (action.equalsIgnoreCase("add")) {
                    stats.addMana(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to mana."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setMana(value);
                    ctx.sendMessage(Message.raw("Set mana to " + value + "."));
                } else if (action.equalsIgnoreCase("remove")) {
                    stats.subtractMana(value);
                    ctx.sendMessage(Message.raw("Removed " + value + " from mana."));
                }
                break;
            case "stamina":
                 if (action.equalsIgnoreCase("add")) {
                    stats.addStamina(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to stamina."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setStamina(value);
                    ctx.sendMessage(Message.raw("Set stamina to " + value + "."));
                } else if (action.equalsIgnoreCase("remove")) {
                    stats.subtractStamina(value);
                    ctx.sendMessage(Message.raw("Removed " + value + " from stamina."));
                }
                break;
            case "speed":
                 if (isReset) {
                     stats.resetSpeed();
                     ctx.sendMessage(Message.raw("Reset speed to normal."));
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
