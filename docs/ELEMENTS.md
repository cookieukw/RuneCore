# RuneCore: Elements & Magical Systems

Technical specification of the 20 magical elements in RuneCore, including their concepts, functional definitions, advantages, disadvantages, and system behaviors.

---

## 1. Elements Overview

RuneCore features 20 distinct elements divided into four tiers:

| Basic Level  | Advanced Level | Unstable Elements | Chemical Elements |
| :----------: | :------------: | :---------------: | :---------------: |
|   🔥 Fire    |    ☀️ Light    |     🌀 Chaos      |     ⚙️ Metal      |
|   🪨 Earth   |   🌑 Shadow    |     ✨ Ether      |    💎 Crystal     |
|   💨 Wind    |    🌿 Life     |      🕳️ Void      |     🧪 Poison     |
|   💧 Water   |    ☠️ Death    |      ⏳ Time      |      🧴 Acid      |
|    ❄️ Ice    |    🧠 Mind     |                   |                   |
| ⚡ Lightning |    🩸 Blood    |                   |                   |

---

## 2. Detailed Element Specifications

### Fire

**Concept:** Continuous damage and spatial control (environmental alteration).

**Functional Definition:** Alters environmental and target conditions, reducing direct physical aggressiveness while setting up reactions with other elements.

**Damage Type:**
- Continuous damage over time (DoT).
- Area of effect (AoE) with terrain persistence.
- Ignores part of physical mitigation (burn penetration).

**Scaling:** Scales with effect duration rather than raw burst.  
**Playstyle:** Focused on damage accumulation, area denial, and environmental manipulation.

#### Advantages
- High efficiency against grouped targets.
- Maintains tick damage without continuous player action.
- Limits enemy mobility by altering terrain.
- Direct synergy with flammable compounds (oil, wood, gas).
- Forces target repositioning.

#### Disadvantages
- Low efficiency against high-resistance single targets.
- Mitigated by water, ice, and thermal resistance.
- High resource consumption when maintaining active surfaces.
- Risk of collateral environmental damage (NPCs, structures, loot).
- Difficult to control in confined areas.

#### Applications
- **Direct Application:** Incendiary projectiles, continuous fire beams.
- **Persistent Area:** Burning surfaces, heat zones, residency burn fields.
- **Environmental Interaction:** Igniting structures, reacting with organic materials/liquids, terrain modification.
- **Alchemical Use:** Incendiary bombs, elemental fusion reagent, heat source for alchemical apparatuses.

#### Associated Status Effects
- **Burn:** Accumulative periodic damage.
- **Thermal Exhaustion:** Reduces stamina recovery or speed.
- **Disorientation:** Applies to fragile targets.
- **Thermal Residue:** Surface modifier affecting future interactions.

#### Fire Essence
- **Obtain Methods:**
  - **Environmental:** High-temperature biomes, lava, thermal vents, extreme heat events.
  - **Creatures:** Fire elementals, mobs exposed to heat, igneous bosses.
  - **Structures:** Ancient furnaces, industrial complexes, burnt ruins.
  - **Alchemy:** Distillation of flammable compounds, controlled combustion, thermal fermentation.
- **Classification:**
  - **Unstable:** Common, unpredictable reactions.
  - **Refined:** Standard spellcraft resource.
  - **Living:** High environmental and cross-element reactivity.
- **Systemic Behavior:**
  - High usage increases local temperature.
  - Saturated areas alter spawn rates and biome properties.
  - Attracts igneous entities.

---

### Earth

**Concept:** Structural control, damage mitigation, and terrain modification.

**Functional Definition:** Creates, displaces, or reinforces solid matter to influence combat flow and terrain.

**Effect Type:**
- Physical and elemental damage mitigation.
- Obstruction and area control.
- Direct terrain and structure modification.
- Impact damage (non-continuous).

#### Advantages
- High defensive capability.
- Scales with preparation and positioning.
- Effective against direct-contact melee enemies.
- Creates physical cover.
- Direct integration with building and terrain systems.

#### Disadvantages
- Low mobility.
- Slow cast/execution time.
- Limited effectiveness against airborne targets.
- Susceptible to water (erosion) and explosive damage.
- Obstructs movement for allies if misplaced.

#### Applications
- **Structural Creation:** Walls, pillars, domes, directional barriers.
- **Terrain Control:** Ground elevation/depression, blocking paths, channeling enemy paths.
- **Physical Attack:** Stone spikes, heavy projectiles, seismic waves.
- **Alchemical Use:** Armor reinforcement, defensive enchantments, containment rituals.

