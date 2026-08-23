# RuneCore

Extensible magic system engine and framework for Hytale mods.

[Leia em português](README-PTBR.md) | [API Guide](API_USAGE.md) | [API Reference](docs/API_REFERENCE.md) | [Technical Docs](docs/ELEMENTS.md) | [Manual](RuneCore_Manual.md)

<p align="center">
  <img src="docs/assets/banner.png" alt="RuneCore Banner" width="100%">
</p>

<p align="center">
  <img src="icons/logo/runecore-logo.png" alt="RuneCore Logo" height="180">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="icons/logo/runecore-logo-construction.png" alt="RuneCore Layered Logo" height="180">
</p>

> **Project Status: In Development**
>
> - **In Progress:** Potions and crafting recipes for custom status effects.
> - **Functional:** Core commands, player status management, and essence drop system.
> - **API:** Testing phase.
> - **Visuals:** Custom logo and essence icons. 3D models use recycled game assets.
> - **Next Steps:** Full **RuneAlchemy** system and potion brewing pipeline.

---

## 1. Overview

RuneCore expands Hytale's native foundation into a modular engine for elemental interactions, status effects, custom RPG attributes, and alchemy recipes. The mod offers both ready-to-use mechanics and an extensible API for integration with other mods.

## 2. Visual Gallery

### Elemental Essences
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/essences/Ingredient_Fire_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Water_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Earth_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Wind_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Ice_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Lightning_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Life_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Death_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Light_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Shadow_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Mind_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Blood_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Chaos_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Aether_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Void_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Time_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Metal_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Crystal_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Poison_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Acid_Essence.png" height="48"> |

### Potions
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/potions/Potion_Drinkable_Speed.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Slowness.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Haste.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Mining_Fatigue.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Jump_Boost.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_High_Jump.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Slow_Falling.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Levitation.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Regeneration.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Poison.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Decay.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Burn.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Nausea.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Bleeding.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Frozen.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Instant_Health.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Instant_Damage.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Invisibility.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Glowing.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Blindness.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Night_Vision.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Water_Breathing.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Fire_Resistance.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Resistance.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Strength.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Weakness.png" height="48"> | | | | |

### Status Effects
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/128x/speed.png" height="48"> | <img src="icons/128x/slowness.png" height="48"> | <img src="icons/128x/haste.png" height="48"> | <img src="icons/128x/mining_fatigue.png" height="48"> | <img src="icons/128x/jump_boost.png" height="48"> |
| <img src="icons/128x/high_jump.png" height="48"> | <img src="icons/128x/slow_falling.png" height="48"> | <img src="icons/128x/levitation.png" height="48"> | <img src="icons/128x/regeneration.png" height="48"> | <img src="icons/128x/poison.png" height="48"> |
| <img src="icons/128x/decay.png" height="48"> | <img src="icons/128x/burn.png" height="48"> | <img src="icons/128x/nausea.png" height="48"> | <img src="icons/128x/bleeding.png" height="48"> | <img src="icons/128x/frozen.png" height="48"> |
| <img src="icons/128x/invisibility.png" height="48"> | <img src="icons/128x/glowing.png" height="48"> | <img src="icons/128x/blindness.png" height="48"> | <img src="icons/128x/night_vision.png" height="48"> | <img src="icons/128x/water_breathing.png" height="48"> |
| <img src="icons/128x/fire_resistance.png" height="48"> | <img src="icons/128x/resistance.png" height="48"> | <img src="icons/128x/strength.png" height="48"> | <img src="icons/128x/weakness.png" height="48"> | <img src="icons/128x/darkness.png" height="48"> |
| <img src="icons/128x/electrified.png" height="48"> | | | | |

### In-Game Effect Showcase

#### Bleeding
Deals damage over time and applies a bloody visual filter to the player.
<br>
<img src="docs/assets/screenshots/bleeding.png" alt="Bleeding Effect" width="100%">

---

#### Burn
Ignites the target, causing fire damage and burning screen overlays.
<br>
<img src="docs/assets/screenshots/burn.png" alt="Burn Effect" width="100%">

