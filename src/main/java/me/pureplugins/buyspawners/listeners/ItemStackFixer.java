package me.pureplugins.buyspawners.listeners;

import me.pureplugins.buyspawners.util.CreateSpawner;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class ItemStackFixer implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (is != null && is.getType() == Material.MOB_SPAWNER) {
            event.getPlayer().setItemInHand(CreateSpawner.fixStack(is));
        }
    }
}
