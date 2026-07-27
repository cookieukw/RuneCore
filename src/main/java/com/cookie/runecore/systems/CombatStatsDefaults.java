package com.cookie.runecore.systems;

import com.cookie.runecore.systems.CombatStatsRegistry.ItemCombatData;

public class CombatStatsDefaults {

    public static void registerAll(CombatStatsRegistry registry) {
        registerArmors(registry);
        registerWeapons(registry);
    }

    // ── Armor ────────────────────────────────────────────────────────────────

    private static void registerArmors(CombatStatsRegistry registry) {
        // Tier 1 — Cloth & Wood: low armor, high MR
        armorSet(registry, "Armor_Cloth_Cotton",      5,  15);
        armorSet(registry, "Armor_Cloth_Linen",        6,  16);
        armorSet(registry, "Armor_Cloth_Wool",         5,  14);
        armorSet(registry, "Armor_Cloth_Silk",         4,  18);
        armorSet(registry, "Armor_Cloth_Cindercloth",  7,  20);
        armorSet(registry, "Armor_Wood",               8,  10);
        armorSet(registry, "Armor_Wool",               5,  14);

        // Tier 2 — Leather (light) & Copper: medium-low armor, medium MR
        armorSet(registry, "Armor_Leather_Soft",      10,  10);
        armorSet(registry, "Armor_Leather_Light",     12,   9);
        armorSet(registry, "Armor_Copper",            14,   8);

        // Tier 3 — Leather (med/heavy), Bronze, Iron: medium armor, low MR
        armorSet(registry, "Armor_Leather_Medium",    18,   7);
        armorSet(registry, "Armor_Leather_Heavy",     22,   6);
        armorSet(registry, "Armor_Leather_Raven",     20,   8);
        armorSet(registry, "Armor_Bronze",            20,   5);
        armorSet(registry, "Armor_Bronze_Ornate",     22,   6);
        armorSet(registry, "Armor_Iron",              25,   5);
        armorSet(registry, "Armor_Diving_Crude",      18,   4);

        // Tier 4 — Steel & Cobalt: medium-high armor, low MR
        armorSet(registry, "Armor_Steel",             32,   5);
        armorSet(registry, "Armor_Steel_Ancient",     35,   7);
        armorSet(registry, "Armor_Cobalt",            30,   6);

        // Tier 5 — Mithril & Thorium: high armor, medium MR
        armorSet(registry, "Armor_Mithril",           42,  14);
        armorSet(registry, "Armor_Thorium",           45,  12);

        // Tier 6 — Adamantite, Onyxium, Prisma: very high armor, medium-high MR
        armorSet(registry, "Armor_Adamantite",        55,  18);
        armorSet(registry, "Armor_Onyxium",           58,  20);
        armorSet(registry, "Armor_Prisma",            50,  22);

        // Special sets
        armorPiece(registry, "Armor_Kweebec_Chest",   15, 12, 1.0f);
        armorPiece(registry, "Armor_Kweebec_Head",     15, 12, 0.7f);
        armorSet(registry, "Armor_Trork",              16,  4);
        armorSet(registry, "Armor_Trooper",            28,  6);
        armorSetNoHands(registry, "Armor_Trooper",     28,  6);
        armorSet(registry, "Armor_QA",                 99, 99);
    }

    private static void armorSet(CombatStatsRegistry r, String prefix, float baseArmor, float baseMR) {
        armorPiece(r, prefix + "_Chest", baseArmor, baseMR, 1.0f);
        armorPiece(r, prefix + "_Head",  baseArmor, baseMR, 0.7f);
        armorPiece(r, prefix + "_Legs",  baseArmor, baseMR, 0.85f);
        armorPiece(r, prefix + "_Hands", baseArmor, baseMR, 0.5f);
    }

    private static void armorSetNoHands(CombatStatsRegistry r, String prefix, float baseArmor, float baseMR) {
        armorPiece(r, prefix + "_Chest", baseArmor, baseMR, 1.0f);
        armorPiece(r, prefix + "_Head",  baseArmor, baseMR, 0.7f);
        armorPiece(r, prefix + "_Legs",  baseArmor, baseMR, 0.85f);
    }

    private static void armorPiece(CombatStatsRegistry r, String id, float baseArmor, float baseMR, float mult) {
        r.register(id, ItemCombatData.builder()
                .armor(baseArmor * mult)
                .magicResist(baseMR * mult)
                .build());
    }

