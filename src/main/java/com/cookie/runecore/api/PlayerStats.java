package com.cookie.runecore.api;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Convenience view over a player's stats.
 * <p>
 * This is a facade. It used to also contain the {@link com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap}
 * plumbing and the movement/packet handling inline — three unrelated jobs in one file, with the
 * {@code ref → store → externalData → world} guard chain copy-pasted five times. The mechanics
 * now live in {@code EntityStatAccess}, {@code PlayerMovementStats} and {@code WorldTasks};
 * the public surface here is unchanged.
 */
public class PlayerStats {

    private final Ref<EntityStore> playerRef;

    private static final float MIN_SPEED = 0.0f;
    private static final float MAX_SPEED = 100.0f;
    private static final float DEFAULT_SPEED = 5.5f;

    public PlayerStats(@Nonnull Ref<EntityStore> playerRef) {
        this.playerRef = playerRef;
        // Manual validation should be done by the caller using playerRef.isValid()
    }

    public PlayerStats(@Nonnull PlayerRef playerRef) {
        this(playerRef.getReference());
    }

    // ── Reads ────────────────────────────────────────────────────────────────
    // Each completes with -1f when the stat cannot be read.

    public CompletableFuture<Float> getHealth() {
        return EntityStatAccess.read(playerRef, DefaultEntityStatTypes.getHealth());
    }

    public CompletableFuture<Float> getMana() {
        return EntityStatAccess.read(playerRef, DefaultEntityStatTypes.getMana());
    }

    public CompletableFuture<Float> getStamina() {
        return EntityStatAccess.read(playerRef, DefaultEntityStatTypes.getStamina());
    }

    /** @deprecated placeholder — always returns 100f, the engine exposes no max-mana stat yet. */
    @Deprecated
    public CompletableFuture<Float> getMaxMana() {
        return CompletableFuture.completedFuture(100f);
    }

    // ── Health ───────────────────────────────────────────────────────────────

    public void addHealth(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getHealth(), amount);
    }

    public void subtractHealth(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getHealth(), -amount);
    }

    public void setHealth(float amount) {
        EntityStatAccess.set(playerRef, DefaultEntityStatTypes.getHealth(), amount);
    }

    // ── Mana ─────────────────────────────────────────────────────────────────

    public void addMana(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getMana(), amount);
    }

    public void subtractMana(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getMana(), -amount);
    }

    public void setMana(float amount) {
        EntityStatAccess.set(playerRef, DefaultEntityStatTypes.getMana(), amount);
    }

    // ── Stamina ──────────────────────────────────────────────────────────────

    public void addStamina(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getStamina(), amount);
    }

    public void subtractStamina(float amount) {
        EntityStatAccess.modify(playerRef, DefaultEntityStatTypes.getStamina(), -amount);
    }

    public void setStamina(float amount) {
        EntityStatAccess.set(playerRef, DefaultEntityStatTypes.getStamina(), amount);
    }

    // ── Movement speed ───────────────────────────────────────────────────────

    public void addSpeed(float amount) {
        PlayerMovementStats.addSpeed(playerRef, amount, MIN_SPEED, MAX_SPEED);
    }

    public void subtractSpeed(float amount) {
        PlayerMovementStats.addSpeed(playerRef, -amount, MIN_SPEED, MAX_SPEED);
    }

    public void setSpeed(float amount) {
        PlayerMovementStats.setSpeed(playerRef, amount, MIN_SPEED, MAX_SPEED);
    }

    public void resetSpeed() {
        PlayerMovementStats.setSpeed(playerRef, DEFAULT_SPEED, MIN_SPEED, MAX_SPEED);
    }

    public CompletableFuture<Float> getSpeed() {
        return PlayerMovementStats.readSpeed(playerRef);
    }
}
