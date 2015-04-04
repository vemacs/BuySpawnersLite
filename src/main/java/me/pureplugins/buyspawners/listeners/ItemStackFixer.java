package me.pureplugins.buyspawners.listeners;

import me.pureplugins.buyspawners.util.CreateSpawner;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class ItemStackFixer implements Listener {
    private Field itemInHandField;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (is != null && is.getType() == Material.MOB_SPAWNER) {
            ItemStack fixed = CreateSpawner.fixStack(is);
            if (fixed != is) {
                try {
                    if (itemInHandField == null) {
                        itemInHandField = event.getClass().getDeclaredField("itemInHand");
                        itemInHandField.setAccessible(true);
                    }
                    itemInHandField.set(event, fixed);
                    event.getPlayer().setItemInHand(fixed);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
