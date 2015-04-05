package me.pureplugins.buyspawners.util;

import de.dustplanet.util.SilkUtil;
import me.pureplugins.buyspawners.Main;
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
            Main.getInstance().getLogger().warning("Corrupted NBT entityId detected: " + vanillaNbtId);
        }
        return is;
    }
}
