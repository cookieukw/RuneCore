package com.cookie.runecore;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.cookie.runecore.commands.CombatStatsCommand;
import com.cookie.runecore.commands.RuneStatsCommand;
import com.cookie.runecore.commands.TestUICommand;
import com.cookie.runecore.systems.CastListener;
import com.cookie.runecore.systems.CombatDamageInterceptor;
import com.cookie.runecore.systems.CombatStatsManager;
import com.cookie.runecore.systems.CombatStatsDefaults;
import com.cookie.runecore.systems.CombatStatsRegistry;
import com.cookie.runecore.systems.EquipmentStatsListener;
import com.cookie.runecore.systems.EffectTimerListener;
import com.cookie.runecore.systems.FrozenInteractionListener;
import com.cookie.runecore.systems.MobDropSystem;
import com.cookie.runecore.systems.GenericPotionSplashInteraction;
import com.cookie.runecore.systems.PotionDrinkInteraction;
import com.cookie.runecore.systems.PotionHitSystem;
import com.cookie.runecore.systems.PotionListener;
import com.cookie.runecore.systems.ui.RuneCoreHudManager;
import com.cookie.runemagic.MagicListener;
import com.cookie.runemagic.SwitchSpellCommand;
import com.cookie.runecore.commands.CustomTimeCommand;
import com.cookie.runecore.commands.RuneCommand;
import com.cookie.runecore.api.PlayerDataComponent;
import com.cookie.runecore.system.RuneCore;

import javax.annotation.Nonnull;

public class Main extends JavaPlugin {

    public Main(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Register custom interaction codecs
        com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction.CODEC.register(
                "runecore:potion_drink",
                PotionDrinkInteraction.class,
                PotionDrinkInteraction.CODEC
        );

        Interaction.CODEC.register(
                "runecore:potion_splash_generic",
                GenericPotionSplashInteraction.class,
                GenericPotionSplashInteraction.CODEC
        );

        String[] splashEffects = {
            "speed", "slowness", "haste", "mining_fatigue", "jump_boost", "high_jump",
            "slow_falling", "levitation", "regeneration", "poison", "decay", "burn",
            "nausea", "bleeding", "frozen", "invisibility", "glowing", "blindness",
            "night_vision", "water_breathing", "fire_resistance", "resistance",
            "strength", "weakness", "instant_health", "instant_damage"
        };
        for (String eff : splashEffects) {
            Interaction.CODEC.register(
                    "runecore:potion_splash_" + eff,
                    GenericPotionSplashInteraction.class,
                    GenericPotionSplashInteraction.CODEC
            );
        }

        // Initialize RuneCore engine defaults
        RuneCore.get().initDefaults();

        this.getCommandRegistry().registerCommand(new RuneStatsCommand());
        this.getCommandRegistry().registerCommand(new CombatStatsCommand());
        this.getCommandRegistry().registerCommand(new CustomTimeCommand());
        this.getCommandRegistry().registerCommand(new TestUICommand());
        this.getCommandRegistry().registerCommand(new SwitchSpellCommand());
        this.getCommandRegistry().registerCommand(new RuneCommand());

        // Register custom player data component
        PlayerDataComponent.TYPE = this.getEntityStoreRegistry().registerComponent(
                PlayerDataComponent.class,
                PlayerDataComponent.COMPONENT_ID,
                PlayerDataComponent.CODEC);

        this.getEntityStoreRegistry().registerSystem(new MobDropSystem());
        PotionHitSystem potionHitSystem = new PotionHitSystem();
        this.getEntityStoreRegistry().registerSystem(potionHitSystem);
        this.getEntityStoreRegistry().registerSystem(new com.cookie.runecore.systems.EffectTickSystemBridge());
        new PotionListener(this.getEventRegistry(), potionHitSystem.getPlayerPotions());
        new RuneCoreHudManager(this.getEventRegistry());
        new CastListener(this.getEventRegistry());
        new EffectTimerListener(this.getEventRegistry());
        new MagicListener(this.getEventRegistry());
        new FrozenInteractionListener(this.getEventRegistry());
        new CombatStatsManager(this.getEventRegistry());
        CombatStatsRegistry combatRegistry = new CombatStatsRegistry();
        CombatStatsDefaults.registerAll(combatRegistry);
        this.getEntityStoreRegistry().registerSystem(new CombatDamageInterceptor());
        this.getEntityStoreRegistry().registerSystem(new EquipmentStatsListener());
    }
}
