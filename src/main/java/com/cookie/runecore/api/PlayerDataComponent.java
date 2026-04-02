package com.cookie.runecore.api;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class PlayerDataComponent implements Component<EntityStore> {

    public static final String COMPONENT_ID = "runecore:player_data";
    public static ComponentType<EntityStore, PlayerDataComponent> TYPE;

    // Default values
    private float mana = 100.0f;
    private float maxMana = 100.0f;
    private String activeGrimoireId = null;
    private int activeSpellIndex = 0;

    public PlayerDataComponent() {
    }

    public static final BuilderCodec<PlayerDataComponent> CODEC = BuilderCodec
            .builder(PlayerDataComponent.class, PlayerDataComponent::new)
            .append(new KeyedCodec<Float>("Mana", Codec.FLOAT), (c, v) -> c.mana = v, c -> c.mana)
            .add()
            .append(new KeyedCodec<Float>("MaxMana", Codec.FLOAT), (c, v) -> c.maxMana = v, c -> c.maxMana)
            .add()
            .append(new KeyedCodec<String>("ActiveGrimoireId", Codec.STRING), (c, v) -> c.activeGrimoireId = v,
                    c -> c.activeGrimoireId)
            .add()
            .append(new KeyedCodec<Integer>("ActiveSpellIndex", Codec.INTEGER), (c, v) -> c.activeSpellIndex = v,
                    c -> c.activeSpellIndex)
            .add()
            .build();

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = Math.max(0, Math.min(maxMana, mana));
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = Math.max(1, maxMana);
    }

    public String getActiveGrimoireId() {
        return activeGrimoireId;
    }

    public void setActiveGrimoireId(String id) {
        this.activeGrimoireId = id;
    }

    public int getActiveSpellIndex() {
        return activeSpellIndex;
    }

    public void setActiveSpellIndex(int index) {
        this.activeSpellIndex = index;
    }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        PlayerDataComponent clone = new PlayerDataComponent();
        clone.mana = this.mana;
        clone.maxMana = this.maxMana;
        return clone;
    }
}
