package me.pureplugins.buyspawners.handler;

import me.pureplugins.buyspawners.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

public class SpawnerManager {

    private static final Map<Set<String>, Spawner> creatureData = new HashMap<>();

    public static void loadSpawners() {
        File file = new File(Main.getInstance().getDataFolder(), "mobdata.yml");
        YamlConfiguration configuration;

        if (!file.exists()) {
            try {
                InputStream type = Main.getInstance().getResource("mobdata.yml");

                configuration = YamlConfiguration.loadConfiguration(type);
                configuration.save(file);
            } catch (IOException ioexception) {
                Main.getInstance().getLogger().log(Level.WARNING, "Could not generate the config!");
                return;
            }
        } else {
            configuration = YamlConfiguration.loadConfiguration(file);
        }

        for (String type1 : configuration.getKeys(false)) {
            try {
                EntityType e = EntityType.valueOf(type1.toUpperCase());
                double price = configuration.getDouble(type1 + ".Price");
                int dropChance = configuration.getInt(type1 + ".Drop Chance");
                Set<String> alias = new HashSet<>();

                for (String spawner : configuration.getStringList(type1 + ".Alias")) {
                    alias.add(spawner.toLowerCase());
                }

                Spawner spawner1 = new Spawner(e, type1, price, dropChance);

                SpawnerManager.creatureData.put(alias, spawner1);
            } catch (Exception exception) {
                Main.getInstance().getLogger().log(Level.SEVERE, "Could not load " + type1);
            }
        }

    }

    public static Spawner getSpawner(String type) {

        for (Object o : SpawnerManager.creatureData.entrySet()) {
            Entry entry = (Entry) o;

            if (((HashSet) entry.getKey()).contains(type.toLowerCase())) {
                return (Spawner) entry.getValue();
            }
        }

        return null;
    }

    public static Collection getAllSpawners() {
        return SpawnerManager.creatureData.values();
    }
}
