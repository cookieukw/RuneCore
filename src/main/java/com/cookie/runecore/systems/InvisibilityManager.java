package com.cookie.runecore.systems;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Authoritative record of which players are currently invisible.
 * <p>
 * {@code HiddenPlayersManager} is <b>per viewer</b>: hiding someone writes into each observer's
 * own set. Broadcasting that once, as the effect used to, produced three bugs:
 * <ul>
 *   <li>the loop covered <b>every</b> player in the world including the target, so the player
 *       was hidden from their own client — which is what made the character drop through the
 *       ground, since a client that is not tracking its own entity has nothing to collide with;</li>
 *   <li>players who connected later were never told, so they saw the invisible player normally;</li>
 *   <li>nothing ever undid it on disconnect, and the buff that carried {@code revertInvisibility}
 *       is dropped without running {@code onExpire} once the entity ref goes invalid — so the
 *       UUID stayed in every observer's hidden set for the rest of their session.</li>
 * </ul>
 * Keeping the set here lets new observers be caught up on join and guarantees cleanup on
 * disconnect, independently of whether the buff got a chance to expire.
 */
public class InvisibilityManager {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private static volatile InvisibilityManager instance;

    private final Set<UUID> invisible = ConcurrentHashMap.newKeySet();

    public InvisibilityManager(EventRegistry eventRegistry) {
        instance = this;
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    public static InvisibilityManager get() {
        return instance;
    }

    public boolean isInvisible(UUID playerId) {
        return playerId != null && invisible.contains(playerId);
    }

    /**
     * Hides {@code playerId} from everyone else.
     * <p>
     * The player is deliberately <b>not</b> hidden from themselves: they keep seeing their own
     * character, and the client keeps tracking its own entity.
     */
    public void hide(UUID playerId) {
        if (playerId == null) return;
        invisible.add(playerId);

        for (PlayerRef observer : Universe.get().getPlayers()) {
            if (isSelf(observer, playerId)) continue;
            observer.getHiddenPlayersManager().hidePlayer(playerId);
        }
        LOG.fine("[RuneCore-Invisibility] " + playerId + " hidden from other players");
    }

    /** Makes {@code playerId} visible again to everyone. Safe to call when not invisible. */
    public void show(UUID playerId) {
        if (playerId == null) return;
        invisible.remove(playerId);

        for (PlayerRef observer : Universe.get().getPlayers()) {
            observer.getHiddenPlayersManager().showPlayer(playerId);
        }
        LOG.fine("[RuneCore-Invisibility] " + playerId + " shown again");
    }

    /** Drops all invisibility state, e.g. on shutdown. */
    public void clear() {
        for (UUID playerId : Set.copyOf(invisible)) {
            show(playerId);
        }
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        PlayerRef joiner = event.getPlayer() != null ? event.getPlayer().getPlayerRef() : null;
        if (joiner == null || joiner.getUuid() == null) return;
        UUID joinerId = joiner.getUuid();

        // Reconnecting while flagged means the effect died with the old session — the buff is
        // gone, so the flag must go too. Without this the player stays invisible forever.
        if (invisible.remove(joinerId)) {
            LOG.fine("[RuneCore-Invisibility] " + joinerId + " reconnected, clearing stale flag");
            for (PlayerRef observer : Universe.get().getPlayers()) {
                observer.getHiddenPlayersManager().showPlayer(joinerId);
            }
        }

        // Catch the new arrival up on everyone who is currently invisible.
        for (UUID hidden : invisible) {
            if (hidden.equals(joinerId)) continue;
            joiner.getHiddenPlayersManager().hidePlayer(hidden);
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef leaver = event.getPlayerRef();
        if (leaver == null || leaver.getUuid() == null) return;

        // Clear on the way out so the UUID does not linger in everyone else's hidden set.
        if (invisible.contains(leaver.getUuid())) {
            show(leaver.getUuid());
        }
    }

    private static boolean isSelf(PlayerRef observer, UUID playerId) {
        return observer.getUuid() != null && observer.getUuid().equals(playerId);
    }
}
