package me.pureplugins.buyspawners;

import me.pureplugins.buyspawners.commands.CommandGiveSpawner;
import me.pureplugins.buyspawners.configuration.LocationConfig;
import me.pureplugins.buyspawners.handler.SpawnerManager;
import me.pureplugins.buyspawners.language.Language;
import me.pureplugins.buyspawners.listeners.ItemStackFixer;
import me.pureplugins.buyspawners.listeners.SignChangeListener;
import me.pureplugins.buyspawners.listeners.SignClickListener;
import me.pureplugins.buyspawners.util.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

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
        saveDefaultConfig();
        Language.load.loadLang();
        message = new Message();
        locationConfig = new LocationConfig();
        locationConfig.reloadConfig();
        SpawnerManager.loadSpawners();
        signLocations = locationConfig.getConfig().getStringList("Locations");
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            setEnabled(false);
        } else {
            registerCommands();
            registerListeners();
        }
    }

    private void registerCommands() {
        getCommand("givespawner").setExecutor(new CommandGiveSpawner());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new SignChangeListener(), this);
        getServer().getPluginManager().registerEvents(new SignClickListener(), this);
        getServer().getPluginManager().registerEvents(new ItemStackFixer(), this);
    }

    @Override
    public void onDisable() {
        locationConfig.getConfig().set("Locations", signLocations);
        locationConfig.saveConfig();
        saveDefaultConfig();
        instance = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp == null) {
                return false;
            } else {
                econ = (Economy) rsp.getProvider();
                return econ != null;
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
