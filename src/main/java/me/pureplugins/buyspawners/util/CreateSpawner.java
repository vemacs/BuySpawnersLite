package me.pureplugins.buyspawners.util;

import de.dustplanet.util.SilkUtil;
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
        if (vanillaNbtId == null) return is;
        try {
            EntityType type = EntityType.valueOf(vanillaNbtId.toUpperCase());
            return createSpawner(type, is.getAmount());
        } catch (IllegalArgumentException | NullPointerException e) {
            return is;
        }
    }
}
