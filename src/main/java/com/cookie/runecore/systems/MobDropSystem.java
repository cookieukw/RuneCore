package com.cookie.runecore.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MobDropSystem extends DeathSystems.OnDeathSystem {

    private final Random random = new Random();

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent death,
            @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        Damage deathInfo = death.getDeathInfo();
        if (deathInfo == null) return;

        Ref<EntityStore> attackerRef = resolveAttackerRef(deathInfo);
        if (attackerRef == null) return;

        if (store.getComponent(attackerRef, Player.getComponentType()) == null) return;

        ModelComponent modelComp = store.getComponent(ref, ModelComponent.getComponentType());
        if (modelComp == null || modelComp.getModel() == null) return;

        String entityType = modelComp.getModel().getModelAssetId().toLowerCase();

        if (entityType.contains("projectile") ||
                entityType.contains("arrow") ||
                entityType.contains("bolt") ||
                entityType.contains("vfx") ||
                entityType.contains("particle") ||
                entityType.contains("item")) {
            return;
        }

        String essenceId = getEssenceDrop(entityType);
        if (essenceId == null) return;

        if (random.nextFloat() >= 0.25f) return;

        ItemStack item = new ItemStack(essenceId, 1);

        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform == null) return;

        commandBuffer.invoke(ref, new DropItemEvent.Drop(item, 0f));
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
                DeathComponent.getComponentType(),
                Query.not(Player.getComponentType()));
    }

    @Nullable
    private Ref<EntityStore> resolveAttackerRef(Damage damage) {
        Damage.Source source = damage.getSource();
        if (source == null) return null;

        if (source instanceof Damage.EntitySource s) {
            return s.getRef();
        }

        if (source instanceof Damage.ProjectileSource s) {
            return s.getRef();
        }

        return null;
    }

    @Nullable
    private String getEssenceDrop(String entityType) {
        if (entityType.contains("emberwulf") || entityType.contains("dragon_fire") ||
                entityType.contains("magma") || entityType.contains("flame")) {
            return "Essence_Fire";
        }
        if (entityType.contains("golem_crystal_earth") || entityType.contains("trork") ||
                entityType.contains("mouflon") || entityType.contains("bison") ||
                entityType.contains("tortoise") || entityType.contains("molerat")) {
            return "Essence_Earth";
        }
        if (entityType.contains("archaeopteryx") || entityType.contains("bluebird") ||
                entityType.contains("crow") || entityType.contains("duck") ||
                entityType.contains("feran_windwalker") || entityType.contains("hawk") ||
                entityType.contains("owl") || entityType.contains("parrot") ||
                entityType.contains("pigeon") || entityType.contains("sparrow") ||
                entityType.contains("vulture")) {
            return "Essence_Wind";
        }
        if (entityType.contains("bluegill") || entityType.contains("catfish") ||
                entityType.contains("clownfish") || entityType.contains("crab") ||
                entityType.contains("eel") || entityType.contains("frog") ||
                entityType.contains("jellyfish") || entityType.contains("minnow") ||
                entityType.contains("pike") || entityType.contains("piranha") ||
                entityType.contains("salmon") || entityType.contains("shark") ||
                entityType.contains("tang") || entityType.contains("trout") ||
                entityType.contains("whale")) {
            return "Essence_Water";
        }
        if (entityType.contains("bear_polar") || entityType.contains("dragon_frost") ||
                entityType.contains("golem_crystal_frost") || entityType.contains("leopard_snow") ||
                entityType.contains("penguin") || entityType.contains("skeleton_frost") ||
                entityType.contains("snail_frost") || entityType.contains("worlming_frost") ||
                entityType.contains("yeti")) {
            return "Essence_Ice";
        }
        if (entityType.contains("golem_crystal_thunder") || entityType.contains("spirit_thunder") ||
                entityType.contains("spark_living")) {
            return "Essence_Lightning";
        }
        if (entityType.contains("kweebec_sapling_christmas") || entityType.contains("spirit_root")) {
            return "Essence_Light";
        }
        if (entityType.contains("shadow_knight") || entityType.contains("wraith") ||
                entityType.contains("skrill")) {
            return "Essence_Shadow";
        }
        if (entityType.contains("kweebec") || entityType.contains("cow") ||
                entityType.contains("pig") || entityType.contains("sheep") ||
                entityType.contains("chicken") || entityType.contains("deer") ||
                entityType.contains("mosshorn")) {
            return "Essence_Life";
        }
        if (entityType.contains("skeleton") || entityType.contains("zombie") ||
                entityType.contains("ghoul")) {
            return "Essence_Death";
        }
        if (entityType.contains("slothian") || entityType.contains("outlander_sorcerer")) {
            return "Essence_Mind";
        }
        if (entityType.contains("bat") || entityType.contains("mosquito")) {
            return "Essence_Blood";
        }
        if (entityType.contains("outlander_berserker") || entityType.contains("trork_chieftain")) {
            return "Essence_Chaos";
        }
        if (entityType.contains("spirit_ember")) {
            return "Essence_Aether";
        }
        if (entityType.contains("void")) {
            return "Essence_Void";
        }
        if (entityType.contains("golem_firesteel") || entityType.contains("tank") ||
                entityType.contains("turret")) {
            return "Essence_Metal";
        }
        if (entityType.contains("golem_crystal") || entityType.contains("scarak")) {
            return "Essence_Crystal";
        }
        if (entityType.contains("snake") || entityType.contains("spider") ||
                entityType.contains("scorpion")) {
            return "Essence_Poison";
        }
        return null;
    }

}