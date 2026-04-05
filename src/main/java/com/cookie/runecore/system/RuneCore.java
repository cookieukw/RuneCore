package com.cookie.runecore.system;

import com.cookie.runecore.api.*;
import com.cookie.runecore.content.CoreEffects;
import com.cookie.runecore.content.CoreEssences;
import com.cookie.runecore.content.CoreSpells;
import com.cookie.runecore.content.EquipmentRegistry;

import java.util.*;
import java.util.function.Consumer;

public class RuneCore {
    private static final RuneCore INSTANCE = new RuneCore();

    private final Map<String, Essence> essenceRegistry = new HashMap<>();
    private final Map<String, GameResource> resourceRegistry = new HashMap<>();
    private final Map<String, RuneEffect> effectRegistry = new HashMap<>();
    private final Map<String, Spell> spellRegistry = new HashMap<>();

    private final Map<String, List<Consumer<CastContext>>> eventListeners = new HashMap<>();

    private RuneCore() {
    }

    public static RuneCore get() {
        return INSTANCE;
    }

    public void registerEssence(Essence essence) {
        essenceRegistry.put(essence.id(), essence);
    }

    public void registerResource(GameResource res) {
        resourceRegistry.put(res.id(), res);
    }

    public void registerEffect(RuneEffect effect) {
        effectRegistry.put(effect.getId(), effect);
    }

    public void registerSpell(Spell spell) {
        spellRegistry.put(spell.getId(), spell);
    }

    public RuneEffect getEffect(String id) {
        return effectRegistry.get(id);
    }

    public void on(String eventType, Consumer<CastContext> listener) {
        eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    private void emit(String eventType, CastContext ctx) {
        if (eventListeners.containsKey(eventType)) {
            for (Consumer<CastContext> listener : eventListeners.get(eventType)) {
                listener.accept(ctx);
            }
        }
    }

    public void initDefaults() {
        // 1. Register Resource
        GameResource mana = new GameResource("arcane_mana", 100.0, 5.0, "burnout");
        registerResource(mana);

        // Initialize Core Content
        CoreEssences.init();
        CoreEffects.init();
        CoreSpells.init();
        EquipmentRegistry.init();
    }

    public boolean castSpell(String spellId, CastContext ctx) {
        Spell spell = spellRegistry.get(spellId);
        if (spell == null) {
            System.err.println("Spell not found: " + spellId);
            return false;
        }

        if (!spell.checkConditions(ctx)) {
            System.out.println("Spell condition failed.");
            return false;
        }

        emit("spell:pre_cast", ctx);

        // Deduct mana if source is a player
        if (ctx.source != null) {
            PlayerStats stats = new PlayerStats(ctx.source.getReference());
            for (Map.Entry<String, Integer> entry : spell.getResourceCost().entrySet()) {
                if (entry.getKey().equalsIgnoreCase("arcane_mana") || entry.getKey().equalsIgnoreCase("mana")) {
                    stats.subtractMana(entry.getValue());
                }
            }
        }

        emit("resource:consume", ctx);

        for (String effectId : spell.getEffectIds()) {
            RuneEffect effect = effectRegistry.get(effectId);
            if (effect != null) {
                effect.execute(ctx);
                emit("effect:applied", ctx);
            }
        }

        emit("spell:post_cast", ctx);
        return true;
    }
}
