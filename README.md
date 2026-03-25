# 🔮 RuneCore: Design Document

[Leia em português](README-PTBR.md)

## 1. Where did it come from? 🤔

When Hytale announced that the game would have a magic system, I immediately became interested, as I've never seen a game of this genre implement it well. However, we learned that Hytale's magic was extremely basic and wouldn't be explored that much.

Many people were disappointed because it wouldn't be as cool as expected, but for me, this was more than an opportunity to create such an interesting system. Just as it's being with the energy system, where one of the most promising automation mods for Hytale is **HyEnergy**, by Seyager. I also want to bring not just a promise, but also a great example of what the community can do.

---

## 2. What is RuneCore? 📘

First, we need to understand that RuneCore is a magic system for Hytale. It serves to solve the lack of depth in the original system.

### 🏗️ System Structure

The project is divided into interdependent modules:

#### 🔹 RuneCore (Core)

A core system, meaning a primary system.

- **Function:** It will have an essence system, manages the mana system and player progress.
- **API:** It doesn't do anything on its own. However, it creates an API (resources) so that other modders can use it as a dependency. The entire system revolves around RuneCore.

#### ⚔️ RuneMagic

Focused on abilities and combat.

- **Spells:** Abilities, effects, etc.
- **Runes:** Passive effects (meaning they don't need to be activated more than once).
- **Artifacts:** Items that give you extra abilities.
- **Grimoires:** Books with unique abilities or a set of abilities.
- **New resources.**

#### ⚗️ RuneQuimic (RuneAlchemy)

An alchemy system.

- **Content:** Consists of adding some chemical elements, alchemy apparatus, potions, and enchantment books.
- **Base:** Based precisely on essences.

---

## 3. Essence System ✨

**What are these essences?**
Well, these essences are practically souls and, in some cases, materials.

**Obtainment methods:**

- ⛏️ Resource gathering.
- 🏰 Conquering dungeons and zones.
- ⚔️ Killing specific monsters and mobs.
- 🧪 Through alchemy.

---

## 4. Elements 🌎

Souls are based on elements. Usually, mods or games in this genre always rely on 4 elements (water, fire, wind, and earth), occasionally including ether, light, or shadow. But this mod won't be like that. **We will have 20 elements.**

### Element Classification

| Basic Level | Advanced Level | Unstable Elements | Chemical Elements |
| :----------: | :------------: | :-----------------: | :----------------: |
|🔥 Fire|☀️ Light|🌀 Chaos|⚙️ Metal|
|🪨 Earth|🌑 Shadow|✨ Ether|💎 Crystal|
|💨 Wind|🌿 Life|🕳️ Void|🧪 Poison|
|💧 Water|☠️ Death|⏳ Time|🧴 Acid|
|❄️ Ice|🧠 Mind|||
|⚡ Lightning|🩸 Blood|||

---

## 5. Element Details

### 🔥 Fire

**Concept:** Continuous damage and spatial control (environmental alteration) element.

> **Functional Definition:** It acts by altering environmental and target conditions, reducing direct aggressiveness and preparing reactions with other elements. (Note: The original functional definition text seemed to be for Water; adjusted for Fire based on context or kept as original if consistent - *Original Fire did not have an explicit functional definition like others, assuming concept description*)

**Damage Type:**

- Continuous damage (DoT) applied over time.
- Area of Effect (AoE) damage with terrain persistence.
- Part of the damage ignores physical mitigation (burn penetration).

**Scaling:** Scales better with effect duration than with raw power (burst).
**Playstyle:** Doesn't focus on immediate explosion, but on damage accumulation and area denial.

#### ✅ Advantages

- High efficiency against groups.
- Maintains damage even without continuous player action.
- Reduces enemy mobility by altering terrain.
- Direct synergy with flammable effects (oil, wood, gases).
- Forces the enemy to reposition.

#### ❌ Disadvantages

- Low efficiency against high-resistance single targets.
- Heavily reduced by water, ice, and thermal resistance.
- High resource consumption when maintaining active effects.
- Can cause environmental side effects (NPCs, structures, drops).
- Difficult control in closed areas.

#### 🪄 Ways to Use

- **Direct Application:** Incendiary projectiles, Continuous fire beams.
- **Persistent Area:** Burning surfaces, Intense heat zones, Fields that apply burn through residency.
- **Environmental Interaction:** Igniting flammable structures, Reaction with liquids and organic materials, Permanent or temporary terrain alteration.
- **Alchemical Use:** Incendiary bombs, Reagent for elemental fusions, Energy source for alchemical machines.

#### 😵 Associated Status Effects

- **Burn:** Accumulative periodic damage.
- **Thermal Exhaustion:** Reduction of stamina or speed.
- **Disorientation:** In fragile creatures.
- **Thermal Residue:** On the ground, affecting future interactions.

#### 🔥 Fire Essence

- **Obtain Methods:**
  - **Environmental:** High-temperature biomes, Solidified lava, Thermal vents, Extreme weather events.
  - **Creatures:** Elemental entities, Mobs exposed to extreme heat for a long time, Igneous bosses.
  - **Structures:** Ancient furnaces, Abandoned industrial complexes, Burnt ruins.
  - **Alchemy:** Distillation of flammable compounds, Controlled combustion processes, Thermal fermentation with risk of failure.
- **Essence Classification:**
  - **Unstable:** Easy to obtain, unpredictable effects.
  - **Refined:** Standard use in spells.
  - **Living:** Reacts with the environment and other elements.
- **Systemic Behavior:**
  - Excessive use increases local temperature.
  - High temperature alters spawn, weather, and biome.
  - Affected regions tend to generate igneous entities.

---

### 🪨 Earth

**Concept:** Structural control, damage mitigation, and physical space modification element.

> **Functional Definition:** It acts by creating, displaced, or reinforcing solid matter to influence combat flow and world progression.

**Effect Type:**

- Physical and elemental damage mitigation.
- Area control by obstruction.
- Direct terrain and structure alteration.
- Impact-based damage and pressure (non-continuous).

#### ✅ Advantages

- High defensive efficiency.
- Scales well with preparation and positioning.
- Strong against direct contact enemies.
- Creates real and permanent cover.
- Synergy with construction systems and worldgen.

#### ❌ Disadvantages

- Low mobility.
- Slow execution.
- Little efficiency against aerial targets.
- Weak against water (erosion) and explosions.
- Consumes space and can hinder allies.

#### 🪄 Ways to Use

- **Structural Creation:** Walls, Columns, Domes, Directional barriers.
- **Terrain Control:** Ground elevation or depression, Obstacle creation, Passage closing, Enemy movement channeling.
- **Physical Attack:** Stone spikes, Massive projectiles, Seismic waves, Block drops.
- **Alchemical Use:** Equipment reinforcement, Defensive enchantments, Containment ritual components, Permanent alchemical structures.

#### 🪨 Earth Essence

- **Obtain Methods:**
  - **Environmental:** Mineral veins, Deep caves, Rocky biomes, Tectonically unstable areas.
  - **Creatures:** Stone elementals, Underground creatures, Fossilized or crystallized mobs.
  - **Structures:** Abandoned mines, Ancient fortifications, Buried ruins.
  - **Alchemy:** Compaction of minerals, Sediment distillation, Pressure crystallization. (Risk: Failure leads to collapse or burying).
- **Essence Classification:**
  - **Fragmented:** Weak, easy to obtain.
  - **Condensed:** Standard use.
  - **Geological Core:** Rare, high stability.
- **Systemic Behavior:**
  - Continuous use alters local topography.
  - Excess leads to landslides, collapses, or caves.
  - Created structures may persist in the world.
  - Saturated regions attract underground creatures.

---

### 💨 Wind

**Concept:** Vector force, kinetic control, and trajectory manipulation element.

> **Functional Definition:** It acts by applying acceleration, deceleration, and redirection to entities, projectiles, and environmental effects.

**Effect Type:**

- Forced displacement (knockback / pull / lift).
- Speed and direction alteration.
- Projectile control.
- Interference with incantations and movements.

#### ✅ Advantages

- High control without depending on direct damage.
- Effective against multiple targets.
- Neutralizes physical and magical projectiles.
- Strong synergy with other elements.
- Scales with positioning and timing.

#### ❌ Disadvantages

- Low base damage.
- Little effect on very heavy targets.
- Requires reading the space.
- Lower impact in closed areas.
- Can hinder allies and setups.

#### 🪄 Ways to Use

- **Movement Manipulation:** Pushing or pulling enemies, Launching targets into the air, Redirecting falls, Briefly suspending entities.
- **Projectile Control:** Deflecting arrows and magic, Accelerating allied projectiles, Creating turbulence zones, Curving trajectories.
- **Mobility:** Assisted jumps, Rapid displacement, Gliding, Fall cancellation.
- **Environmental Interaction:** Spreading fire, poisons, and gases, Dissipating fog and smoke, Activating pressure-based mechanisms, Temporarily altering local weather.

#### 😵 Associated Status Effects

- **Disequilibrium:** Reduces precision and control.
- **Exposure:** Removes cover.
- **Disarm:** Chance of dropping light items.
- **Forced Fall.**

#### 💨 Wind Essence

- **Obtain Methods:**
  - **Environmental:** High peaks, Gorges, Open plains, Storms and cold fronts.
  - **Creatures:** Aerial elementals, Flying creatures, Extreme weather entities.
  - **Structures:** Ancient towers, Windmills, Weather observatories, Ruins in high regions.
  - **Alchemy:** Air compression in sealed chambers, Distillation of atmospheric currents. (Risk: Failed use leads to pressure explosion).
- **Essence Classification:**
  - **Dispersed:** Weak, difficult to store.
  - **Condensed:** Standard use.
  - **Cyclonic:** Rare, highly reactive.
- **Systemic Behavior:**
  - Continuous use alters weather patterns.
  - Can increase spawn of aerial creatures.
  - Saturated zones generate random gusts.
  - Interferes with other active elements.

---

### 💧 Water

**Concept:** State control, thermal mitigation, and chemical interaction element.

> **Functional Definition:** It acts by altering environmental and target conditions, reducing direct aggressiveness and preparing reactions with other elements.

**Effect Type:**

- Fluid area control.
- Physical state alteration (wet, soaked).
- Reduction and neutralization of thermal effects.
- Indirect damage by drowning, pressure, or reaction.

#### ✅ Advantages

- Strong against fire and heat.
- Excellent in prolonged control.
- Facilitates elemental reactions.
- Affects multiple targets consistently.
- Low environmental risk compared to fire.

#### ❌ Disadvantages

- Low direct damage.
- Dependent on terrain and volume.
- Can favor aquatic enemies.
- Slow execution in dry areas.
- Little immediate effect without combination.

#### 🪄 Ways to Use

- **Area Control:** Slippery surfaces, Flooding fields, Directional currents, Hydraulic pressure zones.
- **State Alteration:** "Wet" application, Burn suppression, Electric conduction increase, Friction reduction.
- **Indirect Attack:** High-pressure jets, Hydraulic crushing, Progressive drowning, Instability-induced fall.
- **Alchemical Use:** Base for potions and solvents, Extraction and dilution of essences, Controlled chemical reactions, Volatile compound stabilization.

#### 😵 Associated Status Effects

- **Wet:** Reduces lightning resistance, increases effective weight.
- **Drag:** Speed reduction.
- **Disorientation:** In terrestrial creatures.
- **Asphyxia:** In prolonged submersion.

---

### ❄️ Ice

**Concept:** Thermal reduction, movement restriction, and structural weakening element.

> **Functional Definition:** It acts by decreasing targets' kinetic energy and transforming water and surfaces into unstable solid states.

**Effect Type:**

- Progressive speed reduction.
- Temporary immobilization.
- Thermal fracture damage.
- Physical terrain modification.

#### ✅ Advantages

- Strong crowd control.
- Direct synergy with water.
- Neutralizes fire and thermal effects.
- Creates defensive and offensive surfaces.
- Excellent setup for burst from other elements.

#### ❌ Disadvantages

- Low direct damage.
- Cold-resistant targets greatly reduce impact.
- Breakable effects.
- Dependent on positioning.
- Loses efficiency in hot environments.

#### 🪄 Ways to Use

- **Movement Control:** Gradual Freezing, Ice Rooting, Crystalline Prisons, Slippery surfaces.
- **Environmental Alteration:** Freezing water and mud, Creating temporary bridges, Sealing passages, Reinforcing structures for a short period.
- **Structural Attack:** Ice blades, Emerging spikes, Thermal expansion explosion, Fracture of frozen targets.
- **Alchemical Use:** Reagent preservation, Potion stabilization, Thermal shock reactions, Containment of unstable essences.

#### 😵 Associated Status Effects

- **Cold:** Speed and attack reduction.
- **Frozen:** Total immobilization.
- **Brittleness:** Increase in received damage.
- **Hypothermia:** Progressive loss of resources.

#### ❄️ Ice Essence

- **Obtain Methods:**
  - **Environmental:** Glacial biomes, Frozen peaks, Ice caves, Snowstorms.
  - **Creatures:** Ice elementals, Arctic predators, Crystallized entities.
  - **Structures:** Ruins buried in ice, Ancient frozen temples, Preservation laboratories.
  - **Alchemy:** Controlled crystallization, Residual cold extraction, Freezing under pressure. (Risk: Failure leads to violent shattering).
- **Essence Classification:**
  - **Fragile:** Short duration.
  - **Stable:** Standard use.
  - **Eternal:** Rare, does not melt easily.
- **Systemic Behavior:**
  - Prolonged use reduces local temperature.
  - Can freeze entire bodies of water.
  - Increases spawn of cold creatures.
  - Creates risk of environmental fractures.

---

### ⚡ Lightning

**Concept:** Electric discharge, chain propagation, and energetic interference element.

> **Functional Definition:** It acts by applying instantaneous damage, jumping between conductors, and affecting active systems and altered states.

**Effect Type:**

- Instantaneous damage (burst).
- Propagation between nearby targets.
- Interruption of actions and incantations.
- Activation or overload of systems.

#### ✅ Advantages

- High immediate damage.
- Scales very well with grouped targets.
- Strong against wet or metallic enemies.
- Can interrupt attacks and spells.
- Little dependent on terrain.

#### ❌ Disadvantages

- Limited range without conductors.
- Inefficient against isolated targets.
- High resource consumption per use.
- Little prolonged effect.
- Risk of electric feedback to the user.

#### 🪄 Ways to Use

- **Direct Discharge:** Instant lightning, Electric projectiles, Short-range pulses.
- **Propagation:** Electric chain currents, Conduction fields, Jumps between nearby targets.
- **Interference:** Interrupting incantation, Deactivating mechanisms, Causing failures in artificial creatures, Overloading equipment.
- **Alchemical Use:** Energy source, Reactor activation, Catalyst for fast reactions, Item charging.

#### 😵 Associated Status Effects

- **Electrocuted:** Additional immediate damage.
- **Stun:** Temporary loss of action.
- **Overload:** Active effects are interrupted.
- **Conduction:** Increases chance of electric jump.

#### ⚡ Lightning Essence

- **Obtain Methods:**
  - **Environmental:** Electric storms, High peaks, Natural towers, Unstable weather regions.
  - **Creatures:** Electric elementals, Energized creatures, Artificial entities.
  - **Structures:** Ancient lightning rods, Abandoned generators, Arcane laboratories.
  - **Alchemy:** Capturing discharges, Storage in conductive crystals, Energetic compression. (Risk: Failure leads to electric explosion).
- **Essence Classification:**
  - **Unstable:** Difficult to contain.
  - **Condensed:** Standard use.
  - **Ionized:** Rare, high propagation.
- **Systemic Behavior:**
  - Frequent use ionizes the area.
  - Increases chance of electric events.
  - Potentiates effects of water and metal.
  - Can activate dormant entities.

---

### ☀️ Light

**Concept:** Energetic radiation element, information revelation, and anomalous state neutralization.

> **Functional Definition:** It acts by reducing local entropy, exposing hidden entities, and interfering with shadow-based effects, corruption, or illusion.

**Effect Type:**

- Direct energy damage.
- Revelation and detection.
- Negative effect suppression.
- Interference in hidden states.

#### ✅ Advantages

- Strong against shadow, illusion, and corruption.
- Reveals invisible or camouflaged enemies.
- Cleans or reduces debuffs.
- Acts from a distance with high precision.
- Low destructive environmental impact.

#### ❌ Disadvantages

- Reduced efficiency against neutral targets.
- Little terrain alteration.
- High consumption of refined essence.
- Little synergy with physical effects.
- Weak in light-saturated environments.

#### 🪄 Ways to Use

- **Energetic Damage:** Concentrated beams, Radiant pulses, Photonic projectiles.
- **Revelation:** Exposing hidden entities, Marking targets, Lighting up corrupted areas, Detecting traps and illusions.
- **Suppression:** Removing debuffs, Reducing corruption, Weakening shadowy entities, Stabilizing unstable areas.
- **Alchemical Use:** Purifying reagents, Catalyst for cleansing rituals, Creating radiant crystals, Stabilizing dangerous essences.

#### �� Associated Status Effects

- **Illuminated:** Visible, without concealment bonus.
- **Purified:** Negative effect reduction.
- **Exposed:** Briefly loses defensive bonuses.
- **Radiation:** Light continuous damage in sensitive entities.

#### ☀️ Light Essence

- **Obtain Methods:**
  - **Environmental:** High solar exposure regions, Rare luminous phenomena, High peaks at dawn.
  - **Creatures:** Radiant entities, Ancient guardians, Luminous plane creatures.
  - **Structures:** Solar temples, Ancient observatories, Luminous focus artifacts.
  - **Alchemy:** Light concentration in crystals, Refraction purification, Radiant energy distillation. (Risk: Failure leads to blindness or overload).
- **Essence Classification:**
  - **Diffuse:** Common, low potency.
  - **Focused:** Standard use.
  - **Radiant:** Rare, high purity.
- **Systemic Behavior:**
  - Continuous use reduces local corruption.
  - Can stabilize chaotic regions.
  - Decreases spawn of shadowy entities.
  - Interferes with active shadow effects.

---

### 🌑 Shadow

**Concept:** Concealment, entropic drainage, and perception manipulation element.

> **Functional Definition:** It acts by reducing available information, degrading energy, and exploring states of fear, illusion, and vulnerability.

**Effect Type:**

- Concealment and partial invisibility.
- Drainage damage.
- Sensory interference.
- Psychological and energetic debuffs.

#### ✅ Advantages

- Strong in indirect control.
- Effective against isolated targets.
- Synergy with dark environments.
- Allows real stealthy approach.
- Scales with duration and positioning.

#### ❌ Disadvantages

- Weak under intense light.
- Little structural impact.
- Limited direct damage.
- High maintenance cost.
- Requires preparation and a favorable environment.

#### 🪄 Ways to Use

- **Concealment:** Conditional invisibility, Dynamic camouflage, Enemy detection reduction, Illumination suppression.
- **Drainage:** Stealing life or energy, Progressive weakening, Erosion of active buffs, Mental exhaustion.
- **Sensory Manipulation:** Movement illusions, Sound distortion, Target confusion, Fear induction.
- **Alchemical Use:** Catalyst for forbidden rituals, Creating entropic poisons, Corruption storage, Stabilizing chaotic elements.

#### 😵 Associated Status Effects

- **Hidden:** Reduces detection chance.
- **Drained:** Continuous resource loss.
- **Terrified:** Precision and control reduction.
- **Corrupted:** Increase in received damage from light.

#### 🌑 Shadow Essence

- **Obtain Methods:**
  - **Environmental:** Deep caves, Unlit ruins, High entropy biomes, Eclipse events.
  - **Creatures:** Shadowy entities, Nocturnal creatures, Corrupted mobs.
  - **Structures:** Forgotten sanctuaries, Ancient prisons, Inverted temples.
  - **Alchemy:** Condensation of luminous absence, Entropic extraction, Energetic sacrifice. (Risk: Failure leads to uncontrolled corruption).
- **Essence Classification:**
  - **Thin:** Weak, unstable.
  - **Dense:** Standard use.
  - **Abyssal:** Rare, highly corrosive.
- **Systemic Behavior:**
  - Prolonged use darkens the region.
  - Reduces local global illumination.
  - Increases spawn of shadowy creatures.
  - Weakens active light effects.

---

### 🌿 Life

**Concept:** Biological regeneration element, organic adaptation, and directed growth.

> **Functional Definition:** It acts by restoring, modifying, and expanding living structures, with the cost and risk of uncontrolled growth.

**Effect Type:**

- Progressive regeneration.
- Attribute increase and modification.
- Creation and control of organic matter.
- Vitality transfer.

#### ✅ Advantages

- Prolonged sustain.
- Strong synergy with allies and summons.
- Can permanently alter entities.
- Scales with time and investment.
- Strong outside immediate combat.

#### ❌ Disadvantages

- Slow response in intense combat.
- Can generate unwanted mutations.
- Inefficient against massive damage.
- Strong counter by death and corruption.
- Requires constant management.

#### 🪄 Ways to Use

- **Regeneration:** Gradual healing, Limb regeneration, Stamina recovery, Removal of degenerative effects.
- **Organic Growth:** Vines, roots, carapaces; Living barriers; Bridges and organic structures; Temporary biological armor.
- **Adaptation:** Progressive resistance to received damage, Adjustment to hostile biomes, Tolerance to poisons and diseases, Evolution of summons.
- **Alchemical Use:** Creating organic elixirs, Living fermentation, Catalyst for mutations, Revitalization rituals.

#### 😵 Associated Status Effects

- **Regenerating:** Continuous recovery.
- **Rooted:** Reduced mobility, higher defense.
- **Mutation:** Gain with side effect.
- **Vital Excess:** Harmful growth.

#### 🌿 Life Essence

- **Obtain Methods:**
  - **Environmental:** Dense forests, Untouched regions, Ancient trees, Fertile biomes.
  - **Creatures:** Natural entities, Forest guardians, Regenerative creatures.
  - **Structures:** Natural sanctuaries, Living altars, Ancient gardens.
  - **Alchemy:** Vital sap extraction, Controlled cultivation, Alchemical symbiosis. (Risk: Failure leads to hostile proliferation).
- **Essence Classification:**
  - **Latent:** Weak, common.
  - **Vigorous:** Standard use.
  - **Primordial:** Rare, highly reactive.
- **Systemic Behavior:**
  - Continuous use increases local fertility.
  - Can generate excessive growth.
  - Attracts organic creatures.
  - Can conflict with artificial structures.

---

### ☠️ Death

**Concept:** Vital degradation element, interruption of regeneration, and reuse of organic waste.

> **Functional Definition:** It acts by ending life processes, accelerating decomposition, and converting vitality into entropic energy.

**Effect Type:**

- Degenerative damage.
- Suppression of healing and regeneration.
- Progressive decomposition.
- Life-to-resource conversion.

#### ✅ Advantages

- Strong against targets with high regeneration.
- Neutralizes Life effects.
- Consistent damage over time.
- Synergy with corpses and death areas.
- Scales well in long combats.

#### ❌ Disadvantages

- Little burst.
- Low efficiency against constructs and machines.
- Requires maintenance of effects.
- Can generate environmental consequences.
- Hostility from NPCs and factions.

#### 🪄 Ways to Use

- **Degeneration:** Progressive rot, Localized necrosis, Vital drainage, Erosion of biological buffs.
- **Suppression:** Healing block, Regeneration reduction, Cancellation of Life effects, Prolonged weakening.
- **Reuse:** Animation of corpses, Extractions of residual vital energy, Creating death zones, Feeding rituals.
- **Alchemical Use:** Necrotic poison, Corpse conservation, Catalyst for funeral rituals, Stabilization of organic corruption.

#### 😵 Associated Status Effects

- **Necrosis:** Continuous damage + healing reduction.
- **Exhausted:** Loss of maximum stamina.
- **Marked for Death:** Increases received damage.
- **Putrefaction:** Effects spread after death.

#### ☠️ Death Essence

- **Obtain Methods:**
  - **Environmental:** Ancient battlefields, Cemeteries and crypts, Sterile biomes, Extinction zones.
  - **Creatures:** Undead, Necromantic entities, Corrupted creatures.
  - **Structures:** Catacombs, Mausoleums, Funeral altars.
  - **Alchemy:** Distillation of vital residues, Controlled decomposition, Organic sacrifice. (Risk: Failure leads to plague or contamination).
- **Essence Classification:**
  - **Residual:** Weak, unstable.
  - **Condensed:** Standard use.
  - **Terminal:** Rare, extremely corrosive.
- **Systemic Behavior:**
  - Continuous use sterilizes the environment.
  - Reduces organic growth.
  - Increases spawn of undead.
  - Directly conflicts with Life and Light.

---

### 🧠 Mind

**Concept:** Cognitive processing element, perception manipulation, and behavioral interference.

> **Functional Definition:** It acts directly on decision-making, focus, memory, and action control.

**Effect Type:**

- Partial or total control of entities.
- Alteration of perception and priority.
- Cognitive interruption.
- Psychic damage (non-physical).

#### ✅ Advantages

- Ignores physical armor.
- Extremely strong against intelligent targets.
- Allows control without direct damage.
- Synergy with stealth and preparation.
- Can end combat without killing.

#### ❌ Disadvantages

- Weak against simple creatures or automatons.
- High essence cost.
- Limited duration.
- Strong counter by mental resistance.
- Can generate severe narrative consequences.

#### 🪄 Ways to Use

- **Behavioral Control:** Charm, Taunt, Force flight or hesitation, Redirect aggressiveness.
- **Perceptual Manipulation:** Visual and auditory illusions, Cognitive invisibility, Distortion of perceived space, False targets.
- **Mental Interference:** Incantation interruption, Command confusion, Sensory overload, Direct psychic damage.
- **Alchemical Use:** Elixirs of focus or delirium, Psychic drugs, Catalysts for mental rituals, Storage of residual thoughts.

#### 😵 Associated Status Effects

- **Confused:** Erratic commands.
- **Dominated:** Temporary control.
- **Terrified:** Flight or paralysis.
- **Mental Depletion:** Action cost increase.

#### 🧠 Mind Essence

- **Obtain Methods:**
  - **Environmental:** Locations of high psychic activity, Knowledge ruins, Dream or delirium zones.
  - **Creatures:** Intelligent entities, Natural psychics, Oneiric creatures.
  - **Structures:** Ancient libraries, Mental observatories, Control laboratories.
  - **Alchemy:** Memory distillation, Residual thought extraction, Focus condensation. (Risk: Failure leads to temporary insanity).
- **Essence Classification:**
  - **Diffuse:** Weak, unstable.
  - **Clear:** Standard use.
  - **Synaptic:** Rare, high precision.
- **Systemic Behavior:**
  - Excessive use affects nearby NPCs.
  - Can alter faction behavior.
  - Generates accumulative mental resistance.
  - Abuse results in psychic instability.

---

### 🩸 Blood

**Concept:** Sacrifice element, vital bond, and direct amplification.

> **Functional Definition:** It converts current or potential life into immediate power, creating strong effects with measurable and irreversible short-term cost.

**Effect Type:**

- Damage and effect amplification.
- Ritual activation.
- Bond between entities.
- Direct vitality transfer.

#### ✅ Advantages

- Scales directly with assumed risk.
- High immediate impact.
- Ignores traditional mana limitations.
- Strong synergy with Life and Death.
- Allows effects above the system's "normal."

#### ❌ Disadvantages

- Direct cost in HP or vital resources.
- Real risk of user death.
- Accumulative penalties.
- Low sustainability.
- Social and narrative hostility.

#### 🪄 Ways to Use

- **Sacrifice:** Converting HP into power, Activating spells above limit, Reducing cooldowns, Forcing instant incantation.
- **Bond:** Sharing damage between entities, Draining life from marked targets, Creating temporary pacts, Controlling summons by blood.
- **Amplification:** Increasing elemental damage, Extending effect duration, Ignoring resistances, Reinforcing complex rituals.
- **Alchemical Use:** Ritual catalyst, Sacrifice potions, Blood seals, Unstable vital storage.

#### 😵 Associated Status Effects

- **Bleeding:** Continuous HP loss.
- **Exhausted:** Temporary maximum life reduction.
- **Marked:** Traceable target.
- **Blood Bond:** Shared damage.

#### 🩸 Blood Essence

- **Obtain Methods:**
  - **Environmental:** Recent battlefields, Sacrificial altars, Massacre events.
  - **Creatures:** Organic enemies, Living bosses, Rare creatures (higher purity).
  - **Structures:** Profane temples, Ritual arenas, Forbidden laboratories.
  - **Alchemy:** Fresh blood distillation, Controlled coagulation, Mixing with vital catalysts. (Risk: Failure leads to hemorrhage or corruption).
- **Essence Classification:**
  - **Diluted:** Common, weak.
  - **Vital:** Standard use.
  - **Ancestral:** Rare, extreme power.
- **Systemic Behavior:**
  - Frequent use reduces maximum life.
  - Attracts predatory entities.
  - Can generate persistent curses.
  - Dangerous synergy with Death and Chaos.

---

## 6. Unstable Elements 🌀

### 🌀 Chaos

**Concept:** Active entropy, systemic instability, and local rule-breaking element.

> **Functional Definition:** It doesn't create specific effects; it alters how effects work, introducing variation, error, and mutation.

**Effect Type:**

- Random outcome modification.
- Effect amplification or collapse.
- Entity and environmental mutation.
- Controlled violation of system limits.

#### ✅ Advantages

- Potential for above-normal power.
- Breaks predictable combat patterns.
- Synergy with any element.
- Allows emergent solutions.
- High narrative impact.

#### ❌ Disadvantages

- Unpredictable outcome.
- High risk to the user.
- Difficult to balance.
- Frequent side effects.
- Can make areas unusable.

#### 🪄 Ways to Use

- **Spell Modification:** Alter range, shape, or cost; Invert effects; Duplicate or cancel outcomes; Introduce side effects.
- **Mutation:** Permanently alter attributes, Create unstable creatures, Fuse entities, Corrupt structures.
- **Systemic Rupture:** Ignore resistances, Break cooldowns, Distort short time, Rewrite local interactions.
- **Alchemical Use:** Universal catalyst, Potion unstabilizer, Unpredictable rituals, Fuel for forbidden artifacts.

#### 😵 Associated Status Effects

- **Unstable:** Variable effects.
- **Mutated:** Gain with penalty.
- **Distorted:** Altered controls.
- **Collapse:** Imminent critical failure.

#### 🌀 Chaos Essence

- **Obtain Methods:**
  - **Environmental:** Dimensional rifts, Corrupted regions, Anomalous events, Broken biomes.
  - **Creatures:** Unstable entities, Aberrations, Corrupted bosses.
  - **Structures:** Destroyed laboratories, Chaotic altars, Collapsed cores.
  - **Alchemy:** Failed reactions, Incompatible mixtures, Energetic overload. (Risk: Failure leads to local collapse).
- **Essence Classification:**
  - **Volatile:** Almost useless.
  - **Chaotic:** Standard use.
  - **Primordial:** Extremely rare.
- **Systemic Behavior:**
  - Continuous use degrades local rules.
  - Increases chance of random events.
  - Can create permanently unstable zones.
  - Interferes with all other elements.

---

### ✨ Ether

**Concept:** Spatial transition, dimensional displacement, and inter-planar mediation element.

> **Functional Definition:** It doesn't cause direct damage by default; it alters where and how entities and effects exist.

**Effect Type:**

- Teleportation and repositioning.
- Space distortion.
- Dimensional anchoring and unanchoring.
- Interference with collision and range.

#### ✅ Advantages

- Absolute positioning control.
- Ignores physical obstacles.
- Synergy with any element.
- Allows escape, access, and advanced manipulation.
- High tactical and utility value.

#### ❌ Disadvantages

- High essence cost.
- Errors cause severe failures.
- Little direct damage impact.
- Requires precise spatial reading.
- Strong counter by dimensional anchoring.

#### 🪄 Ways to Use

- **Displacement:** Short or medium teleportation, Position swap between entities, Return to marked point, Chain displacement.
- **Spatial Distortion:** Lengthen or compress distance, Curve trajectories, Create temporary folds, Alter effective range.
- **Anchoring:** Preventing enemy teleportation, Fixing entities in space, Stabilizing unstable structures, Sealing rifts.
- **Alchemical Use:** Teleportation cores, Transition rituals, Extradimensional storage, Catalyst for advanced fusions.

#### 😵 Associated Status Effects

- **Unanchored:** Unstable collision.
- **Displaced:** Forcibly altered position.
- **Phased:** Ignores partial collision.
- **Rupture:** Imminent spatial failure.

#### ✨ Ether Essence

- **Obtain Methods:**
  - **Environmental:** Spatial rifts, Liminal regions, Transition points between biomes, Dimensional events.
  - **Creatures:** Interplanar entities, Phased creatures, Portal guardians.
  - **Structures:** Ancient portals, Dimensional observatories, Displaced ruins.
  - **Alchemy:** Space condensation, Transition energy extraction, Dimensional stabilization. (Risk: Failure leads to random displacement).
- **Essence Classification:**
  - **Diffuse:** Unstable, imprecise.
  - **Stable:** Standard use.
  - **Transcendent:** Rare, highly precise.
- **Systemic Behavior:**
  - Continuous use weakens spatial boundaries.
  - Can generate spontaneous rifts.
  - Increases spawn of interplanar entities.
  - Interferes with local teleportation and collision.

---

### 🕳️ Void

**Concept:** Annullation, removal of existence, and systemic silence element.

> **Functional Definition:** It doesn't cause traditional damage; it interrupts, erases, or invalidates entities, effects, and local rules.

**Effect Type:**

- Annullation of entities and projectiles.
- Total magic suppression.
- Existence drainage.
- Creation of neutral zones.

#### ✅ Advantages

- Ignores resistances and armor.
- Strong against buffs, summons, and magic.
- Removes effects instead of competing with them.
- High control in critical situations.
- Scales well against complex builds.

#### ❌ Disadvantages

- Extremely high cost.
- Limited use.
- Permanent environmental impact.
- Can affect the user.
- Strong narrative and systemic penalty.

#### 🪄 Ways to Use

- **Annullation:** Canceling active spells, Erasing projectiles, Dissolving summons, Removing buffs and debuffs.
- **Existence Drainage:** Max life reduction, Attribute erosion, Consumption of weak entities, Progressive weakening.
- **Void Zones:** Areas without magic, Total elemental suppression, Absolute silence, Collapse of persistent effects.
- **Alchemical Use:** Annullation cores, Erase rituals, Silence seals, Containment of forbidden entities.

#### 😵 Associated Status Effects

- **Annuled:** Effects removed.
- **Emptied:** Maximum resource reduction.
- **Silenced:** Impossibility to incant.
- **De-existence:** Direct damage to essence.

#### 🕳️ Void Essence

- **Obtain Methods:**
  - **Environmental:** Non-existence rifts, Dead regions, World edges, Collapse events.
  - **Creatures:** Void entities, Non-existent aberrations, Apocalyptic bosses.
  - **Structures:** Dimensional prisons, Containment cores, Erased ruins.
  - **Alchemy:** Total reagent removal, Absolute isolation, Zeroed reactions. (Risk: Failure erases area or user).
- **Essence Classification:**
  - **Echo:** Almost useless.
  - **Null:** Standard use.
  - **Abyssal:** Extremely rare.
- **Systemic Behavior:**
  - Continuous use creates unrecoverable areas.
  - Reduces global spawn.
  - Interrupts magic systems.
  - Can generate paradoxical entities.

---

### ⏳ Time

**Concept:** Flow control, event ordering, and causal restriction element.

> **Functional Definition:** It doesn't create new states; it alters the speed, order, or persistence of what already exists.

**Effect Type:**

- Acceleration and deceleration.
- Partial pause.
- Limited reversal.
- Extension or shortening of states.

#### ✅ Advantages

- Extreme combat control.
- Synergy with all elements.
- Allows error correction.
- Very strong in precise execution.
- High impact even without damage.

#### ❌ Disadvantages

- Extremely high cost.
- Short windows.
- Strong use restriction.
- Risk of paradoxes.
- Accumulative penalties.

#### 🪄 Ways to Use

- **Flow Alteration:** Selective slow, Localized haste, Accelerating cooldowns, Decelerating projectiles.
- **Partial Pause:** Freezing entities, Suspending effects, Locking small areas, Interrupting critical events.
- **Limited Reversal:** Reversing recent position, Canceling last damage, Restoring short previous state, Returning projectiles to starting point.
- **Duration Manipulation:** Extending buffs/debuffs, Anticipating collapses, Shortening dangerous effects, Forcing immediate expiration.

#### 😵 Associated Status Effects

- **Slow:** Action reduction.
- **Hasted:** Action increase.
- **Frozen in Time:** No interaction.
- **Temporal Echo:** Action repeats.

#### ⏳ Time Essence

- **Obtain Methods:**
  - **Environmental:** Temporal rifts, Out-of-time ruins, Discontinuity events, Unstable zones.
  - **Creatures:** Chrono-affected entities, Temporal guardians, Paradoxical bosses.
  - **Structures:** Ancient clocks, Temporal observatories, Loop prisons.
  - **Alchemy:** Event distillation, Duration condensation, Causal isolation. (Risk: Failure creates loop or forced aging).
- **Essence Classification:**
  - **Residual:** Weak, short duration.
  - **Stable:** Standard use.
  - **Paradoxical:** Rare, high risk.
- **Systemic Behavior:**
  - Repeated use generates temporal resistance.
  - Can cause aging in entities.
  - Creates echoes of past events.
  - Interferes with Ether and Void.

---

## 7. Chemical Elements 🧪

### ⚙️ Metal

**Concept:** Conductivity, structural amplification, and physical response element.

> **Functional Definition:** It doesn't create effects on its own; it modifies how other effects propagate, resist, or return.

**Effect Type:**

- Energy conduction (lightning, heat, ether).
- Impact amplification or blocking.
- Reflection and force return.
- Direct interaction with equipment.

#### ✅ Advantages

- Strong synergy with Lightning, Fire, and Ether.
- Increases equipment efficiency.
- High physical resistance.
- Allows for reactive (counter) builds.
- Stable and predictable.

#### ❌ Disadvantages

- Weight and slowness.
- Vulnerable to corrosion (water, acid).
- Can amplify incorrectly received damage.
- Low flexibility.
- Dependent on gear.

#### 🪄 Ways to Use

- **Conduction:** Channeling lightning, Propagating heat, Redirecting energy, Creating conductive networks.
- **Amplification:** Increasing elemental damage, Extending range via conduction, Intensifying continuous effects, Focusing energy on specific points.
- **Reactive Defense:** Reflective plates, Damage return, Controlled dissipation, Directional shielding.
- **Alchemical Use:** Elemental alloys, Conductive cores, Physical catalysts, Stability seals.

#### 😵 Associated Status Effects

- **Conductor:** Increases lightning propagation.
- **Heated:** Continuous heat damage.
- **Magnetized:** Attraction/repulsion.
- **Overloaded:** Imminent failure.

#### ⚙️ Metal Essence

- **Obtain Methods:**
  - **Environmental:** Mineral veins, Meteorites, Ore-rich regions.
  - **Creatures:** Constructs, Metallic entities, Mechanized mobs.
  - **Structures:** Ancient forges, Industrial cities, Mechanical ruins.
  - **Alchemy:** Fusion and alloy, Impurity refining, Living metal condensation. (Risk: Failure leads to structural collapse).
- **Essence Classification:**
  - **Raw:** Common.
  - **Refined:** Standard use.
  - **Living:** Rare, reacts to energy.
- **Systemic Behavior:**
  - Saturated areas attract lightning.
  - Interferes with magnetic fields.
  - Amplifies active effects.
  - Can cause chain overload.

---

### 💎 Crystal

**Concept:** Focus, stabilization, and storage element.

> **Functional Definition:** It doesn't generate energy; it organizes, maintains, and directs elemental energy without loss.

**Effect Type:**

- Magic focusing.
- Effect stability.
- Energy storage.
- Precision and repeatability.

#### ✅ Advantages

- Reduces magic cost.
- Increases precision.
- Maintains active effects for longer.
- Allows pre-loading.
- Essential for advanced builds.

#### ❌ Disadvantages

- Fragile.
- Overload causes explosion.
- Dependent on purity.
- Little utility when isolated.
- Requires preparation.

#### 🪄 Ways to Use

- **Focus:** Reduces dispersion, Increases effective range, Directs complex effects, Eliminates magic noise.
- **Stabilization:** Prevents alchemical failure, Maintains continuous effects, Reduces variance, Prevents collapse.
- **Storage:** Pre-loaded spell, Encapsulated essence, Reusable charges, Triggered release.
- **Controlled Amplification:** Predictable boost, Synergy with Light, Ether, and Time, Multiplication without chaos, Clean scaling.

#### 😵 Associated Status Effects

- **Attuned:** Greater efficiency.
- **Resonant:** Repeated effect.
- **Unstable:** Imminent break.
- **Saturated:** Does not accept more charge.

#### 💎 Crystal Essence

- **Obtain Methods:**
  - **Environmental:** Geodes, Crystalline caves, Lay lines.
  - **Creatures:** Crystallized entities, Focus guardians, Resonance mobs.
  - **Structures:** Arcane towers, Focus chambers, Stabilization altars.
  - **Alchemy:** Essence crystallization, Controlled cooling. (Risk: Failure leads to chaotic fragmentation).
- **Essence Classification:**
  - **Impure:** Low efficiency.
  - **Clear:** Standard.
  - **Perfect:** High stability.
- **Systemic Behavior:**
  - Amplifies what it touches.
  - Maintains active states.
  - Reacts to overload.
  - Can enter chain resonance.

---

### 🧪 Poison

**Concept:** Progressive degradation and biological interference element.

> **Functional Definition:** It doesn't cause burst (damage explosion); it breaks down the target over time, ignoring raw defense.

**Effect Type:**

- Continuous damage (DoT).
- Stack-based accumulation.
- Gradual weakening.
- Persistence after combat.

#### ✅ Advantages

- Scales with duration.
- Ignores part of armor.
- Strong against large targets.
- Maintains pressure outside direct combat.
- Synergy with control.

#### ❌ Disadvantages

- Slow result.
- Weak against undead and machines.
- Can be purified.
- Requires continuous application.
- Low immediate impact.

#### 🪄 Ways to Use

- **Direct Application:** Strikes, Projectiles, Clouds, Surface contact.
- **Accumulation:** Stacks increase damage, Stacks unlock effects, Controlled maximum limit, Gradual decay.
- **Interference:** Healing reduction, Stamina reduction, Action delay, Progressive penalty.
- **Environmental Persistence:** Contaminated areas, Poisoned water, Toxic terrain, Traps.

#### 😵 Associated Status Effects

- **Poisoned:** Damage over time.
- **Contaminated:** Amplified debuff.
- **Organic Collapse:** Healing failure.
- **Saturated:** Stack limit reached.

#### 🧪 Poison Essence

- **Obtain Methods:**
  - **Environmental:** Toxic plants, Fungi, Swamps, Carcasses.
  - **Creatures:** Spiders, Snakes, Toxic entities.
  - **Structures:** Laboratories, Dens, Contaminated ruins.
  - **Alchemy:** Organic distillation, Chemical concentration. (Risk: Failure leads to hostile mutation).
- **Essence Classification:**
  - **Weak:** Short duration.
  - **Concentrated:** Standard.
  - **Virulent:** Side effect.
- **Systemic Behavior:**
  - Stacks interact with Time.
  - Water can dilute.
  - Fire can neutralize or volatilize.
  - Crystal stabilizes.
