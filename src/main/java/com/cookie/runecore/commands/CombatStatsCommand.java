package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CombatStatsCommand extends AbstractCommand {

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
        Ref<EntityStore> playerRef;
        try {
            playerRef = ctx.senderAsPlayerRef();
        } catch (Error | Exception e) {
            ctx.sendMessage(Message.translation("runecore.combat.error.not_player"));
            return CompletableFuture.completedFuture(null);
        }
        if (playerRef == null || !playerRef.isValid()) {
            ctx.sendMessage(Message.translation("runecore.combat.error.not_player"));
            return CompletableFuture.completedFuture(null);
        }

        Store<EntityStore> store = playerRef.getStore();
        PlayerRef pr = (PlayerRef) store.getComponent(playerRef, PlayerRef.getComponentType());
        if (pr == null) {
            ctx.sendMessage(Message.translation("runecore.combat.error.resolve_player"));
            return CompletableFuture.completedFuture(null);
        }

        UUID uuid = pr.getUuid();
        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null || uuid == null) {
            ctx.sendMessage(Message.translation("runecore.combat.error.not_available"));
            return CompletableFuture.completedFuture(null);
        }

        CombatStats stats = manager.getOrCreate(uuid);
        String action = ctx.get(this.actionArg).toLowerCase();

        switch (action) {
            case "view":
                showStats(ctx, stats);
                break;
            case "set":
                handleSet(ctx, stats);
                break;
            case "add":
                handleAdd(ctx, stats);
                break;
            case "reset":
                handleReset(ctx, stats);
                break;
            default:
                ctx.sendMessage(Message.translation("runecore.combat.usage"));
                ctx.sendMessage(Message.translation("runecore.combat.stat_list"));
                break;
        }

        return CompletableFuture.completedFuture(null);
    }

    private void showStats(CommandContext ctx, CombatStats s) {
        ctx.sendMessage(Message.translation("runecore.combat.view.header"));
        ctx.sendMessage(Message.translation("runecore.combat.view.offense_header"));
        ctx.sendMessage(Message.translation("runecore.combat.view.phys_damage").param("value", fmt(s.getPhysicalDamage())));
        ctx.sendMessage(Message.translation("runecore.combat.view.magic_damage").param("value", fmt(s.getMagicDamage())));
        ctx.sendMessage(Message.translation("runecore.combat.view.true_damage").param("value", fmt(s.getTrueDamage())));
        ctx.sendMessage(Message.translation("runecore.combat.view.armor_pen").param("value", fmt(s.getArmorPenetration())));
        ctx.sendMessage(Message.translation("runecore.combat.view.magic_pen").param("value", fmt(s.getMagicPenetration())));
        ctx.sendMessage(Message.translation("runecore.combat.view.defense_header"));
        ctx.sendMessage(Message.translation("runecore.combat.view.armor").param("value", fmt(s.getArmor())));
        ctx.sendMessage(Message.translation("runecore.combat.view.magic_resist").param("value", fmt(s.getMagicResist())));
        ctx.sendMessage(Message.translation("runecore.combat.view.damage_reduction").param("value", fmt(s.getDamageReduction() * 100f)));
        ctx.sendMessage(Message.translation("runecore.combat.view.shield")
                .param("current", fmt(s.getShieldHP()))
                .param("max", fmt(s.getMaxShieldHP())));

        float testPhys = CombatStats.calcReducedDamage(100f, s.getArmor(), 0) * (1f - s.getDamageReduction());
        float testMag = CombatStats.calcReducedDamage(100f, s.getMagicResist(), 0) * (1f - s.getDamageReduction());
        ctx.sendMessage(Message.translation("runecore.combat.view.effective_header"));
        ctx.sendMessage(Message.translation("runecore.combat.view.effective_phys").param("value", fmt(testPhys)));
        ctx.sendMessage(Message.translation("runecore.combat.view.effective_mag").param("value", fmt(testMag)));
    }

    private void handleSet(CommandContext ctx, CombatStats stats) {
        String stat = getStatArg(ctx);
        Float value = getValueArg(ctx);
        if (stat == null || value == null) return;

        switch (stat) {
            case "armor":
                stats.setArmor(value);
                sendSetMsg(ctx, "armor", value);
                break;
            case "magicresist":
            case "mr":
                stats.setMagicResist(value);
                sendSetMsg(ctx, "magicresist", value);
                break;
            case "reduction":
            case "dr":
                stats.setDamageReduction(value / 100f);
                sendSetMsg(ctx, "reduction", value);
                break;
            case "physdmg":
            case "phys":
                stats.setPhysicalDamage(value);
                sendSetMsg(ctx, "physdmg", value);
                break;
            case "magdmg":
            case "mag":
                stats.setMagicDamage(value);
                sendSetMsg(ctx, "magdmg", value);
                break;
            case "truedmg":
            case "true":
                stats.setTrueDamage(value);
                sendSetMsg(ctx, "truedmg", value);
                break;
            case "armorpen":
            case "apen":
                stats.setArmorPenetration(value);
                sendSetMsg(ctx, "armorpen", value);
                break;
            case "magicpen":
            case "mpen":
                stats.setMagicPenetration(value);
                sendSetMsg(ctx, "magicpen", value);
                break;
            case "shield":
                stats.setShieldHP(value, value);
                ctx.sendMessage(Message.translation("runecore.combat.set.shield")
                        .param("current", fmt(value))
                        .param("max", fmt(value)));
                break;
            default:
                sendUnknownStat(ctx, stat);
                break;
        }
    }

    private void handleAdd(CommandContext ctx, CombatStats stats) {
        String stat = getStatArg(ctx);
        Float value = getValueArg(ctx);
        if (stat == null || value == null) return;

        String modId = "cmd_" + stat + "_" + System.currentTimeMillis();

        switch (stat) {
            case "armor":
                stats.addModifier(modId, "armor", value);
                sendAddMsg(ctx, "armor", value);
                break;
            case "magicresist":
            case "mr":
                stats.addModifier(modId, "magicResist", value);
                sendAddMsg(ctx, "magicresist", value);
                break;
            case "reduction":
            case "dr":
                stats.addModifier(modId, "damageReduction", value / 100f);
                sendAddMsg(ctx, "reduction", value);
                break;
            case "physdmg":
            case "phys":
                stats.addModifier(modId, "physicalDamage", value);
                sendAddMsg(ctx, "physdmg", value);
                break;
            case "magdmg":
            case "mag":
                stats.addModifier(modId, "magicDamage", value);
                sendAddMsg(ctx, "magdmg", value);
                break;
            case "truedmg":
            case "true":
                stats.addModifier(modId, "trueDamage", value);
                sendAddMsg(ctx, "truedmg", value);
                break;
            case "armorpen":
            case "apen":
                stats.addModifier(modId, "armorPenetration", value);
                sendAddMsg(ctx, "armorpen", value);
                break;
            case "magicpen":
            case "mpen":
                stats.addModifier(modId, "magicPenetration", value);
                sendAddMsg(ctx, "magicpen", value);
                break;
            case "shield":
                float newMax = stats.getMaxShieldHP() + value;
                stats.setShieldHP(newMax, newMax);
                ctx.sendMessage(Message.translation("runecore.combat.add.shield")
                        .param("value", fmt(value))
                        .param("total", fmt(newMax)));
                break;
            default:
                sendUnknownStat(ctx, stat);
                break;
        }
    }

    private void handleReset(CommandContext ctx, CombatStats stats) {
        String stat = ctx.get(this.statArg);

        if (stat == null || stat.equalsIgnoreCase("all")) {
            stats.reset();
            ctx.sendMessage(Message.translation("runecore.combat.reset.all"));
            return;
        }

        stat = stat.toLowerCase();
        switch (stat) {
            case "armor": stats.setArmor(0); break;
            case "magicresist": case "mr": stats.setMagicResist(0); break;
            case "reduction": case "dr": stats.setDamageReduction(0); break;
            case "physdmg": case "phys": stats.setPhysicalDamage(0); break;
            case "magdmg": case "mag": stats.setMagicDamage(0); break;
            case "truedmg": case "true": stats.setTrueDamage(0); break;
            case "armorpen": case "apen": stats.setArmorPenetration(0); break;
            case "magicpen": case "mpen": stats.setMagicPenetration(0); break;
            case "shield": stats.setShieldHP(0, 0); break;
            case "modifiers":
                stats.clearModifiers();
                ctx.sendMessage(Message.translation("runecore.combat.reset.modifiers"));
                return;
            default:
                sendUnknownStat(ctx, stat);
                return;
        }
        ctx.sendMessage(Message.translation("runecore.combat.reset.stat").param("stat", stat));
    }

    private void sendSetMsg(CommandContext ctx, String stat, float value) {
        ctx.sendMessage(Message.translation("runecore.combat.set.stat")
                .param("stat", stat)
                .param("value", fmt(value)));
    }

    private void sendAddMsg(CommandContext ctx, String stat, float value) {
        ctx.sendMessage(Message.translation("runecore.combat.add.stat")
                .param("value", fmt(value))
                .param("stat", stat));
    }

    private void sendUnknownStat(CommandContext ctx, String stat) {
        ctx.sendMessage(Message.translation("runecore.combat.error.unknown_stat").param("stat", stat));
        ctx.sendMessage(Message.translation("runecore.combat.stat_list"));
    }

    @Nullable
    private String getStatArg(CommandContext ctx) {
        String stat = ctx.get(this.statArg);
        if (stat == null) {
            ctx.sendMessage(Message.translation("runecore.combat.error.missing_stat"));
            return null;
        }
        return stat.toLowerCase();
    }

    @Nullable
    private Float getValueArg(CommandContext ctx) {
        String valStr = ctx.get(this.valueArg);
        if (valStr == null) {
            ctx.sendMessage(Message.translation("runecore.combat.error.missing_value"));
            return null;
        }
        try {
            return Float.parseFloat(valStr);
        } catch (NumberFormatException e) {
            ctx.sendMessage(Message.translation("runecore.combat.error.invalid_number").param("value", valStr));
            return null;
        }
    }

    private String fmt(float v) {
        return String.format("%.1f", v);
    }
}
