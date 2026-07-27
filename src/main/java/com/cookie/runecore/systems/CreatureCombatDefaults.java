package com.cookie.runecore.systems;

import static com.cookie.runecore.systems.CreatureCombatRegistry.CreatureCombatData.*;

public class CreatureCombatDefaults {

    public static void registerAll(CreatureCombatRegistry r) {
        registerTrorks(r);
        registerSkeletons(r);
        registerZombies(r);
        registerGoblins(r);
        registerOutlanders(r);
        registerScaraks(r);
        registerFerans(r);
        registerGolems(r);
        registerVoidCreatures(r);
        registerDragons(r);
        registerBosses(r);
        registerWildlife(r);
        registerSpirits(r);
        registerMisc(r);
    }

    // ── Trorks: tribal warriors, mostly physical ─────────────────────────────

    private static void registerTrorks(CreatureCombatRegistry r) {
        r.register("Trork",               physical());
        r.register("Trork_Warrior",        physical(5));
        r.register("Trork_Brawler",        physical(8));
        r.register("Trork_Guard",          physical(4));
        r.register("Trork_Sentry",         physical(3));
        r.register("Trork_Hunter",         physical(6));
        r.register("Trork_Mauler",         physical(12));
        r.register("Trork_Chieftain",      physical(15));
        r.register("Trork_Shaman",         magic(10));
        r.register("Trork_Doctor_Witch",   magic(12));
        r.register("Trork_Christmas",      physical());
    }

    // ── Skeletons: varied classes ────────────────────────────────────────────

    private static void registerSkeletons(CreatureCombatRegistry r) {
        // Standard
        r.register("Skeleton",                     physical());
        r.register("Skeleton_Fighter",             physical(4));
        r.register("Skeleton_Soldier",             physical(6));
        r.register("Skeleton_Scout",               physical(3));
        r.register("Skeleton_Ranger",              physical(5));
        r.register("Skeleton_Archer",              physical(4));
        r.register("Skeleton_Knight",              physical(10));
        r.register("Skeleton_Mage",                magic(8));
        r.register("Skeleton_Archmage",            magic(15));

        // Burnt
        r.register("Skeleton_Burnt_Soldier",       hybrid(0.3f, 5, 3));
        r.register("Skeleton_Burnt_Knight",        hybrid(0.3f, 8, 5));
        r.register("Skeleton_Burnt_Lancer",        hybrid(0.3f, 6, 3));
        r.register("Skeleton_Burnt_Archer",        hybrid(0.3f, 4, 2));
        r.register("Skeleton_Burnt_Gunner",        physical(8));
        r.register("Skeleton_Burnt_Wizard",        magic(12));
        r.register("Skeleton_Burnt_Alchemist",     magic(10));
        r.register("Skeleton_Burnt_Praetorian",    hybrid(0.2f, 12, 5));

        // Frost
        r.register("Skeleton_Frost_Fighter",       hybrid(0.4f, 3, 5));
        r.register("Skeleton_Frost_Soldier",       hybrid(0.4f, 5, 6));
        r.register("Skeleton_Frost_Scout",         hybrid(0.4f, 2, 4));
        r.register("Skeleton_Frost_Ranger",        hybrid(0.4f, 4, 5));
        r.register("Skeleton_Frost_Archer",        hybrid(0.4f, 3, 4));
        r.register("Skeleton_Frost_Knight",        hybrid(0.3f, 8, 8));
        r.register("Skeleton_Frost_Mage",          magic(12));
        r.register("Skeleton_Frost_Archmage",      magic(18));

        // Sand
        r.register("Skeleton_Sand_Soldier",        physical(6));
        r.register("Skeleton_Sand_Guard",          physical(8));
        r.register("Skeleton_Sand_Scout",          physical(3));
        r.register("Skeleton_Sand_Ranger",         physical(5));
        r.register("Skeleton_Sand_Archer",         physical(4));
        r.register("Skeleton_Sand_Assassin",       physical(14));
        r.register("Skeleton_Sand_Mage",           magic(10));
        r.register("Skeleton_Sand_Archmage",       magic(16));

        // Incandescent
        r.register("Skeleton_Incandescent_Fighter",  hybrid(0.5f, 6, 8));
        r.register("Skeleton_Incandescent_Footman",  hybrid(0.5f, 8, 8));
        r.register("Skeleton_Incandescent_Mage",     magic(14));
        r.register("Skeleton_Incandescent_Head",     magic(6));

        // Pirate
        r.register("Skeleton_Pirate_Captain",      physical(10));
        r.register("Skeleton_Pirate_Gunner",       physical(12));
        r.register("Skeleton_Pirate_Striker",      physical(8));
    }

    // ── Zombies ──────────────────────────────────────────────────────────────

