package me.pureplugins.buyspawners.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateSpawner {
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
        tag.setString("EntityId", entityType);
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
