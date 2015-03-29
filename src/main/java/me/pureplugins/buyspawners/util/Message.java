package me.pureplugins.buyspawners.util;

import me.pureplugins.buyspawners.language.Language;
import org.bukkit.entity.Player;

public class Message {
    public void send(Player target, Object message) {
        target.sendMessage(Language.PREFIX.toString() + message);
    }
}
