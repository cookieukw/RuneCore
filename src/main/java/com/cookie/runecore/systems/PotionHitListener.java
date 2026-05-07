package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.entity.EntityDamageEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;

public class PotionHitListener {

    public PotionHitListener(EventRegistry eventRegistry) {
        // Listening to the hypothetical EntityDamageEvent (or equivalent in future API)
        // Since Hytale native projectiles do area damage, this triggers for each entity hit.
        eventRegistry.registerGlobal(EntityDamageEvent.class, this::onEntityDamage);
    }

    private void onEntityDamage(EntityDamageEvent event) {
        // If the damage cause was a projectile
        if (event.getDamageCause() != null && event.getDamageCause().contains("Bomb_Potion_")) {
            
            // Extract the effect name from the projectile ID
            // Example: "Bomb_Potion_Speed" -> "speed"
            String effectName = event.getDamageCause().replace("Bomb_Potion_", "").toLowerCase();
            
            Ref<EntityStore> targetRef = event.getTargetRef();
            Ref<EntityStore> sourceRef = event.getSourceRef(); // The player who threw it

            if (targetRef != null) {
                World world = targetRef.getStore().getExternalData() != null ? 
                              targetRef.getStore().getExternalData().getWorld() : null;

                // Create context and execute
                CastContext ctx = new CastContext(null, targetRef, world, 1.0);
                
                // We use our existing engine!
                if (RuneCore.get().getEffect(effectName) != null) {
                    RuneCore.get().getEffect(effectName).execute(ctx);
                }
            }
        }
    }
}
