package com.cookie.runecore.api.combat;

import com.cookie.runecore.api.attribute.AttributeContainer;
import com.cookie.runecore.api.attribute.AttributeRegistry;
import com.cookie.runecore.api.attribute.RuneAttribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The pipeline is global static state, so every test unregisters what it added.
 */
class DamagePipelineTest {

    private static final float EPS = 0.001f;

    private final List<String> registered = new ArrayList<>();

    private void register(String id, int priority, DamageStage stage) {
        registered.add(id);
        DamagePipeline.register(id, priority, stage);
    }

    @AfterEach
    void cleanUp() {
        registered.forEach(DamagePipeline::unregister);
        registered.clear();
    }

    private static DamageContext ctx() {
        // Refs are only carried through for stages that want them; null is fine here.
        return new DamageContext(new AttributeContainer(), new AttributeContainer(),
                null, null, DamageKind.PHYSICAL, 100f);
    }

    @Test
    @DisplayName("with no stages the damage passes through untouched")
    void emptyPipeline() {
        assertEquals(50f, DamagePipeline.run(ctx(), 50f), EPS);
    }

    @Test
    @DisplayName("stages run in priority order, feeding each other")
    void ordering() {
        List<String> order = new ArrayList<>();

        // Registered out of order on purpose.
        register("t:late", DamagePipeline.FINAL, (c, d) -> { order.add("late"); return d + 1f; });
        register("t:early", DamagePipeline.BEFORE_MITIGATION, (c, d) -> { order.add("early"); return d * 2f; });
        register("t:mid", DamagePipeline.AFTER_MITIGATION, (c, d) -> { order.add("mid"); return d + 10f; });

        float result = DamagePipeline.run(ctx(), 5f);

        assertEquals(List.of("early", "mid", "late"), order);
        assertEquals(21f, result, EPS, "5*2=10, +10=20, +1=21");
    }

    @Test
    @DisplayName("a stage that throws is skipped, not allowed to abort the hit")
    void failingStageIsIsolated() {
        // A broken third-party stage must never make players invulnerable.
        register("t:boom", DamagePipeline.BEFORE_MITIGATION, (c, d) -> {
            throw new IllegalStateException("intentional");
        });
        register("t:after", DamagePipeline.AFTER_MITIGATION, (c, d) -> d + 5f);

        assertEquals(15f, DamagePipeline.run(ctx(), 10f), EPS,
                "the failing stage contributes nothing but the rest still runs");
    }

    @Test
    @DisplayName("damage never comes out negative")
    void neverNegative() {
        register("t:drain", DamagePipeline.FINAL, (c, d) -> -999f);
        assertEquals(0f, DamagePipeline.run(ctx(), 10f), EPS);
    }

    @Test
    @DisplayName("re-registering an id replaces the stage instead of stacking it")
    void reRegisterReplaces() {
        register("t:dup", DamagePipeline.FINAL, (c, d) -> d + 1f);
        DamagePipeline.register("t:dup", DamagePipeline.FINAL, (c, d) -> d + 100f);

        assertEquals(110f, DamagePipeline.run(ctx(), 10f), EPS, "only the newest applies");
    }

    @Test
    @DisplayName("slices run only their half of the chain")
    void prioritySlices() {
        // This is how the interceptor wraps RuneCore's own maths.
        register("t:pre", DamagePipeline.BEFORE_MITIGATION, (c, d) -> d + 1f);
        register("t:post", DamagePipeline.AFTER_MITIGATION, (c, d) -> d * 2f);

        float beforeOnly = DamagePipeline.runBefore(ctx(), 10f, DamagePipeline.MITIGATION);
        assertEquals(11f, beforeOnly, EPS, "post stage must not have run");

        float fromOnly = DamagePipeline.runFrom(ctx(), 10f, DamagePipeline.MITIGATION);
        assertEquals(20f, fromOnly, EPS, "pre stage must not have run");
    }

    @Test
    @DisplayName("a stage can read an attribute RuneCore has never heard of")
    void customAttributeDrivesDamage() {
        // The whole point of the extensible model.
        RuneAttribute crit = AttributeRegistry.register(RuneAttribute.fraction("pipetest:crit", 1f));

        AttributeContainer attacker = new AttributeContainer();
        attacker.setBase(crit, 1f);
        DamageContext context = new DamageContext(attacker, new AttributeContainer(),
                null, null, DamageKind.PHYSICAL, 100f);

        register("t:crit", DamagePipeline.AFTER_MITIGATION,
                (c, d) -> c.attacker().get(crit) >= 1f ? d * 2f : d);

        assertEquals(60f, DamagePipeline.run(context, 30f), EPS);
    }

    @Test
    @DisplayName("registered ids are reported in execution order")
    void stageIdsAreOrdered() {
        register("t:b", DamagePipeline.FINAL, (c, d) -> d);
        register("t:a", DamagePipeline.BEFORE_MITIGATION, (c, d) -> d);

        List<String> ids = DamagePipeline.stageIds();
        assertTrue(ids.indexOf("t:a") < ids.indexOf("t:b"));
    }

    @Test
    @DisplayName("blank ids and null stages are rejected quietly")
    void invalidRegistration() {
        DamagePipeline.register(null, DamagePipeline.FINAL, (c, d) -> d + 1f);
        DamagePipeline.register("t:null", DamagePipeline.FINAL, null);
        DamagePipeline.register("  ", DamagePipeline.FINAL, (c, d) -> d + 1f);

        assertEquals(10f, DamagePipeline.run(ctx(), 10f), EPS);
    }
}