    // ── Weapons ──────────────────────────────────────────────────────────────

    private static void registerWeapons(CombatStatsRegistry registry) {
        // ─── Swords: balanced physical damage ───
        sword(registry, "Weapon_Sword_Crude",              8);
        sword(registry, "Weapon_Sword_Wood",               6);
        sword(registry, "Weapon_Sword_Bone",              10);
        sword(registry, "Weapon_Sword_Copper",            14);
        sword(registry, "Weapon_Sword_Bronze",            18);
        sword(registry, "Weapon_Sword_Bronze_Ancient",    20);
        sword(registry, "Weapon_Sword_Iron",              22);
        sword(registry, "Weapon_Sword_Scrap",             16);
        sword(registry, "Weapon_Sword_Steel",             28);
        sword(registry, "Weapon_Sword_Steel_Rusty",       24);
        sword(registry, "Weapon_Sword_Steel_Incandescent",32);
        sword(registry, "Weapon_Sword_Cobalt",            30);
        sword(registry, "Weapon_Sword_Silversteel",       34);
        sword(registry, "Weapon_Sword_Mithril",           38);
        sword(registry, "Weapon_Sword_Thorium",           42);
        sword(registry, "Weapon_Sword_Onyxium",           48);
        sword(registry, "Weapon_Sword_Adamantite",        50);
        sword(registry, "Weapon_Sword_Stone_Trork",       12);
        sword(registry, "Weapon_Sword_Cutlass",           26);
        sword(registry, "Weapon_Sword_Doomed",            36);
        sword(registry, "Weapon_Sword_Nexus",             44);
        sword(registry, "Weapon_Sword_Runic",             40);
        swordMagic(registry, "Weapon_Sword_Frost",        28, 14);

        // ─── Longswords: higher physical, slower ───
        longsword(registry, "Weapon_Longsword_Crude",         12);
        longsword(registry, "Weapon_Longsword_Copper",        20);
        longsword(registry, "Weapon_Longsword_Iron",          30);
        longsword(registry, "Weapon_Longsword_Cobalt",        40);
        longsword(registry, "Weapon_Longsword_Mithril",       50);
        longsword(registry, "Weapon_Longsword_Thorium",       55);
        longsword(registry, "Weapon_Longsword_Onyxium",       62);
        longsword(registry, "Weapon_Longsword_Adamantite",    65);
        longsword(registry, "Weapon_Longsword_Adamantite_Saurian", 68);
        longsword(registry, "Weapon_Longsword_Stone_Trork",   16);
        longsword(registry, "Weapon_Longsword_Tribal",        18);
        longsword(registry, "Weapon_Longsword_Scarab",        35);
        longsword(registry, "Weapon_Longsword_Praetorian",    45);
        longsword(registry, "Weapon_Longsword_Praetorian_NPC",45);
        longsword(registry, "Weapon_Longsword_Katana",        48);
        longswordMagic(registry, "Weapon_Longsword_Flame",    36, 18);
        longswordMagic(registry, "Weapon_Longsword_Spectral", 30, 30);
        longswordMagic(registry, "Weapon_Longsword_Void",     25, 40);

        // ─── Axes: physical + armor penetration ───
        axe(registry, "Weapon_Axe_Crude",          10,  4);
        axe(registry, "Weapon_Axe_Bone",           12,  5);
        axe(registry, "Weapon_Axe_Copper",         16,  6);
        axe(registry, "Weapon_Axe_Iron",           24,  9);
        axe(registry, "Weapon_Axe_Iron_Rusty",     20,  8);
        axe(registry, "Weapon_Axe_Cobalt",         32, 12);
        axe(registry, "Weapon_Axe_Mithril",        40, 15);
        axe(registry, "Weapon_Axe_Thorium",        44, 16);
        axe(registry, "Weapon_Axe_Onyxium",        50, 18);
        axe(registry, "Weapon_Axe_Adamantite",     52, 20);
        axe(registry, "Weapon_Axe_Stone_Trork",    14,  5);
        axe(registry, "Weapon_Axe_Tribal",         18,  7);
        axe(registry, "Weapon_Axe_Doomed",         38, 14);

        // ─── Battleaxes: heavy physical + high armor pen ───
        battleaxe(registry, "Weapon_Battleaxe_Crude",          14,  6);
        battleaxe(registry, "Weapon_Battleaxe_Copper",         22,  9);
        battleaxe(registry, "Weapon_Battleaxe_Iron",           32, 12);
        battleaxe(registry, "Weapon_Battleaxe_Steel_Rusty",    34, 13);
        battleaxe(registry, "Weapon_Battleaxe_Cobalt",         42, 16);
        battleaxe(registry, "Weapon_Battleaxe_Mithril",        52, 20);
        battleaxe(registry, "Weapon_Battleaxe_Thorium",        56, 22);
        battleaxe(registry, "Weapon_Battleaxe_Onyxium",        64, 24);
        battleaxe(registry, "Weapon_Battleaxe_Adamantite",     68, 26);
        battleaxe(registry, "Weapon_Battleaxe_Stone_Trork",    18,  7);
        battleaxe(registry, "Weapon_Battleaxe_Tribal",         24, 10);
        battleaxe(registry, "Weapon_Battleaxe_Scarab",         46, 18);
        battleaxe(registry, "Weapon_Battleaxe_Doomed",         50, 20);
        battleaxe(registry, "Weapon_Battleaxe_Wood_Fence",      8,  3);
        battleaxeMagic(registry, "Weapon_Battleaxe_Scythe_Void", 40, 30, 22);

        // ─── Maces & Clubs: blunt physical ───
        club(registry, "Weapon_Club_Crude",            10);
        club(registry, "Weapon_Club_Scrap",            14);
        club(registry, "Weapon_Club_Copper",           18);
        club(registry, "Weapon_Club_Iron",             26);
        club(registry, "Weapon_Club_Iron_Rusty",       22);
        club(registry, "Weapon_Club_Cobalt",           34);
        club(registry, "Weapon_Club_Mithril",          42);
        club(registry, "Weapon_Club_Thorium",          46);
        club(registry, "Weapon_Club_Onyxium",          52);
        club(registry, "Weapon_Club_Adamantite",       54);
        club(registry, "Weapon_Club_Stone_Trork",      14);
        club(registry, "Weapon_Club_Tribal",           16);
        club(registry, "Weapon_Club_Doomed",           38);
        club(registry, "Weapon_Club_Zombie_Arm",        8);
        club(registry, "Weapon_Club_Zombie_Leg",        8);
        club(registry, "Weapon_Club_Zombie_Burnt_Arm", 10);
        club(registry, "Weapon_Club_Zombie_Burnt_Leg", 10);
        club(registry, "Weapon_Club_Zombie_Frost_Arm", 10);
        club(registry, "Weapon_Club_Zombie_Frost_Leg", 10);
        club(registry, "Weapon_Club_Zombie_Sand_Arm",  10);
        club(registry, "Weapon_Club_Zombie_Sand_Leg",  10);
        club(registry, "Weapon_Club_Steel_Flail_Rusty",30);

        mace(registry, "Weapon_Mace_Crude",            12);
        mace(registry, "Weapon_Mace_Copper",           20);
        mace(registry, "Weapon_Mace_Iron",             28);
        mace(registry, "Weapon_Mace_Cobalt",           36);
        mace(registry, "Weapon_Mace_Mithril",          44);
        mace(registry, "Weapon_Mace_Thorium",          48);
        mace(registry, "Weapon_Mace_Onyxium",          54);
        mace(registry, "Weapon_Mace_Adamantite",       56);
        mace(registry, "Weapon_Mace_Stone_Trork",      16);
        mace(registry, "Weapon_Mace_Scrap",            18);
        mace(registry, "Weapon_Mace_Scrap_NPC",        18);
        mace(registry, "Weapon_Mace_Prisma",           50);

        // ─── Spears: physical + magic penetration ───
        spear(registry, "Weapon_Spear_Crude",              10,  4);
        spear(registry, "Weapon_Spear_Bone",               12,  5);
        spear(registry, "Weapon_Spear_Copper",             16,  6);
        spear(registry, "Weapon_Spear_Bronze",             20,  8);
        spear(registry, "Weapon_Spear_Iron",               24,  9);
        spear(registry, "Weapon_Spear_Scrap",              18,  7);
        spear(registry, "Weapon_Spear_Cobalt",             32, 12);
        spear(registry, "Weapon_Spear_Mithril",            40, 15);
        spear(registry, "Weapon_Spear_Thorium",            44, 16);
        spear(registry, "Weapon_Spear_Onyxium",            50, 18);
        spear(registry, "Weapon_Spear_Adamantite",         52, 20);
        spear(registry, "Weapon_Spear_Adamantite_Saurian", 55, 22);
        spear(registry, "Weapon_Spear_Stone_Trork",        14,  5);
        spear(registry, "Weapon_Spear_Tribal",             16,  6);
        spear(registry, "Weapon_Spear_Leaf",               18,  8);
        spear(registry, "Weapon_Spear_Fishbone",           14,  6);
        spearMagic(registry, "Weapon_Spear_Double_Incandescent", 35, 20, 18);

        // ─── Daggers: low physical + high armor penetration ───
        dagger(registry, "Weapon_Daggers_Crude",              6, 10);
        dagger(registry, "Weapon_Daggers_Bone",               8, 12);
        dagger(registry, "Weapon_Daggers_Copper",            10, 15);
        dagger(registry, "Weapon_Daggers_Bronze",            14, 18);
        dagger(registry, "Weapon_Daggers_Bronze_Ancient",    16, 20);
        dagger(registry, "Weapon_Daggers_Iron",              18, 22);
        dagger(registry, "Weapon_Daggers_Cobalt",            24, 28);
        dagger(registry, "Weapon_Daggers_Mithril",           30, 34);
        dagger(registry, "Weapon_Daggers_Thorium",           34, 38);
        dagger(registry, "Weapon_Daggers_Onyxium",           38, 42);
        dagger(registry, "Weapon_Daggers_Adamantite",        40, 45);
        dagger(registry, "Weapon_Daggers_Adamantite_Saurian",42, 48);
        dagger(registry, "Weapon_Daggers_Stone_Trork",       10, 14);
        dagger(registry, "Weapon_Daggers_Claw_Bone",         12, 16);
        dagger(registry, "Weapon_Daggers_Doomed",            28, 32);
        dagger(registry, "Weapon_Daggers_Fang_Doomed",       26, 35);

        // ─── Staffs: magic damage ───
        staff(registry, "Weapon_Staff_Wood",               8);
        staff(registry, "Weapon_Staff_Wood_Rotten",        6);
        staff(registry, "Weapon_Staff_Wood_Kweebec",      10);
        staff(registry, "Weapon_Staff_Bo_Wood",            8);
        staff(registry, "Weapon_Staff_Bo_Bamboo",         10);
        staff(registry, "Weapon_Staff_Bone",              14);
        staff(registry, "Weapon_Staff_Cane",              12);
        staff(registry, "Weapon_Staff_Onion",             10);
        staff(registry, "Weapon_Staff_Copper",            18);
        staff(registry, "Weapon_Staff_Bronze",            22);
        staff(registry, "Weapon_Staff_Iron",              28);
        staff(registry, "Weapon_Staff_Cobalt",            36);
        staff(registry, "Weapon_Staff_Mithril",           44);
        staff(registry, "Weapon_Staff_Thorium",           48);
        staff(registry, "Weapon_Staff_Onyxium",           54);
        staff(registry, "Weapon_Staff_Adamantite",        58);
        staff(registry, "Weapon_Staff_Doomed",            40);
        staff(registry, "Weapon_Staff_Wizard",            42);
        staffElemental(registry, "Weapon_Staff_Frost",            34, 10);
        staffElemental(registry, "Weapon_Staff_Crystal_Flame",    38, 12);
        staffElemental(registry, "Weapon_Staff_Crystal_Fire_Trork",20,  8);
        staffElemental(registry, "Weapon_Staff_Crystal_Ice",      36, 10);
        staffElemental(registry, "Weapon_Staff_Crystal_Purple",   40, 14);
        staffElemental(registry, "Weapon_Staff_Crystal_Red",      38, 12);

        // ─── Spellbooks: high magic damage + magic penetration ───
        spellbook(registry, "Weapon_Spellbook_Fire",              45, 15);
        spellbook(registry, "Weapon_Spellbook_Frost",             45, 15);
        spellbook(registry, "Weapon_Spellbook_Demon",             55, 20);
        spellbook(registry, "Weapon_Spellbook_Grimoire_Brown",    35, 10);
        spellbook(registry, "Weapon_Spellbook_Grimoire_Purple",   40, 12);
        spellbook(registry, "Weapon_Spellbook_Rekindle_Embers",   50, 18);

        // ─── Wands: light magic damage ───
        wand(registry, "Weapon_Wand_Wood",           12);
        wand(registry, "Weapon_Wand_Wood_Rotten",    10);
        wand(registry, "Weapon_Wand_Root",           16);
        wand(registry, "Weapon_Wand_Tribal",         14);
        wand(registry, "Weapon_Wand_Stoneskin",      20);

        // ─── Bows & Crossbows: ranged physical ───
        bow(registry, "Weapon_Shortbow_Crude",         10);
        bow(registry, "Weapon_Shortbow_Copper",        16);
        bow(registry, "Weapon_Shortbow_Bronze",        20);
        bow(registry, "Weapon_Shortbow_Iron",          24);
        bow(registry, "Weapon_Shortbow_Iron_Rusty",    20);
        bow(registry, "Weapon_Shortbow_Combat",        28);
        bow(registry, "Weapon_Shortbow_Cobalt",        32);
        bow(registry, "Weapon_Shortbow_Mithril",       40);
        bow(registry, "Weapon_Shortbow_Thorium",       44);
        bow(registry, "Weapon_Shortbow_Onyxium",       50);
        bow(registry, "Weapon_Shortbow_Adamantite",    52);
        bow(registry, "Weapon_Shortbow_Doomed",        36);
        bow(registry, "Weapon_Shortbow_Pull",          30);
        bow(registry, "Weapon_Shortbow_Ricochet",      28);
        bowMagic(registry, "Weapon_Shortbow_Flame",    26, 14);
        bowMagic(registry, "Weapon_Shortbow_Frost",    26, 14);
        bowMagic(registry, "Weapon_Shortbow_Bomb",     20, 20);
        bowMagic(registry, "Weapon_Shortbow_Vampire",  22, 18);
        bow(registry, "Weapon_Crossbow_Iron",          30);
        bow(registry, "Weapon_Crossbow_Ancient_Steel", 38);

        // ─── Shields: armor + shield HP ───
        shield(registry, "Weapon_Shield_Wood",                 8,  30);
        shield(registry, "Weapon_Shield_Scrap",               12,  40);
        shield(registry, "Weapon_Shield_Scrap_Spiked",        14,  40);
        shield(registry, "Weapon_Shield_Rusty",               16,  50);
        shield(registry, "Weapon_Shield_Copper",              18,  60);
        shield(registry, "Weapon_Shield_Iron",                24,  80);
        shield(registry, "Weapon_Shield_Cobalt",              30, 100);
        shield(registry, "Weapon_Shield_Mithril",             38, 130);
        shield(registry, "Weapon_Shield_Thorium",             42, 150);
        shield(registry, "Weapon_Shield_Onyxium",             48, 170);
        shield(registry, "Weapon_Shield_Adamantite",          52, 200);
        shield(registry, "Weapon_Shield_Doomed",              35, 120);
        shield(registry, "Weapon_Shield_Praetorian",          40, 140);
        shield(registry, "Weapon_Shield_Orbis_Knight",        36, 130);
        shield(registry, "Weapon_Shield_Orbis_Incandescent",  38, 140);

        // ─── Special / misc ───
        registry.register("Weapon_Kunai", ItemCombatData.builder()
                .physicalDamage(12).armorPenetration(20).build());
        registry.register("Weapon_Blowgun_Tribal", ItemCombatData.builder()
                .physicalDamage(8).magicDamage(6).build());
        registry.register("Weapon_Claws_Tribal", ItemCombatData.builder()
                .physicalDamage(16).armorPenetration(18).build());
        registry.register("Weapon_Bomb", ItemCombatData.builder()
                .physicalDamage(30).magicDamage(10).build());
        registry.register("Weapon_Bomb_Fire", ItemCombatData.builder()
                .physicalDamage(20).magicDamage(25).build());
        registry.register("Weapon_Bomb_Large_Fire", ItemCombatData.builder()
                .physicalDamage(30).magicDamage(35).build());
        registry.register("Weapon_Bomb_Stun", ItemCombatData.builder()
                .physicalDamage(15).magicDamage(15).build());
        registry.register("Weapon_Bomb_Continuous", ItemCombatData.builder()
                .physicalDamage(25).magicDamage(10).build());
        registry.register("Weapon_Bomb_Popberry", ItemCombatData.builder()
                .physicalDamage(10).build());
        registry.register("Weapon_Bomb_Potion_Poison", ItemCombatData.builder()
                .magicDamage(20).magicPenetration(10).build());
        registry.register("Weapon_Grenade_Frag", ItemCombatData.builder()
                .physicalDamage(35).armorPenetration(15).build());
        registry.register("Weapon_Gun", ItemCombatData.builder()
                .physicalDamage(30).armorPenetration(20).build());
        registry.register("Weapon_Gun_Blunderbuss", ItemCombatData.builder()
                .physicalDamage(40).armorPenetration(10).build());
        registry.register("Weapon_Gun_Blunderbuss_Rusty", ItemCombatData.builder()
                .physicalDamage(32).armorPenetration(8).build());
        registry.register("Weapon_Handgun", ItemCombatData.builder()
                .physicalDamage(22).armorPenetration(18).build());
        registry.register("Weapon_Assault_Rifle", ItemCombatData.builder()
                .physicalDamage(28).armorPenetration(25).build());
        registry.register("Weapon_Dart_Tribal", ItemCombatData.builder()
                .physicalDamage(6).magicDamage(4).build());

        // Deployables
        registry.register("Weapon_Deployable_Turret", ItemCombatData.builder()
                .physicalDamage(20).build());
        registry.register("Weapon_Deployable_Healing_Totem", ItemCombatData.builder()
                .magicDamage(0).build());
        registry.register("Weapon_Deployable_Slowness_Totem", ItemCombatData.builder()
                .magicDamage(5).build());

        // RuneCore custom
        registry.register("Weapon_Staff_Thorium", ItemCombatData.builder()
                .magicDamage(48).magicPenetration(12).build());
    }

