package com.cookie.runecore.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class MobDropSystem extends DeathSystems.OnDeathSystem {

    private final Random random = new Random();

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent death, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        try {
            // Check if attacker was a player
            Damage deathInfo = death.getDeathInfo();
            if (deathInfo == null) return;

            Ref<EntityStore> attackerRef = resolveAttackerRef(deathInfo);
            if (attackerRef == null) return;
            
            // Allow drop if attacker is a player
            if (store.getComponent(attackerRef, Player.getComponentType()) == null) {
                 return;
            }
            
            // Identify entity
            String entityType = ref.toString().toLowerCase(); 
            // Debug log to help identify correct strings
            // System.out.println("[RuneCore-Debug] Entity died: " + entityType);

            String essenceId = getEssenceDrop(entityType);
            
            if (essenceId != null) {
                // 25% chance to drop (adjustable)
                if (random.nextFloat() < 0.25f) {
                    ItemStack essenceItem = new ItemStack(essenceId, 1);
                    DropItemEvent.Drop dropEvent = new DropItemEvent.Drop(essenceItem, 0.5f);
                    commandBuffer.invoke(ref, dropEvent);
                    
                    // Feedback to attacker (optional)
                    // PlayerRef attackerPlayerRef = (PlayerRef) store.getComponent(attackerRef, PlayerRef.getComponentType());
                    // if (attackerPlayerRef != null) {
                    //    attackerPlayerRef.sendMessage(Message.raw("You found an essence!"));
                    // }
                }
            }

        } catch (Exception e) {
            System.err.println("Error in MobDropSystem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Nullable
    private String getEssenceDrop(String entityType) {
        // Fire
        if (entityType.contains("emberwulf") || entityType.contains("dragon_fire") || 
            entityType.contains("magma") || entityType.contains("flame")) {
            return "essence_fire";
        }
        // Earth
        if (entityType.contains("golem_crystal_earth") || entityType.contains("trork") || 
            entityType.contains("mouflon") || entityType.contains("bison") || 
            entityType.contains("tortoise") || entityType.contains("molerat")) {
            return "essence_earth";
        }
        // Wind
        if (entityType.contains("archaeopteryx") || entityType.contains("bluebird") || 
            entityType.contains("crow") || entityType.contains("duck") || 
            entityType.contains("feran_windwalker") || entityType.contains("hawk") || 
            entityType.contains("owl") || entityType.contains("parrot") || 
            entityType.contains("pigeon") || entityType.contains("sparrow") || 
            entityType.contains("vulture")) {
            return "essence_wind";
        }
        // Water
        if (entityType.contains("bluegill") || entityType.contains("catfish") || 
            entityType.contains("clownfish") || entityType.contains("crab") || 
            entityType.contains("eel") || entityType.contains("frog") || 
            entityType.contains("jellyfish") || entityType.contains("minnow") || 
            entityType.contains("pike") || entityType.contains("piranha") || 
            entityType.contains("salmon") || entityType.contains("shark") || 
            entityType.contains("tang") || entityType.contains("trout") || 
            entityType.contains("whale")) {
            return "essence_water";
        }
        // Ice
        if (entityType.contains("bear_polar") || entityType.contains("dragon_frost") || 
            entityType.contains("golem_crystal_frost") || entityType.contains("leopard_snow") || 
            entityType.contains("penguin") || entityType.contains("skeleton_frost") || 
            entityType.contains("snail_frost") || entityType.contains("worlming_frost") || 
            entityType.contains("yeti")) {
            return "essence_ice";
        }
        // Lightning
        if (entityType.contains("golem_crystal_thunder") || entityType.contains("spirit_thunder") || 
            entityType.contains("spark_living")) {
            return "essence_lightning";
        }
        // Light
        if (entityType.contains("kweebec_sapling_christmas") || entityType.contains("spirit_root")) {
            return "essence_light";
        }
        // Shadow
        if (entityType.contains("shadow_knight") || entityType.contains("wraith") || 
            entityType.contains("skrill")) {
            return "essence_shadow";
        }
        // Life
        if (entityType.contains("kweebec") || entityType.contains("cow") || 
            entityType.contains("pig") || entityType.contains("sheep") || 
            entityType.contains("chicken") || entityType.contains("deer") || 
            entityType.contains("mosshorn")) {
            return "essence_life";
        }
        // Death
        if (entityType.contains("skeleton") || entityType.contains("zombie") || 
            entityType.contains("ghoul")) {
            // Priority over others if needed, but here simple checks
            return "essence_death";
        }
        // Mind
        if (entityType.contains("slothian") || entityType.contains("outlander_sorcerer")) {
            return "essence_mind";
        }
        // Blood
        if (entityType.contains("bat") || entityType.contains("mosquito")) {
            return "essence_blood";
        }
        // Chaos
        if (entityType.contains("outlander_berserker") || entityType.contains("trork_chieftain")) {
            return "essence_chaos";
        }
        // Aether
        if (entityType.contains("spirit_ember")) {
            return "essence_aether";
        }
        // Void
        if (entityType.contains("void")) {
            return "essence_void";
        }
        // Metal
        if (entityType.contains("golem_firesteel") || entityType.contains("tank") || 
            entityType.contains("turret")) {
            return "essence_metal";
        }
        // Crystal
        if (entityType.contains("golem_crystal") || entityType.contains("scarak")) {
            return "essence_crystal";
        }
        // Poison
        if (entityType.contains("snake") || entityType.contains("spider") || 
            entityType.contains("scorpion")) {
            return "essence_poison";
        }
        
        return null; // No essence for this mob
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }
    
    // Helper to resolve attacker regardless of projectile or direct
    @Nullable
    private Ref<EntityStore> resolveAttackerRef(Damage damage) {
        Damage.Source source = damage.getSource();
        if (source == null) {
            return null;
        }
        // Direct entity attack
        if (source instanceof Damage.EntitySource) {
             return ((Damage.EntitySource) source).getRef();
        }
        // Projectile attack (e.g. arrow, magic)
        if (source instanceof Damage.ProjectileSource) {
            Damage.ProjectileSource ps = (Damage.ProjectileSource) source;
            return ps.getRef(); // This is usually the owner of the projectile (Shooter)
        }
        return null;
    }
}
