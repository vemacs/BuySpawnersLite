package me.pureplugins.buyspawners.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import me.pureplugins.buyspawners.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class LocationConfig {

    Main instance = Main.getInstance();
    private FileConfiguration fileConfig = null;
    private File file = null;

    public void reloadConfig() {
        String configName = "locations.yml";
        if (file == null) {
            file = new File(instance.getDataFolder(), configName);
        }

        fileConfig = YamlConfiguration.loadConfiguration(file);
        InputStream input = instance.getResource(configName);

        if (input != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            fileConfig.setDefaults(config);
        }

    }

    public FileConfiguration getConfig() {
        if (fileConfig == null) {
            reloadConfig();
        }

        return fileConfig;
    }

    public void saveConfig() {
        if (fileConfig != null && file != null) {
            try {
                getConfig().save(file);
            } catch (IOException ioexception) {
                instance.getLogger().log(Level.SEVERE, "Could not create config " + file, ioexception);
            }

        }
    }
}
