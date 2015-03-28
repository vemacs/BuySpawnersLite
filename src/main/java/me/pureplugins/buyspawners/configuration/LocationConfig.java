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
        if (this.file == null) {
            this.file = new File(this.instance.getDataFolder(), configName);
        }

        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
        InputStream input = this.instance.getResource(configName);

        if (input != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);

            this.fileConfig.setDefaults(config);
        }

    }

    public FileConfiguration getConfig() {
        if (this.fileConfig == null) {
            this.reloadConfig();
        }

        return this.fileConfig;
    }

    public void saveConfig() {
        if (this.fileConfig != null && this.file != null) {
            try {
                this.getConfig().save(this.file);
            } catch (IOException ioexception) {
                this.instance.getLogger().log(Level.SEVERE, "Could not create config " + this.file, ioexception);
            }

        }
    }
}
