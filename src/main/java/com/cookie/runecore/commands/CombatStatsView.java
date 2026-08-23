package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;

/**
 * Presentation for {@code /combatstats}: the stat sheet and every feedback message.
 * <p>
 * Split out of the command so that file is left with argument wiring and dispatch. Colours and
 * wording live here.
 */
final class CombatStatsView {

    private static final String LABEL   = "#AAAAAA";
    private static final String HEADING = "#FFD700";
    private static final String PHYS    = "#FF9966";
    private static final String MAGIC   = "#BB86FC";
    private static final String TRUE    = "#FFFFFF";
    private static final String PEN     = "#FF6E6E";
    private static final String ARMOR   = "#FFE066";
    private static final String RESIST  = "#66CCFF";
    private static final String DEFENSE = "#66FFAA";
    private static final String SHIELD  = "#66EEFF";
    private static final String MUTED   = "#CCCCCC";

    private CombatStatsView() {}

    static void showStats(CommandContext ctx, CombatStats s) {
        ctx.sendMessage(Message.raw("=== Combat Stats ===").color(HEADING).bold(true));

        ctx.sendMessage(Message.raw("--- Offense ---").color(PHYS).bold(true));
        ctx.sendMessage(stat("Physical Damage: ", fmt(s.getPhysicalDamage()), PHYS));
        ctx.sendMessage(stat("Magic Damage: ", fmt(s.getMagicDamage()), MAGIC));
        ctx.sendMessage(stat("True Damage: ", fmt(s.getTrueDamage()), TRUE));
        ctx.sendMessage(stat("Armor Penetration: ", fmt(s.getArmorPenetration()), PEN));
        ctx.sendMessage(stat("Magic Penetration: ", fmt(s.getMagicPenetration()), PEN));

        ctx.sendMessage(Message.raw("--- Defense ---").color(DEFENSE).bold(true));
        ctx.sendMessage(stat("Armor: ", fmt(s.getArmor()), ARMOR));
        ctx.sendMessage(stat("Magic Resist: ", fmt(s.getMagicResist()), RESIST));
        ctx.sendMessage(stat("Damage Reduction: ", fmt(s.getDamageReduction() * 100f) + "%", DEFENSE));
        ctx.sendMessage(stat("Shield: ", fmt(s.getShieldHP()) + " / " + fmt(s.getMaxShieldHP()), SHIELD));

        float physTaken = CombatStats.calcReducedDamage(100f, s.getArmor(), 0) * (1f - s.getDamageReduction());
        float magicTaken = CombatStats.calcReducedDamage(100f, s.getMagicResist(), 0) * (1f - s.getDamageReduction());
        ctx.sendMessage(Message.raw("--- Effective ---").color(MUTED).bold(true));
        ctx.sendMessage(stat("100 physical hit -> ", fmt(physTaken) + " taken", PHYS));
        ctx.sendMessage(stat("100 magic hit -> ", fmt(magicTaken) + " taken", MAGIC));
    }

    static void usage(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.usage"));
        ctx.sendMessage(Message.translation("runecore.combat.stat_list"));
    }

    static void statSet(CommandContext ctx, String stat, float value) {
        ctx.sendMessage(Message.translation("runecore.combat.set.stat")
                .param("stat", stat)
                .param("value", fmt(value)));
    }

    static void statAdded(CommandContext ctx, String stat, float value) {
        ctx.sendMessage(Message.translation("runecore.combat.add.stat")
                .param("value", fmt(value))
                .param("stat", stat));
    }

    static void shieldSet(CommandContext ctx, float value) {
        ctx.sendMessage(Message.translation("runecore.combat.set.shield")
                .param("current", fmt(value))
                .param("max", fmt(value)));
    }

    static void shieldAdded(CommandContext ctx, float value, float total) {
        ctx.sendMessage(Message.translation("runecore.combat.add.shield")
                .param("value", fmt(value))
                .param("total", fmt(total)));
    }

    static void statReset(CommandContext ctx, String stat) {
        ctx.sendMessage(Message.translation("runecore.combat.reset.stat").param("stat", stat));
    }

    static void allReset(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.reset.all"));
    }

    static void modifiersReset(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.reset.modifiers"));
    }

    static void unknownStat(CommandContext ctx, String stat) {
        ctx.sendMessage(Message.translation("runecore.combat.error.unknown_stat").param("stat", stat));
        ctx.sendMessage(Message.translation("runecore.combat.stat_list"));
    }

    static void missingStat(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.error.missing_stat"));
    }

    static void missingValue(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.error.missing_value"));
    }

    static void invalidNumber(CommandContext ctx, String value) {
        ctx.sendMessage(Message.translation("runecore.combat.error.invalid_number").param("value", value));
    }

    static void notPlayer(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.error.not_player"));
    }

    static void notAvailable(CommandContext ctx) {
        ctx.sendMessage(Message.translation("runecore.combat.error.not_available"));
    }

    private static Message stat(String label, String value, String color) {
        return Message.join(
                Message.raw(label).color(LABEL),
                Message.raw(value).color(color)
        );
    }

    private static String fmt(float v) {
        return String.format("%.1f", v);
    }
}
