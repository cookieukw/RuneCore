# RuneCore API Manual

Welcome to the **RuneCore** documentation. This manual details the architecture, classes, and usage flows of the RuneCore magic system.

## 1. System Overview

RuneCore is designed as a **data-driven magic engine**. Instead of hardcoding magic logic directly into items or blocks, you define:

1.  **Resources** (what spells cost)
2.  **Essences** (the types of magic available)
3.  **Effects** (what magic _does_)
4.  **Spells** (how effects are combined and triggered)

The core system (`RuneCore.java`) acts as a registry and an execution engine that processes these definitions.

---

## 2. API Reference (Class by Class)

### 2.1. Enum: `RuneElement`

**File:** `api/RuneElement.java`

Defines the elemental types available in the game. It is divided into conceptual categories:

- **Basic:** `FIRE`, `EARTH`, `WATER`, etc.
- **Advanced:** `LIGHT`, `SHADOW`, `LIFE`, etc.
- **Unstable:** `AVOID`, `TIME`, etc.
- **Scientific:** `METAL`, `ACID`, etc.

**Usage:** used primarily for categorizing `Essence` and defining spell requirements.

### 2.2. Class: `Essence`

**File:** `api/Essence.java`

Represents a specific magical substance. An element (like Fire) can have multiple essences (e.g., "Weak Fire Essence", "Concentrated Fire Essence").

- `id`: Unique string identifier (e.g., `"essence_fire_weak"`).
- `element`: The `RuneElement` it belongs to.
- `tier`: Power level (1, 2, 3...).
- `stability`: Modifier for fizzle rates (default 1.0).

### 2.3. Class: `GameResource`

**File:** `api/GameResource.java`

Represents a consumable stat required to cast spells.

- `id`: Unique identifier (e.g., `"mana"`, `"stamina"`).
- `max`: The maximum cap for this resource.
- `regenRate`: How fast it regenerates passively.
- `overflowPenalty`: What happens if you have too much (e.g., `"burnout"`).

### 2.4. Class: `RuneEffect`

**File:** `api/RuneEffect.java`

This is the **logic unit** of the system. A spell is composed of one or more effects.

- `id`: Identifier (e.g., `"damage_fire"`).
- `action`: An `IEffectAction` lambda that contains the code to run.
- `isInstant`: If true, happens immediately. If false, might be a status effect (DoT).

**Crucial Method:** `execute(CastContext ctx)` triggers the effect.

### 2.5. Class: `Spell`

**File:** `api/Spell.java`

The **Blueprint** for a magic ability. It connects costs to effects.

- **Costs:** Can require `GameResource` (Mana) and/or `Essence` items.
- **Conditions:** A predicate (logic check) that must be true to cast (e.g., "Must have a target").
- **Effects:** A list of `RuneEffect` IDs to trigger when cast.

### 2.6. Class: `CastContext`

**File:** `api/CastContext.java`

A transient object passed around during a single cast. It holds the "State" of the event.

- `source`: The caster (Player/Mob).
- `target`: The victim/location.
- `world`: The game world.
- `power`: A multiplier for effect strength.
- `metadata`: A map for sharing data between different effects in the same spell.

---

## 3. How to Create Magic (Step-by-Step)

### Step 1: Initialize the Core

Get the instance of the engine.

```java
RuneCore core = RuneCore.get();
```

### Step 2: Register Definitions

Define what your magic is made of.

```java
// Define Mana
core.registerResource(new GameResource("mana", 100, 1, "none"));

// Define Fire Essence
core.registerEssence(new Essence("fire_T1", RuneElement.FIRE, 1));
```

### Step 3: valid Logic (Effects)

Define what happens. **This is where you write actual game code.**

```java
RuneEffect explosion = new RuneEffect("explosion", true, 0, (ctx) -> {
    // 1. Get location from ctx.target
    // 2. Spawn particle
    // 3. Deal damage
    System.out.println("BOOM! Power: " + ctx.power);
});
core.registerEffect(explosion);
```

### Step 4: Create the Spell

Assemble the pieces.

```java
Spell fireball = new Spell("fireball", (ctx) -> ctx.target != null);

fireball.addCost("mana", 10);
fireball.addEssenceRequirement(RuneElement.FIRE, 1);
fireball.addEffect("explosion");

core.registerSpell(fireball);
```

### Step 5: Cast it!

When a player clicks a wand:

```java
CastContext ctx = new CastContext(player, target, world, 1.0);
core.castSpell("fireball", ctx);
```

---

## 4. The Event System (Hooks)

RuneCore has an event bus that allows external mods (or items) to modify spells **while they are being cast**.

**Key Events:**

1.  `spell:pre_cast`: Fires before anything happens. Use this to modify `ctx.power` or cancel the spell.
2.  `resource:consume`: Fires when mana is taken.
3.  `effect:applied`: Fires after every single effect.
4.  `spell:post_cast`: Fires when the spell is totally finished.

**Example: A "Fire Staff" that boosts damage**

```java
core.on("spell:pre_cast", (ctx) -> {
    // Check if source is holding fire staff
    // if (player.holds("fire_staff")) {
        ctx.power *= 1.5; // 50% more damage
        System.out.println("Staff boosted the spell!");
    // }
});
```