---

#### Decay
Corrodes the entity's health over time with necrotic visual effects.
<br>
<img src="docs/assets/screenshots/decay.png" alt="Decay Effect" width="100%">

---

#### Fire Resistance
Grants immunity to fire damage and protects the entity in lava environments.
<br>
<img src="docs/assets/screenshots/fire_resistance.png" alt="Fire Resistance Effect" width="100%">

---

#### Frozen
Encases the target in ice, locking physical movement and rotation.
<br>
<img src="docs/assets/screenshots/frozen.png" alt="Frozen Effect" width="100%">

---

#### Glowing
Emits dynamic light around entities and players in low-light environments.
<br>
<img src="docs/assets/screenshots/glowing_entity.png" alt="Glowing Entity Effect" width="100%">

---

#### High Jump
Boosts vertical velocity, launching players and entities into high jumps.
<br>
<img src="docs/assets/screenshots/high_jump.png" alt="High Jump Effect" width="100%">

---

#### Normal Health
Default player health status on HUD.
<br>
<img src="docs/assets/screenshots/full_health.png" alt="Normal Health" width="100%">

---

#### Instant Damage
Instantly subtracts health points upon hit or potion splash.
<br>
<img src="docs/assets/screenshots/instant_damage.png" alt="Instant Damage Effect" width="100%">

---

#### Instant Health
Instantly restores health points and updates player health.
<br>
<img src="docs/assets/screenshots/instant_health.png" alt="Instant Health Effect" width="100%">

---

#### Blindness
Darkens the player's view and HUD completely, simulating loss of vision.
<br>
**Normal Vision:**
<br>
<img src="docs/assets/screenshots/normal_vision.png" alt="Normal Vision" width="100%">
<br><br>
**Blinded View:**
<br>
<img src="docs/assets/screenshots/blindness.png" alt="Blindness Effect" width="100%">

---

#### Levitation
Lifts entities into the air with anti-gravity physics, followed by a fall upon expiry.
<br>
**Floating Upward:**
<br>
<img src="docs/assets/screenshots/levitation_rising.png" alt="Levitation Rising" width="100%">
<br><br>
**Descending / Falling:**
<br>
<img src="docs/assets/screenshots/levitation_falling.png" alt="Levitation Falling" width="100%">

---

#### Nausea
Causes severe camera sway and screen disorientation, cycling view angles.
<br>
<img src="docs/assets/screenshots/nausea_1.png" alt="Nausea Stage 1" width="100%">
<br><br>
<img src="docs/assets/screenshots/nausea_2.png" alt="Nausea Stage 2" width="100%">
<br><br>
<img src="docs/assets/screenshots/nausea_3.png" alt="Nausea Stage 3" width="100%">

---

#### Night Vision
Illuminates dark environments by dynamically adjusting ambient light levels.

> **Note on Engine Limitations:**
> Due to current Hytale Server API limitations, dynamic light components affect global scene lighting around the target entity rather than applying a per-client post-processing shader.

<br>
**Night Environment (Without Effect):**
<br>
<img src="docs/assets/screenshots/night_darkness.png" alt="Dark Night" width="100%">
<br><br>
**Night Vision Active:**
<br>
<img src="docs/assets/screenshots/night_vision.png" alt="Night Vision Active" width="100%">

---

#### Poison
Deals continuous poison damage over time, turning health indicators green until expired.
<br>
**Poison Active:**
<br>
<img src="docs/assets/screenshots/poison_active.png" alt="Poison Active" width="100%">
<br><br>
**After Effect Expires:**
<br>
<img src="docs/assets/screenshots/poison_ended.png" alt="Poison Ended" width="100%">

---

#### Regeneration
Heals health points over time, restoring player health periodically.
<br>
<img src="docs/assets/screenshots/regeneration.png" alt="Regeneration Effect" width="100%">

---

## 3. Architecture

RuneCore is split into three main modules:

