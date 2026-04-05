package com.cookie.runecore.content;

import com.cookie.runecore.api.ActiveBuff;
import com.cookie.runecore.api.EffectHelper;
import com.cookie.runecore.api.RuneEffect;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class CoreEffects {

    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEffect(new RuneEffect("speed", 1200)
            .withAsset("runecore:Speed")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applySpeed(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "speed", 1200)
                    .onExpire(ref -> EffectHelper.revertSpeed(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("slowness", 600)
            .withAsset("runecore:Slowness")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applySlowness(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "slowness", 600)
                    .onExpire(ref -> EffectHelper.revertSlowness(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("haste", 1200)
            .withAsset("runecore:Haste")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyHaste(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "haste", 1200)
                    .onExpire(ref -> EffectHelper.revertHaste(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("mining_fatigue", 1200)
            .withAsset("runecore:Mining_Fatigue")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyMiningFatigue(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "mining_fatigue", 1200)
                    .onExpire(ref -> EffectHelper.revertMiningFatigue(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("jump_boost", 1200)
            .withAsset("runecore:Jump_Boost")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyJumpBoost(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "jump_boost", 1200)
                    .onExpire(ref -> EffectHelper.revertJumpBoost(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("high_jump", 600)
            .withAsset("runecore:High_Jump")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyHighJump(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "high_jump", 600)
                    .onExpire(ref -> EffectHelper.revertHighJump(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("slow_falling", 1200)
            .withAsset("runecore:Slow_Falling")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applySlowFalling(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "slow_falling", 1200)
                    .onExpire(ref -> EffectHelper.revertSlowFalling(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("levitation", 200)
            .withAsset("runecore:Levitation")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyLevitation(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "levitation", 200)
                    .onExpire(ref -> EffectHelper.revertLevitation(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("regeneration", 400)
            .withAsset("runecore:Regeneration")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "regeneration", 400)
                    .interval(50)
                    .onTick(ref -> EffectHelper.addHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("poison", 400)
            .withAsset("runecore:Poison")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "poison", 400)
                    .interval(25)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("wither", 400)
            .withAsset("runecore:Wither")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "wither", 400)
                    .interval(40)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("burn", 200)
            .withAsset("runecore:Burn")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "burn", 200)
                    .interval(20)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("bleeding", 300)
            .withAsset("runecore:Bleeding")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "bleeding", 300)
                    .interval(30)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("frozen", 600)
            .withAsset("runecore:Frozen")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyFrozen(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "frozen", 600)
                    .onExpire(ref -> EffectHelper.revertFrozen(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("instant_health", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                float amount = (float)(4.0 * ctx.power);
                EffectHelper.addHealth(ref, amount);
            }
        }));

        core.registerEffect(new RuneEffect("instant_damage", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                float amount = (float)(6.0 * ctx.power);
                EffectHelper.subtractHealth(ref, amount);
            }
        }));

        core.registerEffect(new RuneEffect("damage_fire_instant", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                float amount = (float)(10.0 * ctx.power);
                EffectHelper.subtractHealth(ref, amount);
            }
        }));

        core.registerEffect(new RuneEffect("invisibility", "hytale:invisibility", 1200));
        core.registerEffect(new RuneEffect("glowing", "hytale:glowing", 1200));
        core.registerEffect(new RuneEffect("blindness", "hytale:blindness", 200));
        core.registerEffect(new RuneEffect("nausea", "hytale:nausea", 200));
        core.registerEffect(new RuneEffect("darkness", "hytale:darkness", 400));
        core.registerEffect(new RuneEffect("night_vision", "hytale:night_vision", 1200));
        core.registerEffect(new RuneEffect("water_breathing", "hytale:water_breathing", 1200));
        core.registerEffect(new RuneEffect("fire_resistance", "hytale:fire_resistance", 1200));
        core.registerEffect(new RuneEffect("resistance", "hytale:resistance", 1200));
        core.registerEffect(new RuneEffect("strength", "hytale:strength", 1200));
        core.registerEffect(new RuneEffect("weakness", "hytale:weakness", 600));
        core.registerEffect(new RuneEffect("electrified", "hytale:electrified", 200));
    }
}
