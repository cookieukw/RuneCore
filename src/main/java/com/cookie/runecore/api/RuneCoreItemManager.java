package com.cookie.runecore.api;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RuneCoreItemManager {
    
    // Maps item ID suffixes (e.g., "MagicStaff") to their handler logic.
    private static final Map<String, BiConsumer<Player, PlayerRef>> itemHandlers = new HashMap<>();

    public static void register(String itemIdSuffix, BiConsumer<Player, PlayerRef> handler) {
        itemHandlers.put(itemIdSuffix, handler);
    }

    public static boolean handleItemUse(String itemId, Player player, PlayerRef playerRef) {
        if (itemId == null) return false;
        
        for (Map.Entry<String, BiConsumer<Player, PlayerRef>> entry : itemHandlers.entrySet()) {
            if (itemId.endsWith(entry.getKey())) {
                entry.getValue().accept(player, playerRef);
                return true;
            }
        }
        return false;
    }
}
