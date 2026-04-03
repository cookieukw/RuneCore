package com.cookie.runecore.content;

public class EquipmentRegistry {
    public static void init() {
        registerGrimoire("grimoire_purple", "Weapon_Spellbook_Grimoire_Purple", "Purple");
        registerGrimoire("grimoire_brown", "Weapon_Spellbook_Grimoire_Brown", "Brown");

        registerStaff("staff_wood", "Weapon_Staff_Wood");
        registerStaff("staff_fire", "Weapon_Staff_Crystal_Flame");
        registerStaff("staff_ice", "Weapon_Staff_Crystal_Ice");
        registerStaff("staff_crystal", "Weapon_Staff_Crystal_Purple");
        registerStaff("staff_bone", "Weapon_Staff_Bone");
    }

    private static void registerGrimoire(String id, String assetId, String name) {
        System.out.println("[RuneCore-Equipment] Registered Grimoire: " + id + " (Asset: " + assetId + ")");
    }

    private static void registerStaff(String id, String assetId) {
        System.out.println("[RuneCore-Equipment] Registered Staff: " + id + " (Asset: " + assetId + ")");
    }
}
