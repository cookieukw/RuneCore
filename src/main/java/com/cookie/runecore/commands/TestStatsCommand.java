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
    private final RequiredArg<Float> valueArg;

    public TestStatsCommand() {
        super("runestat", "Modify player stats for testing purposes.");
        this.statArg = this.withRequiredArg("stat", "health|mana|stamina", ArgTypes.STRING);
        this.actionArg = this.withRequiredArg("action", "add|set", ArgTypes.STRING);
        this.valueArg = this.withRequiredArg("value", "Value", ArgTypes.FLOAT);
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
        Float value = ctx.get(this.valueArg);

        PlayerStats stats = new PlayerStats(playerRef);

        switch (stat.toLowerCase()) {
            case "health":
                if (action.equalsIgnoreCase("add")) {
                    stats.addHealth(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to health."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setHealth(value);
                    ctx.sendMessage(Message.raw("Set health to " + value + "."));
                }
                break;
            case "mana":
                if (action.equalsIgnoreCase("add")) {
                    stats.addMana(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to mana."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setMana(value);
                    ctx.sendMessage(Message.raw("Set mana to " + value + "."));
                }
                break;
            case "stamina":
                 if (action.equalsIgnoreCase("add")) {
                    stats.addStamina(value);
                    ctx.sendMessage(Message.raw("Added " + value + " to stamina."));
                } else if (action.equalsIgnoreCase("set")) {
                    stats.setStamina(value);
                    ctx.sendMessage(Message.raw("Set stamina to " + value + "."));
                }
                break;
            default:
                ctx.sendMessage(Message.raw("Unknown stat: " + stat));
                break;
        }
        
        return CompletableFuture.completedFuture(null);
    }
}
