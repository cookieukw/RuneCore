package com.cookie.runemagic;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.cookie.runecore.api.PlayerDataComponent;

public class MagicListener {

    public MagicListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
        // SwitchActiveSlotEvent will be handled when we find the right entity reference
        // method
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();
        if (playerRef == null)
            return;

        // Check for right click. Use ordinal or specific enum match if known.
        // Assuming 1 is right click for now, but will check MouseButtonEvent if
        // possible.
        if (event.getMouseButton() == null)
            return;

        Item itemType = event.getItemInHand();
        if (itemType == null)
            return;

        String itemId = itemType.getId().toString().toLowerCase();

        if (itemId.contains("grimoire") || itemId.contains("spellbook")) {
            com.hypixel.hytale.component.Store<EntityStore> store = playerRef.getReference().getStore();
            if (store == null)
                return;

            PlayerDataComponent data = store.getComponent(playerRef.getReference(), PlayerDataComponent.TYPE);
            if (data == null)
                return;

            // Simplified grimoire management using PlayerData
            String activeSpellId = data.getActiveSpellIndex() == 0 ? "fireball" : "toxic_bolt";

            CastContext ctx = new CastContext(playerRef, null, null, 1.0);
            RuneCore.get().castSpell(activeSpellId, ctx);

            playerRef.sendMessage(
                    com.hypixel.hytale.server.core.Message.raw("§6[RuneMagic] §fCast spell: §e" + activeSpellId));
        }
    }
}
