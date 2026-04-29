package com.cookie.runecore.content;

import com.cookie.runecore.api.ActiveBuff;
import com.cookie.runecore.api.MovementHelper;
import com.cookie.runecore.api.RuneEffect;
import com.cookie.runecore.api.StatHelper;
import com.cookie.runecore.api.StatusEffectHelper;
import com.cookie.runecore.api.VisualEffectHelper;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class CoreEffects {

    public static void init() {
        RuneCore core = RuneCore.get();

        core.registerEffect(new RuneEffect("speed", 1200)
            .withAsset("Speed")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "speed", 1200)
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("slowness", 600)
            .withAsset("Slowness")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applySlowness(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "slowness", 600)
                    .onExpire(ref -> MovementHelper.revertSlowness(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("haste", 1200)
            .withAsset("Haste")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyHaste(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "haste", 1200)
                    .onExpire(ref -> StatusEffectHelper.revertHaste(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("mining_fatigue", 1200)
            .withAsset("Mining_Fatigue")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyMiningFatigue(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "mining_fatigue", 1200)
                    .onExpire(ref -> StatusEffectHelper.revertMiningFatigue(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("jump_boost", 1200)
            .withAsset("Jump_Boost")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applyJumpBoost(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "jump_boost", 1200)
                    .onExpire(ref -> MovementHelper.revertJumpBoost(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("high_jump", 600)
            .withAsset("High_Jump")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applyHighJump(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "high_jump", 600)
                    .onExpire(ref -> MovementHelper.revertHighJump(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("slow_falling", 1200)
            .withAsset("Slow_Falling")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applySlowFalling(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "slow_falling", 1200)
                    .onExpire(ref -> MovementHelper.revertSlowFalling(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("levitation", 60)
            .withAsset("Levitation")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applyLevitation(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "levitation", 60)
                    .onExpire(ref -> MovementHelper.revertLevitation(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("regeneration", 400)
            .withAsset("Regeneration")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "regeneration", 400)
                    .interval(50)
                    .onTick(ref -> StatHelper.addHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("poison", 400)
            .withAsset("Poison")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "poison", 400)
                    .interval(25)
                    .onTick(ref -> StatHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("decay", 400)
            .withAsset("Decay")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "decay", 400)
                    .interval(40)
                    .onTick(ref -> StatHelper.subtractHealth(ref, 1.0f))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("burn", 200)
            .withAsset("Burn")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyBurn(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "burn", 200)
                    .interval(20)
                    .onTick(ref -> {
                        StatHelper.subtractHealth(ref, 1.0f);
                        StatusEffectHelper.onBurnTick(ref);
                    })
                    .onExpire(ref -> StatusEffectHelper.revertBurn(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("nausea", 200)
            .withAsset("Nausea")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyNausea(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "nausea", 200)
                    .interval(1)
                    .onTick(ref -> StatusEffectHelper.onNauseaTick(ref))
                    .onExpire(ref -> StatusEffectHelper.revertNausea(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("bleeding", 300)
            .withAsset("Bleeding")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyBleeding(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "bleeding", 300)
                    .interval(20)
                    .onTick(ref -> {
                        StatHelper.subtractHealth(ref, 1.0f);
                        StatusEffectHelper.onBleedingTick(ref);
                    })
                    .onExpire(ref -> StatusEffectHelper.revertBleeding(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("frozen", 600)
            .withAsset("Frozen")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    MovementHelper.applyFrozen(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "frozen", 600)
                    .interval(10)
                    .onTick(ref -> MovementHelper.onFrozenTick(ref))
                    .onExpire(ref -> MovementHelper.revertFrozen(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("instant_health", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                StatHelper.addHealth(ref, (float) (4.0 * ctx.power));
            }
        }));

        core.registerEffect(new RuneEffect("instant_damage", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                StatHelper.subtractHealth(ref, (float) (6.0 * ctx.power));
            }
        }).withAsset("InstantDamage"));

        core.registerEffect(new RuneEffect("damage_fire_instant", true, 0, ctx -> {
            if (ctx.target instanceof Ref<?> raw) {
                @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                StatHelper.subtractHealth(ref, (float) (10.0 * ctx.power));
            }
        }).withAsset("DamageFireInstant"));

        core.registerEffect(new RuneEffect("invisibility", "Invisibility", 1200)
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    StatusEffectHelper.applyInvisibility(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "invisibility", 1200)
                    .onExpire(ref -> StatusEffectHelper.revertInvisibility(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("glowing", 1200)
            .withAsset("Glowing")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    VisualEffectHelper.applyGlowing(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "glowing", 1200)
                    .onExpire(ref -> VisualEffectHelper.revertGlowing(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("blindness", 200)
            .withAsset("Blindness")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    VisualEffectHelper.applyBlindness(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "blindness", 200)
                    .onExpire(ref -> VisualEffectHelper.revertBlindness(ref))
                    .build();
            })
        );

        
        core.registerEffect(new RuneEffect("night_vision", 1200)
        .withAsset("NightVision")
            .withAction(ctx -> {
                if (ctx.target instanceof Ref<?> raw) {
                    @SuppressWarnings("unchecked") Ref<EntityStore> ref = (Ref<EntityStore>) raw;
                    VisualEffectHelper.applyNightVision(ref);
                }
            })
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "night_vision", 1200)
                    .onExpire(ref -> VisualEffectHelper.revertNightVision(ref))
                    .build();
            })
        );

        core.registerEffect(new RuneEffect("water_breathing", 1200)
            .withAsset("WaterBreathing")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "water_breathing", 1200).build();
            })
        );

        core.registerEffect(new RuneEffect("fire_resistance", 1200)
            .withAsset("FireResistance")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "fire_resistance", 1200).build();
            })
        );

        core.registerEffect(new RuneEffect("resistance", 1200)
            .withAsset("Resistance")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "resistance", 1200).build();
            })
        );

        core.registerEffect(new RuneEffect("strength", 1200)
            .withAsset("Strength")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "strength", 1200).build();
            })
        );

        core.registerEffect(new RuneEffect("weakness", 600)
            .withAsset("Weakness")
            .withBuff(ctx -> {
                String uid = ctx.source != null ? ctx.source.getUuid().toString() : ctx.target.toString();
                return ActiveBuff.builder(uid, "weakness", 600).build();
            })
        );
    }
}