    private static void registerZombies(CreatureCombatRegistry r) {
        r.register("Zombie",               physical());
        r.register("Zombie_Aberrant",       physical(4));
        r.register("Zombie_Aberrant_Big",   physical(8));
        r.register("Zombie_Aberrant_Small", physical(2));
        r.register("Zombie_Burnt",          hybrid(0.3f, 3, 3));
        r.register("Zombie_Frost",          hybrid(0.4f, 2, 5));
        r.register("Zombie_Sand",           physical(3));
        r.register("Zombie_Werewolf",       physical(10));
    }

    // ── Goblins ──────────────────────────────────────────────────────────────

    private static void registerGoblins(CreatureCombatRegistry r) {
        r.register("Goblin",               physical());
        r.register("Goblin_Scrapper",      physical(3));
        r.register("Goblin_Thief",         physical(8));
        r.register("Goblin_Miner",         physical(5));
        r.register("Goblin_Lobber",        hybrid(0.5f));
        r.register("Goblin_Hermit",        magic(5));
        r.register("Goblin_Boss",          physical(12));
        r.register("Goblin_Duke",          hybrid(0.3f, 10, 8));
        r.register("Goblin_Duke_Large",    hybrid(0.3f, 15, 10));
        r.register("Goblin_Ogre",          physical(10));
    }

    // ── Outlanders ───────────────────────────────────────────────────────────

    private static void registerOutlanders(CreatureCombatRegistry r) {
        r.register("Outlander",            physical(3));
        r.register("Outlander_Peon",       physical(2));
        r.register("Outlander_Marauder",   physical(8));
        r.register("Outlander_Berserker",  physical(12));
        r.register("Outlander_Brute",      physical(15));
        r.register("Outlander_Hunter",     physical(6));
        r.register("Outlander_Stalker",    physical(10));
        r.register("Outlander_Cultist",    magic(8));
        r.register("Outlander_Priest",     magic(12));
        r.register("Outlander_Sorcerer",   magic(15));
    }

    // ── Scaraks: insectoid, physical with some pen ───────────────────────────

    private static void registerScaraks(CreatureCombatRegistry r) {
        r.register("Scarak_Louse",                   physical());
        r.register("Scarak_Seeker",                  physical(3));
        r.register("Scarak_Fighter",                 physical(6));
        r.register("Scarak_Fighter_Royal_Guard",     physical(10));
        r.register("Scarak_Defender",                physical(4));
        r.register("Scarak_Broodmother_Young",       physical(12));
        r.register("Scarak_Broodmother",             hybrid(0.3f, 18, 10));
    }

    // ── Ferans: beast warriors ───────────────────────────────────────────────

    private static void registerFerans(CreatureCombatRegistry r) {
        r.register("Feran",                physical(3));
        r.register("Feran_Burrower",       physical(5));
        r.register("Feran_Longtooth",      physical(8));
        r.register("Feran_Sharptooth",     physical(10));
        r.register("Feran_Windwalker",     hybrid(0.5f, 5, 8));
    }

    // ── Golems: heavy hitters ────────────────────────────────────────────────

    private static void registerGolems(CreatureCombatRegistry r) {
        r.register("Golem_Crystal_Earth",    physical(15));
        r.register("Golem_Crystal_Flame",    hybrid(0.6f, 10, 12));
        r.register("Golem_Crystal_Frost",    hybrid(0.6f, 10, 12));
        r.register("Golem_Crystal_Sand",     physical(12));
        r.register("Golem_Crystal_Thunder",  hybrid(0.7f, 8, 15));
        r.register("Golem_Firesteel",        hybrid(0.4f, 18, 10));
        r.register("Golem_Guardian_Void",    hybrid(0.5f, 15, 15));
    }

    // ── Void creatures: magic-heavy ──────────────────────────────────────────

    private static void registerVoidCreatures(CreatureCombatRegistry r) {
        r.register("Crawler_Void",          hybrid(0.6f, 5, 8));
        r.register("Eye_Void",             magic(10));
        r.register("Larva_Void",           magic(4));
        r.register("Necromancer_Void",     magic(20));
        r.register("Spawn_Void",           magic(6));
        r.register("Spectre_Void",         magic(15));
        r.register("Wraith",               magic(12));
        r.register("Wraith_Lantern",       magic(14));
    }

    // ── Dragons: boss-tier, hybrid ───────────────────────────────────────────

    private static void registerDragons(CreatureCombatRegistry r) {
        r.register("Dragon_Fire",   hybrid(0.6f, 15, 20));
        r.register("Dragon_Frost",  hybrid(0.6f, 15, 20));
        r.register("Dragon_Void",   hybrid(0.7f, 20, 25));
    }

    // ── Bosses & elite mobs ──────────────────────────────────────────────────

    private static void registerBosses(CreatureCombatRegistry r) {
        r.register("Shadow_Knight",            hybrid(0.4f, 20, 15));
        r.register("Temple_Mithril_Guard",     physical(18));
        r.register("Yeti",                     hybrid(0.3f, 15, 10));
        r.register("Werewolf",                 physical(14));
        r.register("Klops",                    physical(8));
        r.register("Klops_Gentleman",          physical(8));
        r.register("Emberwulf",                hybrid(0.5f, 10, 12));
    }