#### Earth Essence
- **Obtain Methods:**
  - **Environmental:** Mineral veins, deep caves, rocky biomes, tectonic fault lines.
  - **Creatures:** Stone elementals, subterranean mobs, fossilized entities.
  - **Structures:** Abandoned mines, fortifications, subterranean ruins.
  - **Alchemy:** Mineral compaction, sediment distillation, pressure crystallization.
- **Classification:**
  - **Fragmented:** Common, low potency.
  - **Condensed:** Standard crafting grade.
  - **Geological Core:** Rare, high stability.
- **Systemic Behavior:**
  - Frequent usage modifies local topography.
  - Excess energy triggers landslides or cave collapse.
  - Saturated regions attract subterranean mobs.

---

### Wind

**Concept:** Vector force, kinetic control, and trajectory manipulation.

**Functional Definition:** Applies acceleration, deceleration, and redirection to entities, projectiles, and environmental hazards.

**Effect Type:**
- Forced displacement (knockback, pull, lift).
- Velocity and direction alteration.
- Projectile deflection and acceleration.
- Interruption of casts and movement.

#### Advantages
- High utility and spatial control without relying on high base damage.
- Multi-target crowd control.
- Deflects incoming physical and magic projectiles.
- Strong cross-element synergy.
- Scales with positioning and timing.

#### Disadvantages
- Low base damage output.
- Reduced impact against heavy entities.
- Requires precise spatial positioning.
- Less effective in narrow spaces.
- Can displace targets out of friendly damage zones.

#### Applications
- **Movement Control:** Pushing/pulling targets, airborne suspension, fall redirection.
- **Projectile Control:** Deflecting arrows/spells, accelerating allied projectiles, creating turbulence fields.
- **Mobility:** Assisted jumps, speed bursts, gliding, fall damage cancellation.
- **Environmental Interaction:** Spreading fire/gas/poison, clearing fog/smoke, pressure mechanism activation.

#### Associated Status Effects
- **Disequilibrium:** Reduces accuracy and control.
- **Exposure:** Strips target cover.
- **Disarm:** Chance to drop lightweight items.
- **Forced Fall:** Cancels airborne stability.

#### Wind Essence
- **Obtain Methods:**
  - **Environmental:** Mountain peaks, canyons, open plains, storm fronts.
  - **Creatures:** Air elementals, flying beasts, storm entities.
  - **Structures:** High towers, windmills, weather observatories.
  - **Alchemy:** Gas compression, atmospheric distillation.
- **Classification:**
  - **Dispersed:** Low stability.
  - **Condensed:** Standard grade.
  - **Cyclonic:** Highly reactive.
- **Systemic Behavior:**
  - Prolonged use influences weather conditions.
  - Increases aerial creature spawn rates.
  - Disturbs active gas and fire fields.

---

### Water

**Concept:** Fluid control, thermal mitigation, and chemical interaction.

**Functional Definition:** Alters environmental and target states, reducing heat/fire effects and facilitating chemical or electrical reactions.

**Effect Type:**
- Fluid area control.
- State application (wet, soaked).
- Heat neutralization.
- Indirect damage (pressure, drowning, elemental combos).

#### Advantages
- Counteracts fire and heat effects.
- Strong crowd control when combined with ice/lightning.
- Facilitates elemental chain reactions.
- Consistent multi-target application.

#### Disadvantages
- Low direct damage.
- Dependent on environmental water volume.
- Can buff aquatic enemies.
- Slow execution in dry biomes.

#### Applications
- **Area Control:** Slippery surfaces, localized flooding, directional currents, hydraulic pressure.
- **State Alteration:** Applying wet state, extinguishing fires, increasing electrical conductivity.
- **Indirect Attack:** High-pressure jets, hydraulic crushing, drowning mechanics.
- **Alchemical Use:** Solvent base for potions, essence extraction, reaction stabilization.

#### Associated Status Effects
- **Wet:** Decreases lightning resistance, increases target weight.
- **Drag:** Movement slowdown.
- **Asphyxia:** Submersion damage over time.

---

### Ice

**Concept:** Thermal reduction, movement restriction, and structural weakening.

**Functional Definition:** Lowers kinetic energy, freezing water and surfaces into solid states.

**Effect Type:**
- Progressive slowdown.
- Immobilization.
- Thermal fracture damage.
- Surface modification.

#### Advantages
- Excellent crowd control (root/freeze).
- Direct synergy with water.
- Counters fire/heat effects.
- Creates temporary solid structures and hazard zones.
- Prepares targets for physical or magical shatter damage.

#### Disadvantages
- Low direct base damage.
- Ineffective against cold-immune targets.
- Freeze effects can be broken by physical impacts.
- Reduced efficiency in high-temperature environments.

