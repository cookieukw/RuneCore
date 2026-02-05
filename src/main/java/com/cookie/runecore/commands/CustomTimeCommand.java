package com.cookie.runecore.commands;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class CustomTimeCommand extends AbstractWorldCommand {
    private final RequiredArg<String> actionArg;
    private final RequiredArg<String> valueArg;

    public CustomTimeCommand() {
        super("time", "Modify the world time.");
        this.actionArg = this.withRequiredArg("action", "set|add|query", ArgTypes.STRING);
        this.valueArg = this.withRequiredArg("value", "day|night|noon|midnight|<number>", ArgTypes.STRING);
    }

    @Override
    protected void execute(@Nonnull CommandContext ctx, @Nonnull World world, @Nonnull Store<EntityStore> store) {
        String action = ctx.get(this.actionArg);
        String valueStr = ctx.get(this.valueArg);

        WorldTimeResource timeResource = (WorldTimeResource) store.getResource(WorldTimeResource.getResourceType());
        if (timeResource == null) {
            ctx.sendMessage(Message.raw("World time resource not found."));
            return;
        }

        if (action.equalsIgnoreCase("set")) {
            double time = 0;
            switch (valueStr.toLowerCase()) {
                case "day":
                    time = 10.0; // Custom requested value
                    break;
                case "night":
                    time = 20.0; // Standard night roughly
                    break;
                case "noon":
                    time = 12.0;
                    break;
                case "midnight":
                    time = 0.0;
                    break;
                case "dawn":
                case "sunrise":
                    time = 6.0;
                    break;
                case "dusk":
                case "sunset":
                    time = 18.0;
                    break;
                default:
                    try {
                        time = Double.parseDouble(valueStr);
                    } catch (NumberFormatException e) {
                        ctx.sendMessage(Message.raw("Invalid time value: " + valueStr));
                        return;
                    }
                    break;
            }
            
            // Normalize time from hours (0-24) to day progress (0.0 - 1.0)
            double normalizedTime = (time / 24.0) % 1.0;
            if (normalizedTime < 0) normalizedTime += 1.0;
            
            timeResource.setDayTime(normalizedTime, world, store);
            ctx.sendMessage(Message.raw("Set time to " + time + " (" + String.format("%.2f", normalizedTime) + ")"));
            
        } else if (action.equalsIgnoreCase("add")) {
            try {
                double amount = Double.parseDouble(valueStr);
                // Convert amount (hours) to progress
                double amountProgress = amount / 24.0;
                double current = timeResource.getDayProgress();
                
                double newTime = (current + amountProgress) % 1.0;
                if (newTime < 0) newTime += 1.0;

                timeResource.setDayTime(newTime, world, store);
                 ctx.sendMessage(Message.raw("Added " + amount + " hours to time."));
            } catch (NumberFormatException e) {
                 ctx.sendMessage(Message.raw("Invalid number for add: " + valueStr));
            }
        } else {
             ctx.sendMessage(Message.raw("Unknown action: " + action));
        }
    }
}
