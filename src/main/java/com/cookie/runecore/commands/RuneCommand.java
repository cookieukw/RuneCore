package com.cookie.runecore.commands;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.api.RuneEffect;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.Message;

import javax.annotation.Nonnull;

public class RuneCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> subCommandArg;
    private final RequiredArg<String> effectIdArg;
    private final OptionalArg<Integer> durationArg;

    public RuneCommand() {
        super("rune", "RuneCore administrative commands");
        this.subCommandArg = this.withRequiredArg("subcommand", "Subcommand (effect|stats)", ArgTypes.STRING);
        this.effectIdArg = this.withRequiredArg("id", "Effect identifier or Stat", ArgTypes.STRING);
        this.durationArg = this.withOptionalArg("duration", "Duration in ticks (for effects)", ArgTypes.INTEGER);
    }

    @Override
    protected void execute(
        @Nonnull CommandContext ctx,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> ref,
        @Nonnull PlayerRef playerRef,
        @Nonnull World world
    ) {
        String subCommand = ctx.get(this.subCommandArg);
        
        if ("effect".equalsIgnoreCase(subCommand)) {
            handleEffect(ctx, ref, playerRef, world);
        } else {
            ctx.sendMessage(Message.raw("Unknown subcommand: " + subCommand + ". Use 'effect'."));
        }
    }

    private void handleEffect(CommandContext ctx, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        String effectId = ctx.get(this.effectIdArg);
        Integer duration = ctx.get(this.durationArg);

        RuneEffect effect = RuneCore.get().getEffect(effectId);
        if (effect != null) {
            CastContext castCtx = new CastContext(playerRef, ref, world, 1.0);
            effect.execute(castCtx);
            
            String msg = "Effect '" + effectId + "' applied!";
            if (duration != null) {
                msg += " (Note: duration override not yet implemented in API, using default: " + effect.getDurationTicks() + " ticks)";
            }
            ctx.sendMessage(Message.raw(msg));
        } else {
            ctx.sendMessage(Message.raw("Effect not found: " + effectId));
        }
    }
}