* **RuneCore (Core):** Manages essences, mana, combat attributes, and player progression. Exposes the core API.
* **RuneMagic:** Spells, runes (passive effects), artifacts, and grimoires.
* **RuneAlchemy:** Brewing system for potions, reagents, and item enchantments using essences.

---

## 4. Loot Tables & Drop Rates

Creatures drop elemental essences when defeated by a player (default base drop rate: **25%**).

| Icon | Essence | Source Mobs |
| :---: | :--- | :--- |
| <img src="icons/essences/Ingredient_Fire_Essence.png" height="32"> | **Fire** | Emberwulf, Fire Dragon, Magma/Flame creatures |
| <img src="icons/essences/Ingredient_Earth_Essence.png" height="32"> | **Earth** | Trork, Earth Golem, Bison, Tortoise, Molerat |
| <img src="icons/essences/Ingredient_Wind_Essence.png" height="32"> | **Wind** | Birds (Hawk, Owl, Crow, etc.), Feran Windwalker |
| <img src="icons/essences/Ingredient_Water_Essence.png" height="32"> | **Water** | Fish (Shark, Piranha, etc.), Crab, Frog, Whale |
| <img src="icons/essences/Ingredient_Ice_Essence.png" height="32"> | **Ice** | Polar Bear, Frost Dragon, Yeti, Frost Skeleton |
| <img src="icons/essences/Ingredient_Lightning_Essence.png" height="32"> | **Lightning** | Thunder Golem, Thunder Spirit, Living Spark |
| <img src="icons/essences/Ingredient_Light_Essence.png" height="32"> | **Light** | Spirit Root, Christmas Kweebec |
| <img src="icons/essences/Ingredient_Shadow_Essence.png" height="32"> | **Shadow** | Shadow Knight, Wraith, Skrill |
| <img src="icons/essences/Ingredient_Life_Essence.png" height="32"> | **Life** | Animals (Cow, Pig, Sheep, Deer), Kweebec |
| <img src="icons/essences/Ingredient_Death_Essence.png" height="32"> | **Death** | Skeleton, Zombie, Ghoul |
| <img src="icons/essences/Ingredient_Mind_Essence.png" height="32"> | **Mind** | Slothian, Outlander Sorcerer |
| <img src="icons/essences/Ingredient_Blood_Essence.png" height="32"> | **Blood** | Bat, Mosquito |
| <img src="icons/essences/Ingredient_Chaos_Essence.png" height="32"> | **Chaos** | Outlander Berserker, Trork Chieftain |
| <img src="icons/essences/Ingredient_Aether_Essence.png" height="32"> | **Aether** | Ember Spirit |
| <img src="icons/essences/Ingredient_Void_Essence.png" height="32"> | **Void** | Void-corrupted creatures |
| <img src="icons/essences/Ingredient_Metal_Essence.png" height="32"> | **Metal** | Firesteel Golem, Tank, Turret |
| <img src="icons/essences/Ingredient_Crystal_Essence.png" height="32"> | **Crystal** | Crystal Golem, Scarak |
| <img src="icons/essences/Ingredient_Poison_Essence.png" height="32"> | **Poison** | Snake, Spider, Scorpion |

---

## 5. Key Features

- **20 Elements:** Categorized into Basic, Advanced, Unstable, and Chemical tiers.
- **Modular API:** Direct registration for custom essences, spells, and status effects.
- **Persistent Status Effects:** Ticking buff/debuff system (Poison, Regeneration, Frozen, Bleeding, etc.) supporting custom world logic.
- **Resource Management:** Player stat trackers for mana, stamina, and custom attributes.

For element details and design notes, refer to [**ELEMENTS.md**](docs/ELEMENTS.md).

---

## 6. Potion Recipes

Potions are crafted at the **Alchemy Bench** using **Glass Bottle** (`Potion_Empty`) + **Elemental Essence** + **Secondary Material** (base craft time: 4 seconds).

