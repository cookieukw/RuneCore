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
    private boolean frozen = false;
    private float frozenRotX = 0.0f;
    private float frozenRotY = 0.0f;
    private float frozenRotZ = 0.0f;
    private boolean nausea = false;
    private float nauseaTime = 0.0f;
    private boolean blinded = false;
    private boolean glowing = false;
    private boolean nightVision = false;

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
            .append(new KeyedCodec<Boolean>("Frozen", Codec.BOOLEAN), (c, v) -> c.frozen = v, c -> c.frozen)
            .add()
            .append(new KeyedCodec<Float>("FrozenRotX", Codec.FLOAT), (c, v) -> c.frozenRotX = v, c -> c.frozenRotX)
            .add()
            .append(new KeyedCodec<Float>("FrozenRotY", Codec.FLOAT), (c, v) -> c.frozenRotY = v, c -> c.frozenRotY)
            .add()
            .append(new KeyedCodec<Float>("FrozenRotZ", Codec.FLOAT), (c, v) -> c.frozenRotZ = v, c -> c.frozenRotZ)
            .add()
            .append(new KeyedCodec<Boolean>("Nausea", Codec.BOOLEAN), (c, v) -> c.nausea = v, c -> c.nausea)
            .add()
            .append(new KeyedCodec<Float>("NauseaTime", Codec.FLOAT), (c, v) -> c.nauseaTime = v, c -> c.nauseaTime)
            .add()
            .append(new KeyedCodec<Boolean>("Blinded", Codec.BOOLEAN), (c, v) -> c.blinded = v, c -> c.blinded)
            .add()
            .append(new KeyedCodec<Boolean>("Glowing", Codec.BOOLEAN), (c, v) -> c.glowing = v, c -> c.glowing)
            .add()
            .append(new KeyedCodec<Boolean>("NightVision", Codec.BOOLEAN), (c, v) -> c.nightVision = v, c -> c.nightVision)
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

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public float getFrozenRotX() { return frozenRotX; }
    public void setFrozenRotX(float frozenRotX) { this.frozenRotX = frozenRotX; }

    public float getFrozenRotY() { return frozenRotY; }
    public void setFrozenRotY(float frozenRotY) { this.frozenRotY = frozenRotY; }

    public float getFrozenRotZ() { return frozenRotZ; }
    public void setFrozenRotZ(float frozenRotZ) { this.frozenRotZ = frozenRotZ; }

    public boolean isNausea() { return nausea; }
    public void setNausea(boolean nausea) { this.nausea = nausea; }

    public float getNauseaTime() { return nauseaTime; }
    public void setNauseaTime(float nauseaTime) { this.nauseaTime = nauseaTime; }

    public boolean isBlinded() { return blinded; }
    public void setBlinded(boolean blinded) { this.blinded = blinded; }

    public boolean isGlowing() { return glowing; }
    public void setGlowing(boolean glowing) { this.glowing = glowing; }

    public boolean isNightVision() { return nightVision; }
    public void setNightVision(boolean nightVision) { this.nightVision = nightVision; }

    @Nonnull
    @Override
    public Component<EntityStore> clone() {
        PlayerDataComponent clone = new PlayerDataComponent();
        clone.mana = this.mana;
        clone.maxMana = this.maxMana;
        clone.frozen = this.frozen;
        clone.frozenRotX = this.frozenRotX;
        clone.frozenRotY = this.frozenRotY;
        clone.frozenRotZ = this.frozenRotZ;
        clone.nausea = this.nausea;
        clone.nauseaTime = this.nauseaTime;
        clone.blinded = this.blinded;
        clone.glowing = this.glowing;
        clone.nightVision = this.nightVision;
        return clone;
    }
}
