package me.pureplugins.buyspawners;

import java.io.File;
import java.util.List;

import me.pureplugins.buyspawners.commands.CommandGiveSpawner;
import me.pureplugins.buyspawners.configuration.LocationConfig;
import me.pureplugins.buyspawners.handler.SpawnerManager;
import me.pureplugins.buyspawners.language.Language;
import me.pureplugins.buyspawners.listeners.SignChangeListener;
import me.pureplugins.buyspawners.listeners.SignClickListener;
import me.pureplugins.buyspawners.util.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    public static YamlConfiguration LANG;
    public static File LANG_FILE;
    public Message message;
    public LocationConfig locationConfig;
    public Economy econ;
    public List<String> signLocations;

    @Override
    public void onEnable() {
        Main.instance = this;
        this.saveDefaultConfig();
        Language.load.loadLang();
        this.message = new Message();
        this.locationConfig = new LocationConfig();
        this.locationConfig.reloadConfig();
        SpawnerManager.loadSpawners();
        this.signLocations = this.locationConfig.getConfig().getStringList("Locations");
        if (!this.setupEconomy()) {
            this.getLogger().info(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[] { this.getDescription().getName()}));
            this.setEnabled(false);
        } else {
            this.registerCommands();
            this.registerListeners();
        }
    }

    private void registerCommands() {
        this.getCommand("givespawner").setExecutor(new CommandGiveSpawner());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new SignChangeListener(), this);
        this.getServer().getPluginManager().registerEvents(new SignClickListener(), this);
    }

    @Override
    public void onDisable() {
        this.locationConfig.getConfig().set("Locations", this.signLocations);
        this.locationConfig.saveConfig();
        this.saveDefaultConfig();
        Main.instance = null;
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider rsp = this.getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp == null) {
                return false;
            } else {
                this.econ = (Economy) rsp.getProvider();
                return this.econ != null;
            }
        }
    }

    public YamlConfiguration getLang() {
        return Main.LANG;
    }

    public File getLangFile() {
        return Main.LANG_FILE;
    }

    public static Main getInstance() {
        return Main.instance;
    }
}
