package com.cookie.runecore.api.combat;

/**
 * One step of the damage pipeline.
 * <p>
 * This is how a mod makes its own attribute matter. RuneCore cannot know what
 * {@code mymod:lifesteal} or {@code mymod:crit_chance} mean, so behaviour is contributed rather
 * than inferred: register an attribute, then register a stage that reads it.
 *
 * <pre>{@code
 * RuneAttribute CRIT = AttributeRegistry.register(RuneAttribute.fraction("mymod:crit", 1f));
 *
 * DamagePipeline.register("mymod:crit", DamagePipeline.AFTER_MITIGATION, (ctx, damage) ->
 *         Math.random() < ctx.attacker().get(CRIT) ? damage * 2f : damage);
 * }</pre>
 */
@FunctionalInterface
public interface DamageStage {

    /**
     * @param damage the amount as left by the previous stage
     * @return the amount passed to the next stage; must not be negative
     */
    float apply(DamageContext context, float damage);
}
