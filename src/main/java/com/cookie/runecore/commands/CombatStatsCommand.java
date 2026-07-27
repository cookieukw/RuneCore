package com.cookie.runecore.commands;

import com.cookie.runecore.api.CombatStats;
import com.cookie.runecore.systems.CombatStatsManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CombatStatsCommand extends AbstractCommand {

    public CombatStatsCommand() {
        super("combatstats", "View your RuneCore combat stats.");
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        Ref<EntityStore> playerRef;
        try {
            playerRef = ctx.senderAsPlayerRef();
        } catch (Error | Exception e) {
            ctx.sendMessage(Message.raw("You must be a player to use this command."));
            return CompletableFuture.completedFuture(null);
        }
        if (playerRef == null || !playerRef.isValid()) {
            ctx.sendMessage(Message.raw("You must be a player to use this command."));
            return CompletableFuture.completedFuture(null);
        }

        Store<EntityStore> store = playerRef.getStore();
        PlayerRef pr = (PlayerRef) store.getComponent(playerRef, PlayerRef.getComponentType());
        if (pr == null) {
            ctx.sendMessage(Message.raw("Could not resolve player."));
            return CompletableFuture.completedFuture(null);
        }

        UUID uuid = pr.getUuid();
        CombatStatsManager manager = CombatStatsManager.get();
        if (manager == null || uuid == null || !manager.hasStats(uuid)) {
            ctx.sendMessage(Message.raw("No combat stats found."));
            return CompletableFuture.completedFuture(null);
        }

        CombatStats s = manager.getStats(uuid);
        StringBuilder sb = new StringBuilder();
        sb.append("§6=== Combat Stats ===\n");
        sb.append("§e⚔ Physical Damage: §f").append(fmt(s.getPhysicalDamage())).append("\n");
        sb.append("§d✦ Magic Damage: §f").append(fmt(s.getMagicDamage())).append("\n");
        sb.append("§c☄ True Damage: §f").append(fmt(s.getTrueDamage())).append("\n");
        sb.append("§e⊘ Armor Pen: §f").append(fmt(s.getArmorPenetration())).append("\n");
        sb.append("§d⊘ Magic Pen: §f").append(fmt(s.getMagicPenetration())).append("\n");
        sb.append("§6--- Defense ---\n");
        sb.append("§b🛡 Armor: §f").append(fmt(s.getArmor())).append("\n");
        sb.append("§9✧ Magic Resist: §f").append(fmt(s.getMagicResist())).append("\n");
        sb.append("§a❖ Damage Reduction: §f").append(fmt(s.getDamageReduction() * 100f)).append("%\n");
        if (s.getMaxShieldHP() > 0) {
            sb.append("§e⊕ Shield: §f").append(fmt(s.getShieldHP()))
              .append(" / ").append(fmt(s.getMaxShieldHP())).append("\n");
        }

        ctx.sendMessage(Message.raw(sb.toString()));
        return CompletableFuture.completedFuture(null);
    }

    private String fmt(float v) {
        return String.format("%.1f", v);
    }
}
