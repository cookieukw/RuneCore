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

public class RuneEffectCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> effectIdArg;
    private final OptionalArg<Double> powerArg;

    public RuneEffectCommand() {
        super("rune_effect", "Applies a rune effect to the player");
        this.effectIdArg = this.withRequiredArg("id", "Effect identifier", ArgTypes.STRING);
        this.powerArg = this.withOptionalArg("power", "Effect power", ArgTypes.DOUBLE);
    }

    @Override
    protected void execute(
        @Nonnull CommandContext ctx,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> ref,
        @Nonnull PlayerRef playerRef,
        @Nonnull World world
    ) {
        String effectId = ctx.get(this.effectIdArg);
        Double powerVal = ctx.get(this.powerArg);
        double power = (powerVal != null) ? powerVal : 1.0;

        CastContext castCtx = new CastContext(playerRef, ref, world, power);

        RuneEffect effect = RuneCore.get().getEffect(effectId);
        if (effect != null) {
            effect.execute(castCtx);
            ctx.sendMessage(Message.raw("Effect '" + effectId + "' applied!"));
        } else {
            ctx.sendMessage(Message.raw("Effect not found: " + effectId));
        }
    }
}
