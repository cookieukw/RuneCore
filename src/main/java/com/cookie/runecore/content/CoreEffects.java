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
            .withAsset("Speed")
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
            .withAsset("Slowness")
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
            .withAsset("Haste")
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
            .withAsset("Mining_Fatigue")
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
            .withAsset("Jump_Boost")
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
            .withAsset("High_Jump")
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
            .withAsset("Slow_Falling")
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

        core.registerEffect(new RuneEffect("levitation", 60)
            .withAsset("Levitation")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyLevitation(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "levitation", 60)
                    .onExpire(ref -> EffectHelper.revertLevitation(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("regeneration", 400)
            .withAsset("Regeneration")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "regeneration", 400)
                    .interval(50)
                    .onTick(ref -> EffectHelper.addHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("poison", 400)
            .withAsset("Poison")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "poison", 400)
                    .interval(25)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("decay", 400)
            .withAsset("Decay")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "decay", 400)
                    .interval(40)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("burn", 200)
            .withAsset("Burn")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "burn", 200)
                    .interval(20)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("bleeding", 300)
            .withAsset("Bleeding")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "bleeding", 300)
                    .interval(30)
                    .onTick(ref -> EffectHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("frozen", 600)
            .withAsset("Frozen")
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

        core.registerEffect(new RuneEffect("invisibility", "Invisibility", 1200)
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    EffectHelper.applyInvisibility(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "invisibility", 1200)
                    .onExpire(ref -> EffectHelper.revertInvisibility(ref))
                    .build();
            })
        );
        core.registerEffect(new RuneEffect("glowing", "Glowing", 1200));
        core.registerEffect(new RuneEffect("blindness", "Blindness", 200));
        core.registerEffect(new RuneEffect("nausea", "Nausea", 200));
        core.registerEffect(new RuneEffect("darkness", "Darkness", 400));
        core.registerEffect(new RuneEffect("night_vision", "Night_Vision", 1200));
        core.registerEffect(new RuneEffect("water_breathing", "Water_Breathing", 1200));
        core.registerEffect(new RuneEffect("fire_resistance", "Fire_Resistance", 1200));
        core.registerEffect(new RuneEffect("resistance", "Resistance", 1200));
        core.registerEffect(new RuneEffect("strength", "Strength", 1200));
        core.registerEffect(new RuneEffect("weakness", "Weakness", 600));
        core.registerEffect(new RuneEffect("electrified", "Electrified", 200));
    }
}
