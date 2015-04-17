package me.pureplugins.buyspawners.listeners;

import com.google.common.base.Joiner;
import de.dustplanet.util.SilkUtil;
import me.pureplugins.buyspawners.Main;
import me.pureplugins.buyspawners.util.CreateSpawner;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStackFixer implements Listener {
    private Field itemInHandField;
    private static SilkUtil su;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (is != null && is.getType() == Material.MOB_SPAWNER) {
            Main.getInstance().getLogger().info("Spawner placed by "
                    + event.getPlayer().getName() + generateDebug(is));
            ItemStack fixed = CreateSpawner.fixStack(is);
            if (fixed != is) {
                Main.getInstance().getLogger().info("Fixed spawner for "
                        + event.getPlayer().getName() + generateDebug(fixed));
                try {
                    if (itemInHandField == null) {
                        itemInHandField = event.getClass().getDeclaredField("itemInHand");
                        itemInHandField.setAccessible(true);
                    }
                    itemInHandField.set(event, fixed);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String generateDebug(ItemStack is) {
        if (su == null) su = SilkUtil.hookIntoSilkSpanwers();
        String displayName;
        List<String> lore;
        if (is.hasItemMeta()) {
            ItemMeta meta = is.getItemMeta();
            displayName = meta.hasDisplayName() ? meta.getDisplayName() : "none";
            lore = meta.hasLore() ? meta.getLore() : Collections.singletonList("none");
        } else {
            displayName = "null";
            lore = Collections.singletonList("null");
        }
        displayName = displayName.replace("\u00A7", "&");
        lore = new ArrayList<>(lore);
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("\u00A7", "&"));
        }
        String vanillaNbtId = su.nmsProvider.getVanillaNBTEntityID(is);
        if (vanillaNbtId == null) vanillaNbtId = "null";
        return ", with displayName " + displayName
                + ", vanillaNbtId " + vanillaNbtId + ", lore " + Joiner.on(":").join(lore);
    }
}
