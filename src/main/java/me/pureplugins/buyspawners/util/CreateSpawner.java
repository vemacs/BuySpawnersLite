package me.pureplugins.buyspawners.util;

import de.dustplanet.util.SilkUtil;
import me.pureplugins.buyspawners.Main;
import org.bukkit.ChatColor;
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
            try {
                EntityType type = EntityType.valueOf(vanillaNbtId.toUpperCase());
                return createSpawner(type, is.getAmount());
            } catch (IllegalArgumentException | NullPointerException e) {
                for (EntityType type : EntityType.values()) {
                    if (type.name().replace("_", "").equals(vanillaNbtId.toUpperCase())) {
                        return createSpawner(type, is.getAmount());
                    }
                }
            }
            if (is.hasItemMeta()) {
                String name = is.getItemMeta().getDisplayName();
                if (name != null && !name.isEmpty() && !ChatColor.stripColor(name).equals(name)) {
                    Main.getInstance().getLogger().warning("Falling back to displayname for " + name);
                    name = ChatColor.stripColor(name.toLowerCase());
                    String[] nameParts = name.split(" ");
                    for (String part : nameParts) {
                        if (part.equalsIgnoreCase("spawner")) {
                            continue;
                        }
                        if (su.isKnown(part)) {
                            short entityID = su.name2Eid.get(part);
                            return su.newSpawnerItem(entityID, su.getCustomSpawnerName(su.eid2MobID.get(entityID)),
                                    is.getAmount(), false);
                        }
                    }
                }
            }
        }
        return is;
    }
}
