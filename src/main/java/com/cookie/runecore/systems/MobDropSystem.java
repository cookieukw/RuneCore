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

        System.out.println("[RuneCore] === Mob died ===");

        Damage deathInfo = death.getDeathInfo();
        if (deathInfo == null) {
            System.out.println("[RuneCore] deathInfo null");
            return;
        }

        Ref<EntityStore> attackerRef = resolveAttackerRef(deathInfo);
        if (attackerRef == null) {
            System.out.println("[RuneCore] attackerRef null");
            return;
        }

        System.out.println("[RuneCore] attackerRef válido: " + attackerRef);

        if (store.getComponent(attackerRef, Player.getComponentType()) == null) {
            System.out.println("[RuneCore] attacker is not player");
            return;
        }

        System.out.println("[RuneCore] attacker is player");

        ModelComponent modelComp = store.getComponent(ref, ModelComponent.getComponentType());
        if (modelComp == null) {
            System.out.println("[RuneCore] ModelComponent null");
            return;
        }

        if (modelComp.getModel() == null) {
            System.out.println("[RuneCore] Model null");
            return;
        }

        String entityType = modelComp.getModel().getModelAssetId().toLowerCase();
        System.out.println("[RuneCore] entityType: " + entityType);

        if (entityType.contains("projectile") ||
                entityType.contains("arrow") ||
                entityType.contains("bolt") ||
                entityType.contains("vfx") ||
                entityType.contains("particle") ||
                entityType.contains("item")) {
            System.out.println("[RuneCore] ignored (trash/invalid entity)");
            return;
        }

        String essenceId = getEssenceDrop(entityType);
        if (essenceId == null) {
            System.out.println("[RuneCore] no drop configured");
            return;
        }

        System.out.println("[RuneCore] possible drop: " + essenceId);

        float roll = random.nextFloat();
        System.out.println("[RuneCore] roll: " + roll);

        if (roll >= 0.25f) {
            System.out.println("[RuneCore] failed chance");
            return;
        }

        System.out.println("[RuneCore] passed chance");

        ItemStack item = new ItemStack(essenceId, 1);

        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform == null) {
            System.out.println("[RuneCore] TransformComponent null");
            return;
        }

        System.out.println("[RuneCore] spawning drop");

        commandBuffer.invoke(
                ref,
                new DropItemEvent.Drop(item, 0f));

        System.out.println("[RuneCore] Executed DROP: " + essenceId);
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        System.out.println("[RuneCore] Query initialized");
        return Query.and(
                DeathComponent.getComponentType(),
                Query.not(Player.getComponentType()));
    }

    @Nullable
    private Ref<EntityStore> resolveAttackerRef(Damage damage) {
        System.out.println("[RuneCore][DEBUG] resolving attacker");

        Damage.Source source = damage.getSource();
        if (source == null) {
            System.out.println("[RuneCore][WARN] damage source is null");
            return null;
        }

        System.out.println("[RuneCore][DEBUG] source type: " + source.getClass().getName());

        if (source instanceof Damage.EntitySource s) {
            System.out.println("[RuneCore][INFO] direct entity damage");
            Ref<EntityStore> ref = s.getRef();
            System.out.println("[RuneCore][DEBUG] attacker ref: " + ref);
            return ref;
        }

        if (source instanceof Damage.ProjectileSource s) {
            System.out.println("[RuneCore][INFO] projectile damage");
            Ref<EntityStore> ref = s.getRef();
            System.out.println("[RuneCore][DEBUG] projectile ref: " + ref);
            return ref;
        }

        System.out.println("[RuneCore][WARN] unknown damage source: " + source.getClass().getName());
        return null;
    }

    @Nullable
    private String getEssenceDrop(String entityType) {
        System.out.println("[RuneCore] checking drop for: " + entityType);

        if (entityType.contains("emberwulf") || entityType.contains("dragon_fire") ||
                entityType.contains("magma") || entityType.contains("flame")) {
            System.out.println("[RuneCore] matched FIRE");
            return "Essence_Fire";
        }

        if (entityType.contains("golem_crystal_earth") || entityType.contains("trork") ||
                entityType.contains("mouflon") || entityType.contains("bison") ||
                entityType.contains("tortoise") || entityType.contains("molerat")) {
            System.out.println("[RuneCore] matched EARTH");
            return "Essence_Earth";
        }

        if (entityType.contains("archaeopteryx") || entityType.contains("bluebird") ||
                entityType.contains("crow") || entityType.contains("duck") ||
                entityType.contains("feran_windwalker") || entityType.contains("hawk") ||
                entityType.contains("owl") || entityType.contains("parrot") ||
                entityType.contains("pigeon") || entityType.contains("sparrow") ||
                entityType.contains("vulture")) {
            System.out.println("[RuneCore] matched WIND");
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
            System.out.println("[RuneCore] matched WATER");
            return "Essence_Water";
        }

        if (entityType.contains("bear_polar") || entityType.contains("dragon_frost") ||
                entityType.contains("golem_crystal_frost") || entityType.contains("leopard_snow") ||
                entityType.contains("penguin") || entityType.contains("skeleton_frost") ||
                entityType.contains("snail_frost") || entityType.contains("worlming_frost") ||
                entityType.contains("yeti")) {
            System.out.println("[RuneCore] matched ICE");
            return "Essence_Ice";
        }

        if (entityType.contains("golem_crystal_thunder") || entityType.contains("spirit_thunder") ||
                entityType.contains("spark_living")) {
            System.out.println("[RuneCore] matched LIGHTNING");
            return "Essence_Lightning";
        }

        if (entityType.contains("kweebec_sapling_christmas") || entityType.contains("spirit_root")) {
            System.out.println("[RuneCore] matched LIGHT");
            return "Essence_Light";
        }

        if (entityType.contains("shadow_knight") || entityType.contains("wraith") ||
                entityType.contains("skrill")) {
            System.out.println("[RuneCore] matched SHADOW");
            return "Essence_Shadow";
        }

        if (entityType.contains("kweebec") || entityType.contains("cow") ||
                entityType.contains("pig") || entityType.contains("sheep") ||
                entityType.contains("chicken") || entityType.contains("deer") ||
                entityType.contains("mosshorn")) {
            System.out.println("[RuneCore] matched LIFE");
            return "Essence_Life";
        }

        if (entityType.contains("skeleton") || entityType.contains("zombie") ||
                entityType.contains("ghoul")) {
            System.out.println("[RuneCore] matched DEATH");
            return "Essence_Death";
        }

        if (entityType.contains("slothian") || entityType.contains("outlander_sorcerer")) {
            System.out.println("[RuneCore] matched MIND");
            return "Essence_Mind";
        }

        if (entityType.contains("bat") || entityType.contains("mosquito")) {
            System.out.println("[RuneCore] matched BLOOD");
            return "Essence_Blood";
        }

        if (entityType.contains("outlander_berserker") || entityType.contains("trork_chieftain")) {
            System.out.println("[RuneCore] matched CHAOS");
            return "Essence_Chaos";
        }

        if (entityType.contains("spirit_ember")) {
            System.out.println("[RuneCore] matched AETHER");
            return "Essence_Aether";
        }

        if (entityType.contains("void")) {
            System.out.println("[RuneCore] matched VOID");
            return "Essence_Void";
        }

        if (entityType.contains("golem_firesteel") || entityType.contains("tank") ||
                entityType.contains("turret")) {
            System.out.println("[RuneCore] matched METAL");
            return "Essence_Metal";
        }

        if (entityType.contains("golem_crystal") || entityType.contains("scarak")) {
            System.out.println("[RuneCore] matched CRYSTAL");
            return "Essence_Crystal";
        }

        if (entityType.contains("snake") || entityType.contains("spider") ||
                entityType.contains("scorpion")) {
            System.out.println("[RuneCore] matched POISON");
            return "Essence_Poison";
        }

        System.out.println("[RuneCore] no match found");
        return null;
    }

}