    // ── Weapon helpers ───────────────────────────────────────────────────────

    private static void sword(CombatStatsRegistry r, String id, float phys) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).build());
    }

    private static void swordMagic(CombatStatsRegistry r, String id, float phys, float mag) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicDamage(mag).build());
    }

    private static void longsword(CombatStatsRegistry r, String id, float phys) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).build());
    }

    private static void longswordMagic(CombatStatsRegistry r, String id, float phys, float mag) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicDamage(mag).build());
    }

    private static void axe(CombatStatsRegistry r, String id, float phys, float apen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).armorPenetration(apen).build());
    }

    private static void battleaxe(CombatStatsRegistry r, String id, float phys, float apen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).armorPenetration(apen).build());
    }

    private static void battleaxeMagic(CombatStatsRegistry r, String id, float phys, float mag, float apen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicDamage(mag).armorPenetration(apen).build());
    }

    private static void club(CombatStatsRegistry r, String id, float phys) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).build());
    }

    private static void mace(CombatStatsRegistry r, String id, float phys) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).build());
    }

    private static void spear(CombatStatsRegistry r, String id, float phys, float mpen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicPenetration(mpen).build());
    }

    private static void spearMagic(CombatStatsRegistry r, String id, float phys, float mag, float mpen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicDamage(mag).magicPenetration(mpen).build());
    }

    private static void dagger(CombatStatsRegistry r, String id, float phys, float apen) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).armorPenetration(apen).build());
    }

    private static void staff(CombatStatsRegistry r, String id, float mag) {
        r.register(id, ItemCombatData.builder().magicDamage(mag).build());
    }

    private static void staffElemental(CombatStatsRegistry r, String id, float mag, float mpen) {
        r.register(id, ItemCombatData.builder().magicDamage(mag).magicPenetration(mpen).build());
    }

    private static void spellbook(CombatStatsRegistry r, String id, float mag, float mpen) {
        r.register(id, ItemCombatData.builder().magicDamage(mag).magicPenetration(mpen).build());
    }

    private static void wand(CombatStatsRegistry r, String id, float mag) {
        r.register(id, ItemCombatData.builder().magicDamage(mag).build());
    }

    private static void bow(CombatStatsRegistry r, String id, float phys) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).build());
    }

    private static void bowMagic(CombatStatsRegistry r, String id, float phys, float mag) {
        r.register(id, ItemCombatData.builder().physicalDamage(phys).magicDamage(mag).build());
    }

    private static void shield(CombatStatsRegistry r, String id, float armor, float shieldHP) {
        r.register(id, ItemCombatData.builder().armor(armor).build());
    }
}
