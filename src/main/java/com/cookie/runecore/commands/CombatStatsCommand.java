package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsManager;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * {@code /combatstats <view|set|add|reset> [stat] [value]}.
 * <p>
 * Argument wiring and dispatch only: the stat vocabulary lives in {@link CombatStatOption} and
 * every message in {@link CombatStatsView}. The three handlers below used to repeat the same
 * nine-stat alias table in three parallel switch statements.
 */
public class CombatStatsCommand extends AbstractCommand {

    private static final String SHIELD = "shield";

    private final RequiredArg<String> actionArg;
    private final OptionalArg<String> statArg;
    private final OptionalArg<String> valueArg;

    public CombatStatsCommand() {
        super("combatstats", "View or modify combat stats. Usage: /combatstats <view|set|add|reset> [stat] [value]");
        this.actionArg = this.withRequiredArg("action", "view|set|add|reset", ArgTypes.STRING);
        this.statArg = this.withOptionalArg("stat",
                "armor|magicresist|reduction|physdmg|magdmg|truedmg|armorpen|magicpen|shield",
                ArgTypes.STRING);
        this.valueArg = this.withOptionalArg("value", "Numeric value", ArgTypes.STRING);
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) {
            CombatStatsView.notPlayer(ctx);
            return CompletableFuture.completedFuture(null);
        }

        UUID uuid = ctx.sender().getUuid();
        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null || uuid == null) {
            CombatStatsView.notAvailable(ctx);
            return CompletableFuture.completedFuture(null);
        }

        CombatStats stats = manager.getOrCreate(uuid);

        switch (ctx.get(this.actionArg).toLowerCase(Locale.ROOT)) {
            case "view"  -> CombatStatsView.showStats(ctx, stats);
            case "set"   -> handleSet(ctx, stats);
            case "add"   -> handleAdd(ctx, stats);
            case "reset" -> handleReset(ctx, stats);
            default      -> CombatStatsView.usage(ctx);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void handleSet(CommandContext ctx, CombatStats stats) {
        String statName = statArg(ctx);
        Float value = valueArg(ctx);
        if (statName == null || value == null) return;

        if (SHIELD.equals(statName)) {
            stats.setShieldHP(value, value);
            CombatStatsView.shieldSet(ctx, value);
            return;
        }

        CombatStatOption option = CombatStatOption.from(statName);
        if (option == null) {
            CombatStatsView.unknownStat(ctx, statName);
            return;
        }
        option.set(stats, value);
        CombatStatsView.statSet(ctx, option.id, value);
    }

    private void handleAdd(CommandContext ctx, CombatStats stats) {
        String statName = statArg(ctx);
        Float value = valueArg(ctx);
        if (statName == null || value == null) return;

        if (SHIELD.equals(statName)) {
            float newMax = stats.getMaxShieldHP() + value;
            stats.setShieldHP(newMax, newMax);
            CombatStatsView.shieldAdded(ctx, value, newMax);
            return;
        }

        CombatStatOption option = CombatStatOption.from(statName);
        if (option == null) {
            CombatStatsView.unknownStat(ctx, statName);
            return;
        }

        String modifierId = "cmd_" + option.id + "_" + System.currentTimeMillis();
        stats.addModifier(modifierId, option.modifierKey, option.toStored(value));
        CombatStatsView.statAdded(ctx, option.id, value);
    }

    private void handleReset(CommandContext ctx, CombatStats stats) {
        String raw = ctx.get(this.statArg);

        if (raw == null || raw.equalsIgnoreCase("all")) {
            stats.reset();
            CombatStatsView.allReset(ctx);
            return;
        }

        String statName = raw.toLowerCase(Locale.ROOT);

        if ("modifiers".equals(statName)) {
            stats.clearModifiers();
            CombatStatsView.modifiersReset(ctx);
            return;
        }
        if (SHIELD.equals(statName)) {
            stats.setShieldHP(0, 0);
            CombatStatsView.statReset(ctx, statName);
            return;
        }

        CombatStatOption option = CombatStatOption.from(statName);
        if (option == null) {
            CombatStatsView.unknownStat(ctx, statName);
            return;
        }
        option.reset(stats);
        CombatStatsView.statReset(ctx, statName);
    }

    @Nullable
    private String statArg(CommandContext ctx) {
        String stat = ctx.get(this.statArg);
        if (stat == null) {
            CombatStatsView.missingStat(ctx);
            return null;
        }
        return stat.toLowerCase(Locale.ROOT);
    }

    @Nullable
    private Float valueArg(CommandContext ctx) {
        String raw = ctx.get(this.valueArg);
        if (raw == null) {
            CombatStatsView.missingValue(ctx);
            return null;
        }
        try {
            return Float.parseFloat(raw);
        } catch (NumberFormatException e) {
            CombatStatsView.invalidNumber(ctx, raw);
            return null;
        }
    }
}