| Potion | Essence | Qty | Secondary Material | Qty |
| :--- | :--- | :---: | :--- | :---: |
| **Speed** | Wind Essence | 1 | Feathers (Light) | 2 |
| **Slowness** | Earth Essence | 1 | Bramble Moss | 2 |
| **Haste** | Lightning Essence | 1 | Crystal (Yellow) | 1 |
| **Mining Fatigue** | Earth Essence | 1 | Bone Fragment | 2 |
| **Jump Boost** | Wind Essence | 1 | Feathers (Blue) | 2 |
| **High Jump** | Aether Essence | 1 | Feathers (Red) | 2 |
| **Slow Falling** | Wind Essence | 1 | Feathers (Dark) | 3 |
| **Levitation** | Aether Essence | 2 | Crystal (White) | 2 |
| **Regeneration** | Life Essence | 2 | Pink Flower | 3 |
| **Poison** | Poison Essence | 1 | Poison Mushroom | 2 |
| **Decay** | Death Essence | 1 | Bone Fragment | 3 |
| **Burn** | Fire Essence | 1 | Charcoal | 2 |
| **Nausea** | Chaos Essence | 1 | Green Mushroom | 2 |
| **Bleeding** | Blood Essence | 1 | Crystal (Red) | 1 |
| **Frozen** | Ice Essence | 1 | Crystal (Cyan) | 2 |
| **Instant Health** | Life Essence (Concentrated) | 1 | Red Flower | 3 |
| **Instant Damage** | Void Essence | 1 | Boom Powder | 2 |
| **Invisibility** | Shadow Essence | 2 | Crystal (White) | 2 |
| **Glowing** | Light Essence | 1 | Glowing Mushroom (Orange) | 2 |
| **Blindness** | Shadow Essence | 1 | Crystal (Purple) | 2 |
| **Night Vision** | Light Essence | 1 | Glowing Mushroom (Blue) | 2 |
| **Water Breathing** | Water Essence | 1 | Blue Coral | 3 |
| **Fire Resistance** | Fire Essence | 2 | Crystal (Red) | 2 |
| **Resistance** | Metal Essence | 2 | Crystal (Blue) | 2 |
| **Strength** | Blood Essence | 2 | Crystal (Red) | 2 |
| **Weakness** | Death Essence | 1 | Grey Flower | 2 |

*Note: Advanced effects require 2 essences.*

---

## 7. Combat Stats System

RuneCore implements an RPG damage pipeline on top of native combat calculations.

Custom attributes are registrable. Players carry persistent stat blocks, while creatures pull stats from static registry lookups.

### Stat Types

| Category | Stat | Description |
|----------|------|-------------|
| **Offensive** | Physical Damage | Reduced by target Armor |
| **Offensive** | Magic Damage | Reduced by target Magic Resist |
| **Offensive** | True Damage | Ignores reductions (blocked by shields) |
| **Offensive** | Armor Penetration | Ignores a portion of target Armor |
| **Offensive** | Magic Penetration | Ignores a portion of target Magic Resist |
| **Defensive** | Armor | Reduces incoming Physical Damage |
| **Defensive** | Magic Resist | Reduces incoming Magic Damage |
| **Defensive** | Damage Reduction | Flat % reduction on incoming damage (capped at 90%) |
| **Defensive** | Shield HP | Temporary hit points consumed before base health |

### Damage Formula

```text
effectiveDefense = max(0, defense - penetration)
reducedDamage = rawDamage × 100 / (100 + effectiveDefense)
finalDamage = (physReduced + magReduced) × (1 - damageReduction%) + trueDamage
```

Hits run through the `DamagePipeline`, allowing external mods to insert custom modifiers (e.g. crit, lifesteal).

### Creature Damage (PvE)

Creatures pull profiles from `CreatureCombatRegistry` when attacking or taking damage. Unregistered creatures fall back to standard `DamageCause` checks.

