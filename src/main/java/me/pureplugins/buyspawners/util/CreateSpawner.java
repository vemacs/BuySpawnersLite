package me.pureplugins.buyspawners.util;

import java.lang.reflect.Field;

import de.dustplanet.util.SilkUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CreateSpawner {
    private static SilkUtil su;

    public static void getSilkUtil() {
        if (su == null) {
            try {
                Plugin pl = Bukkit.getPluginManager().getPlugin("SilkSpawners");
                Field suField = pl.getClass().getDeclaredField("su");
                suField.setAccessible(true);
                su = (SilkUtil) suField.get(pl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static ItemStack createSpawner(EntityType type, int amount) {
        getSilkUtil();
        short entityID = type.getTypeId();
        return su.newSpawnerItem(entityID, su.getCustomSpawnerName(su.eid2MobID.get(entityID)), amount, false);
    }
}
