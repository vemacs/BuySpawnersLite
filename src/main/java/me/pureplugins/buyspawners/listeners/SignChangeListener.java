package me.pureplugins.buyspawners.listeners;

import me.pureplugins.buyspawners.Main;
import me.pureplugins.buyspawners.handler.Spawner;
import me.pureplugins.buyspawners.handler.SpawnerManager;
import me.pureplugins.buyspawners.language.Language;
import me.pureplugins.buyspawners.permissions.Permissions;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    Main instance = Main.getInstance();
    private final String[] colors = new String[4];

    public SignChangeListener() {
        ConfigurationSection section = instance.getConfig().getConfigurationSection("signColors");

        colors[0] = ChatColor.translateAlternateColorCodes('&', section.getString("lineOne"));
        colors[1] = ChatColor.translateAlternateColorCodes('&', section.getString("lineTwo"));
        colors[2] = ChatColor.translateAlternateColorCodes('&', section.getString("lineThree"));
        colors[3] = ChatColor.translateAlternateColorCodes('&', section.getString("lineFour"));
    }

    @EventHandler
    private void onChange(SignChangeEvent evt) {
        Player target = evt.getPlayer();
        String[] lines = evt.getLines();

        if (target.hasPermission(Permissions.get("buyspawners.signs.create")) && target.isOp()) {
            if (lines[0].equalsIgnoreCase("[spawner]")) {
                try {
                    Integer.parseInt(lines[1]);
                } catch (NumberFormatException numberformatexception) {
                    instance.message.send(target, Language.INVALID_NUMBER);
                    return;
                }

                Spawner spawner = SpawnerManager.getSpawner(lines[2]);

                if (spawner != null) {
                    if (!containsDigit(lines[3])) {
                        if (!lines[3].equalsIgnoreCase("sync")) {
                            instance.message.send(target, Language.INVALID_NUMBER);
                            return;
                        }

                        lines[3] = "$" + spawner.getPrice();
                    }

                    evt.setLine(0, colors[0] + "[Spawner]");
                    evt.setLine(1, colors[1] + lines[1]);
                    evt.setLine(2, colors[2] + lines[2]);
                    evt.setLine(3, colors[3] + lines[3]);
                    instance.signLocations.add(evt.getBlock().getLocation().toString());
                    instance.message.send(target, Language.SIGN_CREATE.toString().replace("%type%", WordUtils.capitalizeFully(lines[2])));
                } else {
                    instance.message.send(target, Language.INVALID_MOB);
                }
            }
        }
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s == null) {
            return false;
        } else if (s.isEmpty()) {
            return false;
        } else {
            char[] achar;
            int i = (achar = s.toCharArray()).length;

            for (int j = 0; j < i; ++j) {
                char c = achar[j];

                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }

            return containsDigit;
        }
    }
}
