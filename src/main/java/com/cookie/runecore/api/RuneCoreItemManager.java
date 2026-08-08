package com.cookie.runecore.api;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * Dispatch table for "player used an item RuneCore cares about".
 * <p>
 * Handlers are keyed by an item id <b>suffix</b>, so a mod can claim every item whose id ends in
 * {@code MagicStaff} without enumerating them.
 */
public final class RuneCoreItemManager {

    /**
     * Concurrent because registration happens on the setup thread while lookups run on the
     * interaction thread — a plain {@code HashMap} published that way has no memory barrier and
     * can be observed half-built.
     */
    private static final Map<String, BiConsumer<Player, PlayerRef>> ITEM_HANDLERS = new ConcurrentHashMap<>();

    private RuneCoreItemManager() {}

    public static void register(String itemIdSuffix, BiConsumer<Player, PlayerRef> handler) {
        if (itemIdSuffix == null || itemIdSuffix.isBlank() || handler == null) return;
        ITEM_HANDLERS.put(itemIdSuffix.toLowerCase(Locale.ROOT), handler);
    }

    public static void unregister(String itemIdSuffix) {
        if (itemIdSuffix == null) return;
        ITEM_HANDLERS.remove(itemIdSuffix.toLowerCase(Locale.ROOT));
    }

    /**
     * Runs the handler that claims {@code itemId}.
     * <p>
     * When several suffixes match, the <b>longest</b> one wins — the most specific handler.
     * Iteration used to stop at the first match found while walking a {@code HashMap}, so with
     * both {@code Staff} and {@code MagicStaff} registered the winner depended on hash order and
     * could differ between runs.
     *
     * @return true if a handler took the item
     */
    public static boolean handleItemUse(String itemId, Player player, PlayerRef playerRef) {
        if (itemId == null) return false;
        String id = itemId.toLowerCase(Locale.ROOT);

        BiConsumer<Player, PlayerRef> best = null;
        int bestLength = -1;

        for (Map.Entry<String, BiConsumer<Player, PlayerRef>> entry : ITEM_HANDLERS.entrySet()) {
            String suffix = entry.getKey();
            if (id.endsWith(suffix) && suffix.length() > bestLength) {
                best = entry.getValue();
                bestLength = suffix.length();
            }
        }

        if (best == null) return false;
        best.accept(player, playerRef);
        return true;
    }

    /** Whether any handler claims this item, without running it. */
    public static boolean hasHandler(String itemId) {
        if (itemId == null) return false;
        String id = itemId.toLowerCase(Locale.ROOT);
        return ITEM_HANDLERS.keySet().stream().anyMatch(id::endsWith);
    }
}
