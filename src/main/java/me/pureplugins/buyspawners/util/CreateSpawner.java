package me.pureplugins.buyspawners.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateSpawner {
    private static Map<String, String> bukkitToNMS = ImmutableMap.<String, String>builder()
            .put("Horse", "EntityHorse")
            .put("Mushroom_Cow", "MushroomCow")
            .put("Magma_Cube", "LavaSlime")
            .put("Snowman", "SnowMan")
            .put("SilverFish", "Silverfish")
            .put("Cave_Spider", "CaveSpider")
            .put("Iron_Golem", "VillagerGolem")
            .put("Ender_Dragon", "EnderDragon")
            .put("Pig_Zombie", "PigZombie")
            .build();

    public static ItemStack get(String entityType, int amount) {
        net.minecraft.server.v1_8_R1.ItemStack item = CraftItemStack.asNMSCopy(new ItemStack(Material.MOB_SPAWNER));

        if (!item.hasTag()) {
            item.setTag(new NBTTagCompound());
        }

        NBTTagCompound tag = item.getTag();

        if (!tag.hasKey("BlockEntityTag")) {
            tag.set("BlockEntityTag", new NBTTagCompound());
        }

        tag = tag.getCompound("BlockEntityTag");

        String entityIdStr = entityType;
        if (bukkitToNMS.containsKey(entityIdStr)) {
            entityIdStr = bukkitToNMS.get(entityIdStr);
        }

        tag.setString("EntityId", entityIdStr);
        ItemStack spawner = CraftItemStack.asBukkitCopy(item);
        ItemMeta meta = spawner.getItemMeta();

        spawner.setAmount(amount);
        meta.setDisplayName(ChatColor.RED + WordUtils.capitalizeFully(entityType.replace("_", " ")) + ChatColor.WHITE + " Spawner");
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.YELLOW + "Type: " + ChatColor.RED + WordUtils.capitalizeFully(entityType));
        meta.setLore(lore);
        spawner.setItemMeta(meta);
        return spawner;
    }
}
