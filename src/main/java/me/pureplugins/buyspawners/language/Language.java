package me.pureplugins.buyspawners.language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.pureplugins.buyspawners.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Language {

    PREFIX("prefix", "&b[&aSpawners&b] &f"),
    NO_PERMISSIONS("no-permissions", "&cYou don\'t have the correct permissions to do that."),
    NO_PERMS_BUY("no-perms-buy", "&CYou don\'t heve permission to buy that spawner type."),
    NO_PERMS_UPDATE("no-perms-update", "&cYou don\'t have permissions to update this spawner sign."),
    NO_FUNDS("no-funds", "&cYou dont have enough funds to do that"),
    BROKE_SIGN("broke-sign", "&7You broke a spawner sign."),
    USER_NOT_FOUND("user-not-found", "&cCould not locate that user."),
    NO_CONSOLE("no-console", "That command must be executed by a player."),
    INVALID_MOB("invalid-mob", "&cThat mob dosen\'t exist."),
    INVALID_NUMBER("invalid-number", "&cNumber format Exception."),
    SUCCESS_PURCHASE("sucsess-purchase", "&7You successfully purchased &e%amount% &c%type% &7spawner."),
    SUCCESS_GIVE("sucsess-give", "&7You gave &a%user% &e%amount% &c%type% &7spawners."),
    SUCCESS_UPDATE("sucsess-update", "&7You successfully updated that spawner sign"),
    SIGN_CREATE("sign-create", "&7You created a %type% &7buy sign."),
    SPAWNERS_ADDED("spawners-added", "&e%amount% &a%type% &7spawners added to your inventory.");

    private String path;
    private String def;
    private static YamlConfiguration LANG;

    private Language(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(YamlConfiguration file) {
        Language.LANG = file;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', Language.LANG.getString(this.path, this.def));
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }

    public static class load {

        public static void loadLang() {
            File file = new File(Main.getInstance().getDataFolder(), "lang.yml");

            if (!file.exists()) {
                try {
                    Main.getInstance().getDataFolder().mkdir();
                    file.createNewFile();
                    InputStream configuration = Main.getInstance().getResource("lang.yml");

                    if (configuration != null) {
                        YamlConfiguration yamlconfiguration = YamlConfiguration.loadConfiguration(configuration);

                        yamlconfiguration.save(file);
                        Language.LANG = yamlconfiguration;
                        return;
                    }
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                    Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
                }
            }

            YamlConfiguration yamlconfiguration1 = YamlConfiguration.loadConfiguration(file);

            for (Language e : Language.values()) {
                if (yamlconfiguration1.getString(e.getPath()) == null) {
                    yamlconfiguration1.set(e.getPath(), e.getDefault());
                }
            }

            Language.LANG = yamlconfiguration1;
            Main.LANG = yamlconfiguration1;
            Main.LANG_FILE = file;

            try {
                yamlconfiguration1.save(Main.getInstance().getLangFile());
            } catch (IOException ioexception1) {
                ioexception1.printStackTrace();
            }

        }
    }
}
