package me.pureplugins.buyspawners.util;

import de.dustplanet.util.SilkUtil;
import me.pureplugins.buyspawners.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class CreateSpawner {
    private static SilkUtil su;

    @SuppressWarnings("deprecation")
    public static ItemStack createSpawner(EntityType type, int amount) {
        if (su == null) su = SilkUtil.hookIntoSilkSpanwers();
        short entityID = type.getTypeId();
        return su.newSpawnerItem(entityID, su.getCustomSpawnerName(su.eid2MobID.get(entityID)), amount, false);
    }

    public static ItemStack fixStack(ItemStack is) {
        if (su == null) su = SilkUtil.hookIntoSilkSpanwers();
        String vanillaNbtId = su.nmsProvider.getVanillaNBTEntityID(is);
        if (vanillaNbtId != null) {
            if (su.mobID2Eid.containsKey(vanillaNbtId)) return is;
            EntityType type = entityTypeFromString(vanillaNbtId);
            if (type != null) {
                return createSpawner(type, is.getAmount());
            }
        } else if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
            String firstLine = is.getItemMeta().getLore().get(0);
            if (firstLine != null && !firstLine.isEmpty()) {
                firstLine = ChatColor.stripColor(firstLine).replace("Type: ", "").trim();
                EntityType type = entityTypeFromString(firstLine);
                if (type != null) {
                    return createSpawner(type, is.getAmount());
                }
            }
        }
        return is;
    }

    public static EntityType entityTypeFromString(String str) {
        try {
            return EntityType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            for (EntityType type : EntityType.values()) {
                if (type.name().replace("_", "").equals(str.toUpperCase())) {
                    return type;
                }
            }
        }
        return null;
    }
}