| Damage Profile | Formula | Examples |
|---------------|---------|----------|
| **Physical** | Reduced by Armor; may use Armor Pen | Trork, Skeleton Fighter, Wolf, Bear |
| **Magic** | Reduced by Magic Resist; may use Magic Pen | Skeleton Mage, Wraith, Necromancer |
| **Hybrid** | Split physical/magic ratio, calculated separately | Fire Dragon, Golem Crystal Flame |
| **True** | Bypasses armor/resistances (absorbed by shields) | — |

#### Creature Defenses by Family

| Family | Armor | Magic Resist | DR | Notes |
| :--- | ---: | ---: | ---: | :--- |
| Wildlife | 2 | 0 | — | Unarmored base creatures |
| Zombies | 4 | 2 | — | Low physical/magic resistance |
| Spirits | 4 | 32 | — | Incorporeal (high magic resist) |
| Goblins | 5 | 3 | — | Light leather armor |
| Trorks | 6 | 0 | — | Physical melee focus |
| Ferans | 8 | 8 | — | Balanced stats |
| Skeletons | 10 | 4 | — | Moderate armor |
| Outlanders | 12 | 6 | — | Geared raiders |
| Void | 12 | 28 | — | High magic resistance |
| Scaraks | 18 | 4 | — | Chitin plating (high armor, low magic resist) |
| Dragons | 32 | 28 | — | High resistance base |
| Golems | 34 | 12 | — | High armor base |
| Bosses | 40 | 34 | 15% | High defenses + flat % reduction |

### Equipment Integration

Armors apply stats while equipped (scaled by slot: Chest 100%, Legs 85%, Head 70%, Hands 50%). Weapons apply offensive stats on hit.

#### Armor Scaling by Tier

| Tier | Materials | Armor (Chest) | Magic Resist (Chest) |
|------|-----------|:-------------:|:--------------------:|
| 1 | Cloth, Wood | 4–8 | 10–20 |
| 2 | Light Leather, Copper | 10–14 | 8–10 |
| 3 | Heavy Leather, Bronze, Iron | 18–25 | 5–8 |
| 4 | Steel, Cobalt | 30–35 | 5–7 |
| 5 | Mithril, Thorium | 42–45 | 12–14 |
| 6 | Adamantite, Onyxium, Prisma | 50–58 | 18–22 |

#### Weapon Damage by Tier (Physical Sample)

| Material | Sword | Longsword | Axe | Battleaxe | Dagger |
|----------|:-----:|:---------:|:---:|:---------:|:------:|
| Wood | 6–8 | 12 | 10 | 14 | 6 |
| Copper | 14 | 20 | 16 | 22 | 10 |
| Iron | 22 | 30 | 24 | 32 | 18 |
| Steel | 28 | — | — | 34 | — |
| Mithril | 38 | 50 | 40 | 52 | 30 |
| Adamantite | 50 | 65 | 52 | 68 | 40 |

### Admin Commands

```text
/combatstats view
/combatstats set <stat> <value>
/combatstats add <stat> <value>
/combatstats reset [all|modifiers|<stat>]
```

Supported identifiers: `armor`, `magicresist` (`mr`), `reduction` (`dr`), `physdmg` (`phys`), `magdmg` (`mag`), `truedmg` (`true`), `armorpen` (`apen`), `magicpen` (`mpen`), `shield`.

---

## 8. Status Effects & Testing

In-game testing command:

```text
/rune effect <id>
```

