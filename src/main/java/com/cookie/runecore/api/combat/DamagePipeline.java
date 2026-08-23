package com.cookie.runecore.api.combat;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Ordered chain every intercepted hit runs through.
 * <p>
 * The damage formula used to be a closed method on {@code CombatStats}, so the only attributes
 * that could ever affect a hit were the ones RuneCore itself declared. Stages open that up:
 * mods insert their own step at a chosen priority and read whatever attributes they registered.
 * <p>
 * Stages run on the thread that raised the damage event — keep them cheap and free of blocking
 * work.
 */
public final class DamagePipeline {

    /** Before armour/resist are applied — good for flat bonuses to the incoming amount. */
    public static final int BEFORE_MITIGATION = 100;
    /** Where RuneCore's own armour, resist and reduction run. */
    public static final int MITIGATION = 500;
    /** After mitigation, before shields — multiplicative effects such as crits belong here. */
    public static final int AFTER_MITIGATION = 700;
    /** Last word on the number, after shields have absorbed their part. */
    public static final int FINAL = 900;

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private static final List<Registration> STAGES = new CopyOnWriteArrayList<>();

    private DamagePipeline() {}

    /**
     * Adds a stage. Registering the same id twice replaces the first, so a mod reloading its
     * content does not stack duplicates.
     *
     * @param id       namespaced, e.g. {@code mymod:crit}
     * @param priority lower runs earlier; use the constants on this class as anchors
     */
    public static void register(String id, int priority, DamageStage stage) {
        if (id == null || id.isBlank() || stage == null) return;
        String key = id.toLowerCase(Locale.ROOT);

        STAGES.removeIf(r -> r.id.equals(key));
        STAGES.add(new Registration(key, priority, stage));
        STAGES.sort((a, b) -> Integer.compare(a.priority, b.priority));
    }

    public static void unregister(String id) {
        if (id == null) return;
        String key = id.toLowerCase(Locale.ROOT);
        STAGES.removeIf(r -> r.id.equals(key));
    }

    /**
     * Runs every stage in order.
     * <p>
     * A stage that throws is logged and skipped rather than allowed to abort the hit — a broken
     * third-party stage should not make players invulnerable.
     *
     * @return the final amount, never negative
     */
    public static float run(DamageContext context, float startingDamage) {
        return runRange(context, startingDamage, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /** Runs only the stages registered before {@code priority} (exclusive). */
    public static float runBefore(DamageContext context, float startingDamage, int priority) {
        return runRange(context, startingDamage, Integer.MIN_VALUE, priority - 1);
    }

    /** Runs only the stages registered at {@code priority} or later. */
    public static float runFrom(DamageContext context, float startingDamage, int priority) {
        return runRange(context, startingDamage, priority, Integer.MAX_VALUE);
    }

    /**
     * Runs the stages whose priority falls in {@code [from, to]}.
     * <p>
     * The slices exist because RuneCore's own armour/resist/shield maths is not expressible as
     * a stage: it needs the physical/magic split of the attacker's offence, which no single
     * float carries between steps. The interceptor therefore runs the stages before
     * {@link #MITIGATION}, does its calculation, then runs the rest.
     */
    public static float runRange(DamageContext context, float startingDamage, int from, int to) {
        float damage = startingDamage;

        for (Registration registration : STAGES) {
            if (registration.priority < from || registration.priority > to) continue;
            try {
                damage = registration.stage.apply(context, damage);
            } catch (RuntimeException e) {
                // A broken third-party stage must not abort the hit and make players immortal.
                LOG.warning("damage stage '" + registration.id + "' failed, skipping: " + e);
            }
        }

        return Math.max(0f, damage);
    }

    /** Diagnostics: the registered stage ids, in execution order. */
    public static List<String> stageIds() {
        return STAGES.stream().map(r -> r.id).toList();
    }

    private record Registration(String id, int priority, DamageStage stage) {}
}
