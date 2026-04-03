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
            if (onExpire != null && (ref == null || ref.isValid())) {
                onExpire.accept(ref);
            }
            return false;
        }
        return true;
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