| Icon | Status | Effect ID | Native Visual | Description |
| :---: | :---: | :--- | :--- | :--- |
| <img src="icons/128x/speed.png" height="32"> | [x] | `speed` | Speed | Movement speed boost. |
| <img src="icons/128x/slowness.png" height="32"> | [x] | `slowness` | Slowness | Reduces movement speed. |
| <img src="icons/128x/haste.png" height="32"> | [ ] | `haste` | Haste | Attack/Mining speed modifier (+50%). |
| <img src="icons/128x/mining_fatigue.png" height="32"> | [ ] | `mining_fatigue`| Mining_Fatigue | Attack/Mining speed reduction (-70%). |
| <img src="icons/128x/jump_boost.png" height="32"> | [x] | `jump_boost` | Jump_Boost | Increases jump height. |
| <img src="icons/128x/high_jump.png" height="32"> | [x] | `high_jump` | High_Jump | Significantly increases jump height. |
| <img src="icons/128x/slow_falling.png" height="32"> | [x] | `slow_falling` | Slow_Falling | Decreases fall velocity. |
| <img src="icons/128x/levitation.png" height="32"> | [x] | `levitation` | Levitation | Causes upward float. |
| <img src="icons/128x/regeneration.png" height="32"> | [x] | `regeneration` | Regeneration | Heals 1 HP every 50 ticks. |
| <img src="icons/128x/poison.png" height="32"> | [x] | `poison` | Poison | Deals 1 HP damage every 25 ticks. |
| <img src="icons/128x/decay.png" height="32"> | [x] | `decay` | Decay | Deals 1 HP damage every 40 ticks. |
| <img src="icons/128x/darkness.png" height="32"> | [x] | `darkness` | Darkness | Reduces vision range/brightness. |
| <img src="icons/128x/electrified.png" height="32"> | [x] | `electrified` | Electrified | Periodic electric damage and visual particles. |
| <img src="icons/128x/burn.png" height="32"> | [x] | `burn` | Burn | Deals 1 HP damage every 20 ticks. |
| <img src="icons/128x/nausea.png" height="32"> | [x] | `nausea` | Nausea | Applies screen rotation effect. |
| <img src="icons/128x/bleeding.png" height="32"> | [x] | `bleeding` | Bleeding | Deals 1 HP damage every 20 ticks + blood particles. |
| <img src="icons/128x/frozen.png" height="32"> | [x] | `frozen` | Frozen | Prevents movement for duration. |
| | [x] | `instant_health`| (none) | Instant heal (`4.0 * power`). |
| | [x] | `instant_damage`| InstantDamage | Instant damage (`6.0 * power`). |
| <img src="icons/128x/invisibility.png" height="32"> | [x] | `invisibility` | Invisibility | Conceals entity visual model. |
| <img src="icons/128x/blindness.png" height="32"> | [x] | `blindness` | Blindness | Restricts camera vision overlay. |
| <img src="icons/128x/night_vision.png" height="32"> | [x] | `night_vision` | NightVision | Applies night vision visual light. |
| <img src="icons/128x/fire_resistance.png" height="32"> | [x] | `fire_resistance`| FireResistance | Prevents fire damage. |

### API Usage Example

```java
RuneCore core = RuneCore.getInstance();
RuneEffect poison = core.getEffect("poison");

if (poison != null) {
    CastContext ctx = new CastContext(sourceEntity, targetEntity);
    poison.execute(ctx);
}
```

---

## 9. Build Setup & Tooling

Set up project paths before compiling:

### `local.properties`
```properties
hytale.assets.path=/path/to/Hytale/Assets.zip
hytale.mods.dest=/path/to/Hytale/Mods/
```

### `gradle.properties`
```properties
org.gradle.java.home=/path/to/Hytale/jdk-25
```

### Build Commands
- **Build Jar:** `./gradlew jar`
- **Engine Version:** `./gradlew hytaleVersion`
- **Extract Schemas:** `./gradlew generateSchemas`
- **Validate Assets:** `./gradlew validateAssets`
- **Validate Prefabs:** `./gradlew validatePrefabs`

---

## 10. Developer Guide & Entry Points

Main classes for API integration:

| Entry Point | Usage |
| :--- | :--- |
| `RuneAttributes` | Read/write attributes, register items and creatures |
| `AttributeRegistry` + `RuneAttribute` | Declare custom stats |
| `DamagePipeline` + `DamageStage` | Custom damage calculation steps |
| `RuneCore` | Access core elements, spells, and status effects |
| `RuneCoreItemManager` | Clickable/interactive item registration |

Refer to [**API_USAGE.md**](API_USAGE.md) for step-by-step examples.

---

## 11. License

Licensed under [Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](https://creativecommons.org/licenses/by-nc/4.0/).
Icons located under `/icons` follow the same license terms.