    // ── Hostile wildlife ─────────────────────────────────────────────────────

    private static void registerWildlife(CreatureCombatRegistry r) {
        r.register("Bear_Grizzly",       physical(5));
        r.register("Bear_Polar",         physical(6));
        r.register("Boar",               physical(2));
        r.register("Crocodile",          physical(8));
        r.register("Hyena",              physical(3));
        r.register("Leopard_Snow",       physical(4));
        r.register("Tiger_Sabertooth",   physical(10));
        r.register("Wolf_Black",         physical(3));
        r.register("Wolf_White",         physical(3));
        r.register("Wolf_Outlander_Priest",    magic(5));
        r.register("Wolf_Outlander_Sorcerer",  magic(8));
        r.register("Wolf_Trork_Hunter",        physical(4));
        r.register("Wolf_Trork_Shaman",        magic(6));
        r.register("Spider",             physical(2));
        r.register("Spider_Cave",        physical(4));
        r.register("Scorpion",           physical(5));
        r.register("Snake_Cobra",        hybrid(0.5f));
        r.register("Snake_Marsh",        hybrid(0.5f));
        r.register("Snake_Rattle",       hybrid(0.5f));
        r.register("Shark_Hammerhead",   physical(10));
        r.register("Piranha",            physical(2));
        r.register("Piranha_Black",      physical(3));
        r.register("Bat",                physical());
        r.register("Bat_Ice",            hybrid(0.5f, 0, 3));
        r.register("Crab",               physical(2));
        r.register("Raptor_Cave",        physical(8));
        r.register("Rex_Cave",           physical(15));
        r.register("Pterodactyl",        physical(6));
        r.register("Trillodon",          physical(4));
        r.register("Snapdragon",         hybrid(0.4f, 3, 5));
        r.register("Snapjaw",            physical(6));
        r.register("Toad_Rhino",         physical(8));
        r.register("Toad_Rhino_Magma",   hybrid(0.4f, 6, 5));
        r.register("Pig_Wild",           physical());
        r.register("Warthog",            physical(2));
        r.register("Ram",                physical(3));
        r.register("Moose_Bull",         physical(4));
        r.register("Mosshorn",           physical(5));
        r.register("Mosshorn_Plain",     physical(4));
        r.register("Bramblekin",         physical(2));
        r.register("Bramblekin_Shaman",  magic(6));
        r.register("Slug_Magma",         hybrid(0.6f));
        r.register("Snail_Frost",        hybrid(0.5f));
        r.register("Snail_Magma",        hybrid(0.6f));
        r.register("Shellfish_Lava",     hybrid(0.5f, 5, 3));
        r.register("Fen_Stalker",        hybrid(0.4f, 4, 6));
        r.register("Hound_Bleached",     physical(6));
        r.register("Horse_Skeleton",         physical(4));
        r.register("Horse_Skeleton_Armored", physical(6));
        r.register("Chicken_Undead",     physical());
        r.register("Cow_Undead",         physical(2));
        r.register("Pig_Undead",         physical(2));
    }

    // ── Spirits & elementals ─────────────────────────────────────────────────

    private static void registerSpirits(CreatureCombatRegistry r) {
        r.register("Spirit_Ember",     magic(8));
        r.register("Spirit_Frost",     magic(8));
        r.register("Spirit_Root",      magic(6));
        r.register("Spirit_Thunder",   magic(10));
        r.register("Spark_Living",     magic(5));
        r.register("Cactee",           physical(3));
        r.register("Hedera",           hybrid(0.5f, 2, 4));
        r.register("Mushee",           magic(3));
        r.register("Wurmling_Frost",   hybrid(0.6f, 3, 8));
    }

    // ── Misc hostile ─────────────────────────────────────────────────────────

    private static void registerMisc(CreatureCombatRegistry r) {
        r.register("Saurian",          physical(4));
        r.register("Saurian_Warrior",  physical(8));
        r.register("Saurian_Hunter",   physical(6));
        r.register("Saurian_Rogue",    physical(10));
        r.register("Ghoul",            hybrid(0.4f, 5, 8));
        r.register("Molerat",          physical(2));
        r.register("Rat",              physical());
        r.register("Skrill",           physical(3));
        r.register("Grooble",          physical(2));
        r.register("Hatworm",          physical());
        r.register("Tuluk",            physical(3));
        r.register("Jellyfish_Man_Of_War", magic(6));
        r.register("Jellyfish_Red",    magic(3));
        r.register("Eel_Moray",        physical(4));
        r.register("Pufferfish",       hybrid(0.5f));
        r.register("Slothian_Warrior", physical(5));
        r.register("Slothian_Scout",   physical(3));
        r.register("Kweebec_Sapling_Razorleaf", physical(4));
        r.register("Crossbow_Turret",  physical(8));
    }
}
