package com.cookie.runemagic;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Store;
import com.cookie.runecore.api.PlayerDataComponent;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;

public class SwitchSpellCommand extends AbstractCommand {
    public SwitchSpellCommand() {
        super("switchspell", "Switch active spell for RuneMagic");
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        com.hypixel.hytale.component.Ref<EntityStore> playerEntityRef = null;
        try {
            playerEntityRef = ctx.senderAsPlayerRef();
        } catch (Exception ignored) {
        }

        if (playerEntityRef == null) {
            ctx.sendMessage(com.hypixel.hytale.server.core.Message.raw("You must be a player to use this command."));
            return CompletableFuture.completedFuture(null);
        }

        Store<EntityStore> store = playerEntityRef.getStore();
        if (store == null)
            return CompletableFuture.completedFuture(null);

        PlayerRef player = store.getComponent(playerEntityRef, PlayerRef.getComponentType());
        if (player == null)
            return CompletableFuture.completedFuture(null);

        PlayerDataComponent data = store.getComponent(playerEntityRef, PlayerDataComponent.TYPE);
        if (data == null)
            return CompletableFuture.completedFuture(null);

        int newIndex = (data.getActiveSpellIndex() + 1) % 2;
        data.setActiveSpellIndex(newIndex);

        String spellName = newIndex == 0 ? "Fireball" : "Toxic Bolt";
        player.sendMessage(
                com.hypixel.hytale.server.core.Message.raw("§6[RuneMagic] §fSpell switched to: §e" + spellName));

        return CompletableFuture.completedFuture(null);
    }
}
