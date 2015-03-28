package me.pureplugins.buyspawners.util;

import me.pureplugins.buyspawners.Main;
import me.pureplugins.buyspawners.language.Language;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Message {
    private final boolean useActionBar = Main.getInstance().getConfig().getBoolean("use action bar");

    public void send(Player target, Object message) {
        if (this.useActionBar) {
            this.action(target, message);
        } else {
            target.sendMessage(Language.PREFIX.toString() + message);
        }
    }

    private void action(Player target, Object message) {
        CraftPlayer user = (CraftPlayer) target;
        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, (byte) 2);

        user.getHandle().playerConnection.sendPacket(ppoc);
    }
}
