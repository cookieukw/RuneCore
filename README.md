# 🔮 RuneCore: Magic Engine for Hytale

[Leia em português](README-PTBR.md) | [API Guide](API_USAGE.md) | [Technical Docs](docs/ELEMENTS.md) | [Manual](RuneCore_Manual.md)

> [!IMPORTANT]
> **Project Status: Under Development**
>
> - 🛠️ **In Progress:** Drop system and essence drop mechanics are currently being developed.
> - ✅ **Functional:** Core commands and player status management system are fully operational.
> - 🧪 **API:** The API is currently in the testing phase.
> - 🎨 **Visuals:** The project does not have a custom icon yet. Essences currently use temporary pixel art icons.
> - 🚀 **Next Steps:** Implementing 3D models for essences by recycling existing in-game assets.
>
> **Note for testing:** If you wish to fully test the mod, please uncomment the system registrations in the main plugin file (`ExamplePlugin.java`).

---

## 1. Vision & Origin 🤔

RuneCore was born from the desire to bring a deep, meaningful magic system to Hytale. While the native system provides a basic foundation, RuneCore expands it into a fully-fledged engine that modders can use to create complex elemental interactions, persistent status effects, and rich magical progression.

Our goal is not just to provide a mod, but an **extensible API** that serves as the backbone for the Hytale magic community.

## 2. What is RuneCore? 📘

RuneCore is a modular magic system engine. It is divided into interdependent modules:

*   **🔹 RuneCore (Core):** Manages essences, mana, and player progress. Provides the API for other modders.
*   **⚔️ RuneMagic:** Focused on spells, runes (passive effects), artifacts, and grimoires.
*   **⚗️ RuneAlchemy:** A chemical and alchemical system for creating potions and enchanting items using essences.

## 3. Core Features ✨

*   **20 Elements:** Divided into Basic, Advanced, Unstable, and Chemical tiers.
*   **Modular API:** Easily register custom essences, spells, and status effects.
*   **Persistent Status Effects:** A robust system for ticking buffs/debuffs (e.g., Poison, Regeneration, Frozen) with world-aware logic.
*   **Resource Management:** Custom mana, stamina, and biological resource tracking.

For a full breakdown of all 20 elements and their mechanics, see our [**Technical Documentation**](docs/ELEMENTS.md).

---

## 🎮 How to Test In-Game & Current Status Effects

You can test the registered status effects and spell system using the built-in administrative command:

```text
/rune effect <id>
```

Below is the complete table of effects currently registered in the `RuneCore` engine, their development states, and the expected behavior of each:

| Status | Effect ID | Has Native/JSON Visual? | What it should do |
| :---: | :--- | :--- | :--- |
| [x] | `speed` | Speed | Gives movement speed buff. |
| [x] | `slowness` | Slowness | Slows down the entity. |
| [ ] | `haste` | Haste | Modifies Attack Speed and Mining Speed (+50%) and shows UI. (Attack/Mining Speed pending) |
| [ ] | `mining_fatigue`| Mining_Fatigue | Modifies Attack Speed and Mining Speed (-70%) and shows UI. (Attack/Mining Speed pending) |
| [x] | `jump_boost` | Jump_Boost | Jump higher. |
| [x] | `high_jump` | High_Jump | Jump much higher. |
| [x] | `slow_falling` | Slow_Falling | Slow falling. |
| [x] | `levitation` | Levitation | Causes the entity to float upwards. |
| [x] | `regeneration` | Regeneration | Heals +1 health every 50 ticks. |
| [x] | `poison` | Poison | Deals 1 health damage every 25 ticks. |
| [x] | `decay` | Decay | Deals 1 health damage every 40 ticks. |
| [x] | `burn` | Burn | Deals 1 health damage every 20 ticks + UI. |
| [x] | `nausea` | Nausea | Rotates the camera (NauseaTick) + UI. |
| [x] | `bleeding` | Bleeding | Deals 1 health damage every 20 ticks + UI + Custom blood particles. |
| [x] | `frozen` | Frozen | Prevents movement temporarily. |
| [x] | `instant_health`| (none) | Instant healing (4.0 * power). |
| [x] | `instant_damage`| InstantDamage | Instant damage (6.0 * power). |
| [ ] | `damage_fire_instant`| DamageFireInstant | Instant fire damage (10.0 * power). |
| [x] | `invisibility` | Invisibility | Hides the player from others. (Fine-tuning of own visibility pending) |
| [ ] | `glowing` | Glowing | Adds dynamic light (DynamicLight) + UI. (Does not persist through logout/relog) |
| [x] | `blindness` | Blindness | Modifies vision (VisualEffectHelper) + UI. |
| [x] | `night_vision` | NightVision | White dynamic light around the player + UI. |
| [ ] | `water_breathing`| WaterBreathing | Allows native underwater breathing. (Simply does not work) |
| [x] | `fire_resistance`| FireResistance | Native fire resistance. |
| [ ] | `resistance` | Resistance | Native resistance. (Does not work, needs improvements) |
| [ ] | `strength` | Strength | Native strength. (Does not work, needs improvements) |
| [ ] | `weakness` | Weakness | Native weakness. (Does not work, needs improvements) |

### Implementation Note
To enable all systems during development, ensure they are registered in your entry point:
```java
// In your plugin class
eventRegistry.registerGlobal(EffectTimerListener.class);
eventRegistry.registerGlobal(CastListener.class);
```

---

## 🛠️ Modder's Guide

Interested in building on top of RuneCore? Check out our [**API Usage Guide**](API_USAGE.md) for code examples and integration steps.
