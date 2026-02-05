# RuneCore API Usage Guide

RuneCore is a modular magic system engine designed for Hytale mods. It provides the foundation for creating elements, essences, spells, and effects, allowing different magic mods to interact with a shared ecosystem.

## Core Concepts

### 1. RuneElement

The `RuneElement` enum defines the fundamental types of magic available in the system.

- **Basic:** `FIRE`, `EARTH`, `WIND`, `WATER`, `ICE`, `LIGHTNING`
- **Advanced:** `LIGHT`, `SHADOW`, `LIFE`, `DEATH`, `MIND`, `BLOOD`
- **Unstable:** `CHAOS`, `AETHER`, `VOID`, `TIME`
- **Chemical:** `METAL`, `CRYSTAL`, `POISON`, `ACID`

### 2. Essence

`Essence` represents the raw magical material required to cast spells.

```java
// ID, Element, Tier
Essence fireEssence = new Essence("essence_fire", RuneElement.FIRE, 1);
RuneCore.get().registerEssence(fireEssence);
```

### 3. GameResource

Resources like Mana, Stamina, or Blood are defined using `GameResource`.

```java
// ID, Max Value, Regen Rate, Overflow Penalty
GameResource mana = new GameResource("arcane_mana", 100.0, 5.0, "burnout");
RuneCore.get().registerResource(mana);
```

### 4. RuneEffect

Effects are the actual actions performed by a spell. They contain the logic executed when a spell is cast.

```java
RuneEffect damageEffect = new RuneEffect("damage_instant", true, 0, (ctx) -> {
    System.out.println("Dealing damage to " + ctx.target);
});
RuneCore.get().registerEffect(damageEffect);
```

### 5. Spell

A `Spell` is a container that links costs, conditions, and effects.

```java
Spell fireball = new Spell("fireball", (ctx) -> ctx.target != null)
    .addCost("arcane_mana", 20)
    .addEssenceRequirement(RuneElement.FIRE, 1)
    .addEffect("damage_instant");

RuneCore.get().registerSpell(fireball);
```

## Execution Pipeline

### Casting a Spell

To cast a spell, you need a `CastContext` which contains the source, target, world, and base power.

```java
CastContext ctx = new CastContext(player, target, world, 1.0);
boolean success = RuneCore.get().castSpell("fireball", ctx);
```

### Event Hooks

You can listen to events during the spell casting lifecycle to modify behavior (e.g., adding damage boosts).

```java
RuneCore.get().on("spell:pre_cast", (ctx) -> {
    System.out.println("Spell is about to be cast!");
    ctx.power *= 1.1; // Boost power by 10%
});
```

**Lifecycle Events:**

- `spell:pre_cast`: Before resources are consumed.
- `resource:consume`: When resources are deducted.
- `effect:applied`: After each effect is executed.
- `spell:post_cast`: After all effects are done.
