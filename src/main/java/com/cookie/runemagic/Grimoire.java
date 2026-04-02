package com.cookie.runemagic;

import java.util.ArrayList;
import java.util.List;

public class Grimoire {
    private final String id;
    private final List<String> spellIds;
    private int activeSpellIndex = 0;

    public Grimoire(String id) {
        this.id = id;
        this.spellIds = new ArrayList<>();
    }

    public void addSpell(String spellId) {
        if (!spellIds.contains(spellId)) {
            spellIds.add(spellId);
        }
    }

    public void removeSpell(String spellId) {
        spellIds.remove(spellId);
    }

    public List<String> getSpellIds() {
        return spellIds;
    }

    public String getActiveSpellId() {
        if (spellIds.isEmpty())
            return null;
        return spellIds.get(activeSpellIndex % spellIds.size());
    }

    public void nextSpell() {
        if (!spellIds.isEmpty()) {
            activeSpellIndex = (activeSpellIndex + 1) % spellIds.size();
        }
    }

    public void previousSpell() {
        if (!spellIds.isEmpty()) {
            activeSpellIndex = (activeSpellIndex - 1 + spellIds.size()) % spellIds.size();
        }
    }

    public String getId() {
        return id;
    }
}
