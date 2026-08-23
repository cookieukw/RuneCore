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

## 6. Combat Attributes

Combat attributes (armor, penetration, damage, ...) are **registrable data**, not a fixed list.
RuneCore ships eight of them; your mod can add its own and have it participate in combat.

### Reading and writing

`RuneAttributes` is the entry point. Every method is null-safe and returns `Optional` rather
than throwing during a damage event.

```java
// built-ins
RuneAttributes.of(playerUuid).ifPresent(attrs -> {
    float armor = attrs.get(CoreAttributes.ARMOR);
    attrs.setBase(CoreAttributes.MAGIC_RESIST, 25f);
});

// from an entity ref or a PlayerRef
RuneAttributes.of(entityRef).ifPresent(attrs -> ...);
```

### Base values vs modifiers

A resolved attribute is `base + sum(modifiers)`, clamped to the attribute's declared bounds.
Modifiers are named, so they can be removed exactly — this is how equipment applies and undoes
its bonuses.

```java
attrs.setBase(CoreAttributes.ARMOR, 10f);
attrs.addModifier("mymod:blessing", CoreAttributes.ARMOR, 5f);   // resolves to 15
attrs.removeModifier("mymod:blessing");                          // back to 10
```

Registering the same modifier id again **replaces** it rather than stacking, so re-applying on
every equipment change is safe.

### Declaring your own attribute

```java
public static final RuneAttribute LIFESTEAL =
        AttributeRegistry.register(RuneAttribute.fraction("mymod:lifesteal", 1f));

// positive() → 0..∞, fraction(max) → 0..max
```

Ids are namespaced and lower-cased. Registering an id someone else already took throws
`IllegalStateException` — silently replacing it would corrupt their damage maths.

---

## 7. Damage Pipeline

RuneCore cannot know what your attribute *means*, so behaviour is contributed rather than
inferred: register the attribute, then register a stage that reads it.

```java
DamagePipeline.register("mymod:crit", DamagePipeline.AFTER_MITIGATION, (ctx, damage) ->
        ThreadLocalRandom.current().nextFloat() < ctx.attacker().get(CRIT_CHANCE)
                ? damage * 2f
                : damage);
```

### Priorities

| Anchor | When it runs |
| :--- | :--- |
| `BEFORE_MITIGATION` | before armor/resist — flat changes to the incoming amount |
| `MITIGATION` | where RuneCore's own armor/resist/shield maths happens |
| `AFTER_MITIGATION` | multiplicative effects such as crits |
| `FINAL` | last word on the number |

Lower runs earlier; any `int` works if you need to sit between two anchors.

### Things worth knowing

- **In PvP, `BEFORE_MITIGATION` stages are ignored.** That path derives damage from the
  attacker's stats and weapon instead of scaling the engine's number, so there is nothing for an
  earlier stage to modify. Stages at `AFTER_MITIGATION` and later always apply.
- **A stage that throws is logged and skipped**, never allowed to abort the hit — a broken stage
  must not make players invulnerable.
- Stages run on the thread that raised the damage event. Keep them cheap and non-blocking.
- Registering the same id twice replaces the stage, so reloading your content will not stack it.

---

## 8. Registering Items and Creatures

```java
// a weapon: contributes while held, at the moment of the hit
RuneAttributes.registerItem("MyMod_Blade",
        ItemCombatData.builder().physicalDamage(30f).armorPenetration(5f).build());

// armour: contributes while equipped
RuneAttributes.registerItem("MyMod_Plate",
        ItemCombatData.builder().armor(20f).magicResist(8f).build());

// a creature: how it deals damage, and how it takes it
RuneAttributes.registerCreature("MyBoss",
        CreatureCombatData.magic(20f).withDefense(35f, 40f, 0.1f));
```

The creature key is the model asset's file name, without path or namespace — the same thing the
damage interceptor parses at runtime. A creature that is not registered is left alone entirely.

Both methods return `false` when the registry is not up yet, so you can log or retry instead of
guessing.

---

## 9. Interactive Item Manager

RuneCore provides a generic, unified API to add clickable (interactive) items to your mod, eliminating the need to write repetitive codecs or create hundreds of `RootInteractions` JSON files.

### How to use in Hytale:
Simply create the full JSON for your item (`Items/MyItem.json`) and reference RuneCore's generic interaction in the interaction block:
```json
{
  "DisplayName": "Magic Book",
  "Model": "Items/Consumables/Potions/ItemMagicBook.blockymodel",
  "MaxStackSize": 1,
  "Interactions": {
    "Secondary": "RuneCore_GenericItemUse"
  }
}
```

### How to register in code:
In your plugin/mod's `onEnable()` or `init()`, call `RuneCoreItemManager`:
```java
RuneCoreItemManager.register("MyItem", (player, playerRef) -> {
    playerRef.sendMessage(Message.raw("You clicked my custom item!"));
    // You can open UIs, consume the item from the inventory, etc., here.
});
```
The manager automatically detects which item the player is holding (by reading the ID suffix) and executes the corresponding code block!

---

## 🔮 Pro Tips

1.  **Unique IDs:** Always use `playerRef.getUuid().toString()` as the UID for buffs to ensure they are correctly removed when the player disconnects.
2.  **Stat Reversion:** If you modify a stat (like speed), always specify an `onExpire` callback in your `ActiveBuff` to revert it.
3.  **Check Context:** Always check if `ctx.source` or `ctx.target` is null before applying effects.
4.  **Namespace everything:** attribute ids, modifier ids and pipeline stage ids are all global. Prefix them with your mod id (`mymod:`) so you cannot collide with RuneCore or another mod.
5.  **Modifiers over base values:** if your effect is temporary, use `addModifier`/`removeModifier` rather than writing the base — otherwise you have to remember the previous value to restore it.
