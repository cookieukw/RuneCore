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

## 🎮 How to Test In-Game

You can test the current status effects and spell system using the built-in administrative command.

### Apply a Status Effect
Use the following command to apply an effect to yourself:
```text
/rune effect <id>
```

**Common IDs for testing:**
- `speed` / `slowness`
- `high_jump`
- `poison` / `regeneration`
- `frozen`
- `invisibility` (Native Hytale effect)

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