#### Applications
- **Control:** Slowing, rooting, freezing prisons, ice surfaces.
- **Environmental:** Freezing liquids, creating temporary bridges, sealing passages.
- **Structural Attack:** Ice blades, emerging spikes, thermal shock fracturing.
- **Alchemical Use:** Reagent preservation, thermal shock stabilization.

#### Associated Status Effects
- **Cold:** Reduces movement and attack speed.
- **Frozen:** Complete immobilization.
- **Brittleness:** Increases incoming physical damage.
- **Hypothermia:** Resource depletion over time.

#### Ice Essence
- **Obtain Methods:**
  - **Environmental:** Glaciers, frozen peaks, ice caves, blizzards.
  - **Creatures:** Ice elementals, arctic mobs.
  - **Structures:** Frozen ruins, preservation vaults.
  - **Alchemy:** Pressurized freezing, cold extraction.
- **Classification:**
  - **Fragile:** Short duration.
  - **Stable:** Standard grade.
  - **Eternal:** Non-melting.
- **Systemic Behavior:**
  - Lowers local ambient temperature.
  - Freezes standing water bodies.
  - Increases cold-adapted mob spawns.

---

### Lightning

**Concept:** Electrical discharge, chain conduction, and systemic interruption.

**Functional Definition:** Applies fast damage bursts, jumping between conductive targets and interrupting channels.

**Effect Type:**
- Instantaneous burst damage.
- Chain propagation across nearby targets.
- Spell and action interruption.
- Mechanism activation or overload.

#### Advantages
- High burst damage.
- Multi-target propagation when enemies are grouped.
- Increased damage against wet or armored targets.
- Interrupts enemy spellcasting and attacks.

#### Disadvantages
- Decreased range without conductive targets.
- Low efficiency against single isolated enemies.
- High mana/resource cost.
- Short duration / minimal persistent field effect.

#### Applications
- **Direct Discharge:** Lightning bolts, electric arcs, shock pulses.
- **Chain Mechanics:** Arcing between wet/metal targets, conduction zones.
- **Interruption:** Canceling casts, disrupting mechanism functions.
- **Alchemical Use:** Energy source, catalyst for rapid reactions, item charging.

#### Associated Status Effects
- **Electrocuted:** Immediate shock damage.
- **Stun:** Short duration action loss.
- **Overload:** Interrupts ongoing channeled effects.
- **Conduction:** Increases arcing range to nearby targets.

#### Lightning Essence
- **Obtain Methods:**
  - **Environmental:** Thunderstorms, high mountain spires.
  - **Creatures:** Lightning elementals, energized beasts.
  - **Structures:** Ancient lightning rods, generators.
  - **Alchemy:** Arc storage in crystals, energy condensation.
- **Classification:**
  - **Unstable:** High discharge risk.
  - **Condensed:** Standard grade.
  - **Ionized:** High chain range.
- **Systemic Behavior:**
  - Ionizes local area, increasing storm frequency.
  - Synergizes with metal and water hazards.

---

### Light

**Concept:** Radiation, entity revelation, and anomalous state purging.

**Functional Definition:** Reduces local entropy, reveals concealed entities, and purges shadow or corruption effects.

**Effect Type:**
- Direct radiant energy damage.
- Stealth and trap revelation.
- Negative effect suppression.
- Anti-shadow interaction.

#### Advantages
- Counters shadow, illusion, and corruption mechanics.
- Reveals invisible or camouflaged entities.
- Cleanses debuffs from allies.
- Precise long-range application.

#### Disadvantages
- Lower damage output against non-corrupted targets.
- Minimal physical terrain modification.
- High refined essence cost.
- Ineffective in high ambient light environments.

#### Applications
- **Radiant Damage:** Focused beams, light pulses, photonic projectiles.
- **Detection:** Revealing hidden units, illuminating dark areas, dispelling illusions.
- **Purification:** Debuff removal, corruption suppression.
- **Alchemical Use:** Reagent purification, cleansing ritual catalyst, radiant crystals.

#### Associated Status Effects
- **Illuminated:** Reveals target location and cancels stealth.
- **Purified:** Reduces negative status durations.
- **Exposed:** Briefly lowers defensive resistances.

---

### Shadow

**Concept:** Concealment, entropic drainage, and sensory disruption.

**Functional Definition:** Suppresses light, drains target vitality, and alters perception.

**Effect Type:**
- Concealment and partial invisibility.
- Resource drainage.
- Perception and sensory debuffs.
- Anti-light interaction.

#### Advantages
- Strong utility for stealth and indirect combat.
- High efficiency in unlit environments.
- Drains health/mana over time.
- Synergizes with night cycles and deep caves.

#### Disadvantages
- Cancelled or suppressed by strong light sources.
- Low direct physical damage.
- Minimal structural impact.
- High essence maintenance cost.

