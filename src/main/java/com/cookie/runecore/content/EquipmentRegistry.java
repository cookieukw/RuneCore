package com.cookie.runecore.content;

import java.util.HashMap;
import java.util.Map;

public class EquipmentRegistry {
    private static final Map<String, String> grimoireAssets = new HashMap<>();
    private static final Map<String, String> staffAssets = new HashMap<>();

    public static void init() {
        registerGrimoire("grimoire_purple", "hytale:weapon_spellbook_grimoire_purple", "Purple");
        registerGrimoire("grimoire_brown", "hytale:weapon_spellbook_grimoire_brown", "Brown");

        registerStaff("staff_wood", "hytale:weapon_staff_wood");
        registerStaff("staff_fire", "hytale:weapon_staff_crystal_flame");
        registerStaff("staff_ice", "hytale:weapon_staff_crystal_ice");
        registerStaff("staff_crystal", "hytale:weapon_staff_crystal_purple");
        registerStaff("staff_bone", "hytale:weapon_staff_bone");
    }

    private static void registerGrimoire(String id, String assetId, String name) {
        grimoireAssets.put(id, assetId);
        System.out.println("[RuneCore-Equipment] Registered Grimoire: " + id + " (Asset: " + assetId + ")");
    }

    private static void registerStaff(String id, String assetId) {
        staffAssets.put(id, assetId);
        System.out.println("[RuneCore-Equipment] Registered Staff: " + id + " (Asset: " + assetId + ")");
    }

    public static String getGrimoireAsset(String id) {
        return grimoireAssets.get(id);
    }

    public static String getStaffAsset(String id) {
        return staffAssets.get(id);
    }

    public static Map<String, String> getGrimoires() {
        return grimoireAssets;
    }

    public static Map<String, String> getStaves() {
        return staffAssets;
    }
}
