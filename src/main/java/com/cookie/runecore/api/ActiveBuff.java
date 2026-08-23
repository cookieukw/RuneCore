package com.cookie.runecore.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.function.Consumer;

public class ActiveBuff {
    public final String effectId;
    public final String playerId;
    public int remainingTicks;
    public final int intervalTicks;
    public int ticksSinceLastApply;

    private final Consumer<Ref<EntityStore>> onTick;
    private final Consumer<Ref<EntityStore>> onExpire;

    public ActiveBuff(String playerId, String effectId, int durationTicks, int intervalTicks,
                      Consumer<Ref<EntityStore>> onTick, Consumer<Ref<EntityStore>> onExpire) {
        this.playerId = playerId;
        this.effectId = effectId;
        this.remainingTicks = durationTicks;
        this.intervalTicks = intervalTicks;
        this.ticksSinceLastApply = 0;
        this.onTick = onTick;
        this.onExpire = onExpire;
    }

    public boolean tick(Ref<EntityStore> ref) {
        remainingTicks--;
        ticksSinceLastApply++;

        if (intervalTicks > 0 && ticksSinceLastApply >= intervalTicks) {
            ticksSinceLastApply = 0;
            if (onTick != null && (ref == null || ref.isValid())) {
                onTick.accept(ref);
            }
        }

        if (remainingTicks <= 0) {
            expire(ref);
            return false;
        }
        return true;
    }

    /**
     * Runs the expiry callback, <b>even if the entity is already gone</b>.
     * <p>
     * The guard used to be {@code onExpire != null && (ref == null || ref.isValid())}, which
     * meant an effect whose entity had been invalidated — a player who died or disconnected —
     * never got cleaned up. Invisibility was the visible symptom: the revert never ran, so the
     * player stayed hidden in every observer's list.
     * <p>
     * Callbacks that genuinely need the entity already no-op on an invalid ref (the helpers
     * check before touching anything), so running them unconditionally is safe and lets
     * cleanup keyed on the player UUID rather than the ref do its job.
     */
    public void expire(Ref<EntityStore> ref) {
        if (onExpire != null) {
            onExpire.accept(ref);
        }
    }

    public static Builder builder(String playerId, String effectId, int durationTicks) {
        return new Builder(playerId, effectId, durationTicks);
    }

    public static class Builder {
        private final String playerId;
        private final String effectId;
        private final int durationTicks;
        private int intervalTicks = 0;
        private Consumer<Ref<EntityStore>> onTick = null;
        private Consumer<Ref<EntityStore>> onExpire = null;

        private Builder(String playerId, String effectId, int durationTicks) {
            this.playerId = playerId;
            this.effectId = effectId;
            this.durationTicks = durationTicks;
        }

        public Builder interval(int ticks) {
            this.intervalTicks = ticks;
            return this;
        }

        public Builder onTick(Consumer<Ref<EntityStore>> action) {
            this.onTick = action;
            return this;
        }

        public Builder onExpire(Consumer<Ref<EntityStore>> action) {
            this.onExpire = action;
            return this;
        }

        public ActiveBuff build() {
            return new ActiveBuff(playerId, effectId, durationTicks, intervalTicks, onTick, onExpire);
        }
    }
}