#### Applications
- **Stealth:** Conditional invisibility, light suppression, detection reduction.
- **Drainage:** Stealing health/mana, decaying target buffs.
- **Disruption:** Vision loss, confusion, fear induction.
- **Alchemical Use:** Entropic poisons, ritual catalysts, shadow storage.

#### Associated Status Effects
- **Hidden:** Reduces detection radius.
- **Drained:** Continuous resource loss.
- **Terrified:** Causes target hesitation or flight.

---

### Life

**Concept:** Biological growth, regeneration, and organic adaptation.

**Functional Definition:** Restores and expands organic structures, promoting health recovery and plant growth.

**Effect Type:**
- Health regeneration.
- Attribute fortification.
- Organic structure generation (vines, roots).
- Summons and flora enhancement.

#### Advantages
- High sustain and healing capacity.
- Strong synergy with organic summons and plants.
- Modifies entity stats positively.

#### Disadvantages
- Low immediate burst damage.
- Vulnerable to fire, decay, and necrotic effects.
- Slow combat ramp-up.

#### Applications
- **Healing:** Regeneration over time, stamina recovery, debuff removal.
- **Growth:** Spawning vines, roots, or wooden barriers.
- **Alchemical Use:** Health elixirs, organic growth catalysts, bio-fertilizers.

#### Associated Status Effects
- **Regenerating:** Continuous HP restoration.
- **Rooted:** Anchors entity to location, increasing defense.

---

### Death

**Concept:** Vital degradation, healing suppression, and necrotic decay.

**Functional Definition:** Degrades biological tissue, prevents health recovery, and utilizes organic remains.

**Effect Type:**
- Degenerative decay damage.
- Healing and regeneration suppression.
- Necrotic rot.
- Corpse utilization.

#### Advantages
- Counters healing and life-based effects.
- High damage over time against living targets.
- Synergizes with undead and corpse mechanics.

#### Disadvantages
- Ineffective against non-living constructs/automations.
- Low immediate burst.
- Causes negative NPC reactions.

#### Applications
- **Degeneration:** Life decay, necrosis, healing block.
- **Reuse:** Corpse reanimation, draining residual energy.
- **Alchemical Use:** Necrotic poisons, preservation agents, funeral catalysts.

#### Associated Status Effects
- **Necrosis:** Damage over time + healing reduction.
- **Exhausted:** Reduces maximum stamina pool.

---

### Mind

**Concept:** Cognitive processing, perception manipulation, and control interference.

**Functional Definition:** Alters decision-making, focus, perception, and target behavior.

**Effect Type:**
- Entity control (charm, fear, taunt).
- Illusion generation.
- Cast interruption.
- Direct psychic damage (bypasses physical armor).

#### Advantages
- Ignores physical armor and physical resistances.
- Neutralizes targets without killing them.
- High utility in group control.

#### Disadvantages
- Ineffective against mind-less constructs or simple automatons.
- High mana cost.
- Mitigated by mental resistance attributes.

#### Associated Status Effects
- **Confused:** Randomizes target movement or attacks.
- **Dominated:** Temporarily turns target into an ally.
- **Terrified:** Forces target to flee.

---

### Blood

**Concept:** Vital sacrifice, health-to-power conversion, and biological bonding.

**Functional Definition:** Consumes user health or target vitality to amplify spell power and trigger rituals.

**Effect Type:**
- Self-harm for resource gain.
- Life drain and health linking.
- Damage amplification.

#### Advantages
- Allows high burst output by sacrificing health.
- Ignores standard mana constraints.
- Synergizes with Life and Death mechanics.

#### Disadvantages
- Directly consumes user health pool.
- High risk of self-elimination.
- Increases vulnerability to execution damage.

#### Associated Status Effects
- **Bleeding:** Periodic physical health loss.
- **Blood Bound:** Links damage taken between targets.

---

## 3. Unstable Elements

### Chaos
Entropy and unpredictability. Alters spell mechanics, introducing random variations, critical shifts, or wild magic outcomes.

### Ether
Dimensional displacement and spatial transit. Enables teleportation, phased movement, and inter-planar buffering.

### Void
Annihilation and suppression. Erases active effects, silences magic casting, and strips magical fields.

### Time
Flow control and temporal manipulation. Alters action speed, cooldown rates, and effect duration.

---

## 4. Chemical Elements

### Metal
Conductivity, structural hardness, and physical reflection. Enhances armor, weapon stats, and electrical arcing.

### Crystal
Refraction, energy storage, and amplification. Used for spell focusing, mana storage, and radiant amplification.

### Poison
Biological toxicity and gradual stat degradation. Applies stacking damage over time and stat penalties.

### Acid
Corrosion and armor degradation. Strips target armor values and damages structural blocks.
