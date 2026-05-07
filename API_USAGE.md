# 🛠️ RuneCore API Usage Guide

Welcome to the RuneCore development guide! This document explains how to use the RuneCore engine to create your own magical content for Hytale.

---

## 1. Getting Started

RuneCore is an **Entity-Component-System (ECS)** based engine. To use it, you generally need to register your systems in your Hytale plugin's entry point.

```java
public class MyMagicPlugin extends BasePlugin {
    @Override
    public void onEnable(EventRegistry eventRegistry) {
        // Register RuneCore systems
        eventRegistry.registerGlobal(EffectTimerListener.class);
        eventRegistry.registerGlobal(CastListener.class);
    }
}
```

---

## 2. Registering Essences

Essences are the "fuel" for your spells. Each essence is tied to one of the 20 elements.

```java
// Create a tier 1 Fire Essence
Essence fireEssence = new Essence("essence_fire", RuneElement.FIRE, 1);
RuneCore.get().registerEssence(fireEssence);
```

---

## 3. Creating Custom Status Effects

RuneCore features a robust **ActiveBuff** system. You can create effects that tick over time, have custom intervals, and clean themselves up automatically.

### Example: A Regeneration Effect
```java
RuneEffect regen = new RuneEffect("regeneration", 400) // 400 ticks duration
    .withAsset("runecore:Regeneration")
    .withBuff(ctx -> {
        // Generate a unique ID for this player's buff
        String uid = ctx.source.getUuid().toString();
        
        return ActiveBuff.builder(uid, "regeneration", 400)
            .interval(50) // Tick every 50ms (roughly every 1 tick)
            .onTick(ref -> EffectHelper.addHealth(ref, 1.0f)) // Heal on each tick
            .build();
    });

RuneCore.get().registerEffect(regen);
```

### Example: A Speed Boost
```java
RuneEffect speedBuff = new RuneEffect("speed", 1200)
    .withBuff(ctx -> {
        String uid = ctx.source.getUuid().toString();
        
        // Use EffectHelper to apply the stat change immediately
        EffectHelper.applySpeed(ctx.source.getReference(), 0.15f);
        
        return ActiveBuff.builder(uid, "speed", 1200)
            .onExpire(ref -> EffectHelper.revertSpeed(ref)) // Revert when done
            .build();
    });
```

---

## 4. Using the EffectHelper

The `EffectHelper` provides standardized methods for modifying entity stats and synchronizing them with the Hytale server.

*   **Health:** `addHealth(ref, amount)`, `subtractHealth(ref, amount)`
*   **Movement:** `applySpeed(ref, amount)`, `applySlowness(ref, amount)`, `revertSpeed(ref)`
*   **Mining:** `applyHaste(ref, amount)`, `revertHaste(ref)`

---

## 5. Casting Spells

You can group multiple effects into a single `Spell` and cast it using a `CastContext`.

```java
// Define the spell
Spell fireBlast = new Spell("fire_blast")
    .addCost("mana", 20)
    .addEffect("burn")
    .addEffect("instant_damage");

// Cast the spell
CastContext ctx = new CastContext(playerRef, targetRef, world, 1.0);
RuneCore.get().castSpell("fire_blast", ctx);
```

---

## 🔮 Pro Tips

1.  **Unique IDs:** Always use `playerRef.getUuid().toString()` as the UID for buffs to ensure they are correctly removed when the player disconnects.
2.  **Stat Reversion:** If you modify a stat (like speed), always specify an `onExpire` callback in your `ActiveBuff` to revert it.
3.  **Check Context:** Always check if `ctx.source` or `ctx.target` is null before applying effects.
