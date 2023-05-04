package com.floweytf.SpigotTest.listeners;

import com.floweytf.SpigotTest.commands.Test;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityDismountEventListener implements Listener {
    @EventHandler
    public void onEntityDismountEvent(EntityDismountEvent event) {
        if (Test.hashMap.containsKey(event.getEntity().getUniqueId())) event.setCancelled(true);
    }
}
