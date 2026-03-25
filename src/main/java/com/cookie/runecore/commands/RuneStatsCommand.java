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
import java.util.function.Consumer;

public class RuneStatsCommand extends AbstractCommand {

    private final RequiredArg<String> statArg;
    private final RequiredArg<String> actionArg;
    private final RequiredArg<String> valueArg;

    public RuneStatsCommand() {
        super("runestats", "View or modify RuneCore player stats.");
        this.statArg = this.withRequiredArg("stat", "health|mana|stamina|speed", ArgTypes.STRING);
        this.actionArg = this.withRequiredArg("action", "add|set|remove|get", ArgTypes.STRING);
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
            if (valueStr.equalsIgnoreCase("normal") || valueStr.equalsIgnoreCase("default")
                    || valueStr.equalsIgnoreCase("reset")) {
                isReset = true;
            } else {
                try {
                    value = Float.parseFloat(valueStr);
                } catch (NumberFormatException e) {
                    if (!action.equalsIgnoreCase("get")) {
                        ctx.sendMessage(Message.raw("Invalid number value: " + valueStr));
                        return CompletableFuture.completedFuture(null);
                    }
                }
            }
        } else if (!action.equalsIgnoreCase("get")) {
            ctx.sendMessage(Message.raw("You must provide a value for action: " + action));
            return CompletableFuture.completedFuture(null);
        }

        PlayerStats stats = new PlayerStats(playerRef);

        switch (stat.toLowerCase()) {
            case "health":
                handleStatAction(ctx, action, value, stats::addHealth, stats::setHealth, stats::subtractHealth,
                        stats.getHealth());
                break;
            case "mana":
                handleStatAction(ctx, action, value, stats::addMana, stats::setMana, stats::subtractMana,
                        stats.getMana());
                break;
            case "stamina":
                handleStatAction(ctx, action, value, stats::addStamina, stats::setStamina, stats::subtractStamina,
                        stats.getStamina());
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

    private void handleStatAction(CommandContext ctx, String action, float value,
            Consumer<Float> add,
            Consumer<Float> set,
            Consumer<Float> sub,
            CompletableFuture<Float> getter) {
        if (action.equalsIgnoreCase("add")) {
            add.accept(value);
            ctx.sendMessage(Message.raw("Added " + value + " to stat."));
        } else if (action.equalsIgnoreCase("set")) {
            set.accept(value);
            ctx.sendMessage(Message.raw("Set stat to " + value + "."));
        } else if (action.equalsIgnoreCase("remove")) {
            sub.accept(value);
            ctx.sendMessage(Message.raw("Removed " + value + " from stat."));
        } else if (action.equalsIgnoreCase("get")) {
            getter.thenAccept(current -> {
                ctx.sendMessage(Message.raw("Current value: " + current));
            });
        } else {
            ctx.sendMessage(Message.raw("Unknown or unsupported action: " + action));
        }
    }
}
