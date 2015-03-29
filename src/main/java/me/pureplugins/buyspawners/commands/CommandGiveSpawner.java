package me.pureplugins.buyspawners.commands;

import me.pureplugins.buyspawners.Main;
import me.pureplugins.buyspawners.handler.Spawner;
import me.pureplugins.buyspawners.handler.SpawnerManager;
import me.pureplugins.buyspawners.language.Language;
import me.pureplugins.buyspawners.permissions.Permissions;
import me.pureplugins.buyspawners.util.CreateSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandGiveSpawner implements CommandExecutor {

    Main instance = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player spawner1 = (Player) sender;

            if (!sender.hasPermission(Permissions.get("buyspawners.cmd.give"))) {
                instance.message.send(spawner1, Language.NO_PERMISSIONS);
                return false;
            } else if (args.length == 3) {
                Spawner mob1 = SpawnerManager.getSpawner(args[0]);

                if (mob1 != null) {
                    String e1 = mob1.getName();
                    EntityType t1 = mob1.getType();

                    try {
                        int target2 = Integer.parseInt(args[1]);
                        Player target1 = Bukkit.getPlayer(args[2]);

                        if (target1 != null) {
                            if (target1 != spawner1) {
                                target1.getInventory().addItem(CreateSpawner.createSpawner(t1, target2));
                                target1.updateInventory();
                                instance.message.send(spawner1, Language.SUCCESS_GIVE.toString().replace("%user%", target1.getName()).replace("%amount%", Integer.toString(target2)).replace("%type%", args[0]));
                                instance.message.send(target1, Language.SPAWNERS_ADDED.toString().replace("%amount%", Integer.toString(target2)).replace("%type%", e1));
                                return true;
                            } else {
                                spawner1.getInventory().addItem(CreateSpawner.createSpawner(t1, target2));
                                spawner1.updateInventory();
                                instance.message.send(spawner1, Language.SPAWNERS_ADDED.toString().replace("%amount%", Integer.toString(target2)).replace("%type%", e1));
                                return true;
                            }
                        } else {
                            instance.message.send(spawner1, Language.USER_NOT_FOUND);
                            return false;
                        }
                    } catch (NumberFormatException numberformatexception) {
                        instance.message.send(spawner1, Language.INVALID_NUMBER);
                        return false;
                    }
                } else {
                    instance.message.send(spawner1, Language.INVALID_MOB);
                    return false;
                }
            } else {
                instance.message.send(spawner1, ChatColor.RED + "Try: /givespawner <mobType> <amount> <user>");
                return false;
            }
        } else {
            Spawner spawner = SpawnerManager.getSpawner(args[0]);

            if (spawner != null) {
                String mob = spawner.getName();

                try {
                    int e = Integer.parseInt(args[1]);
                    Player target = Bukkit.getPlayer(args[2]);

                    if (target != null) {
                        target.getInventory().addItem(CreateSpawner.createSpawner(spawner.getType(), e));
                        target.updateInventory();
                        target.sendMessage(Language.PREFIX.toString() + Language.SPAWNERS_ADDED.toString().replace("%amount%", Integer.toString(e)).replace("%type%", mob));
                        return false;
                    } else {
                        sender.sendMessage(Language.PREFIX.toString() + Language.USER_NOT_FOUND);
                        return false;
                    }
                } catch (NumberFormatException numberformatexception1) {
                    sender.sendMessage(Language.PREFIX.toString() + Language.INVALID_NUMBER);
                    return false;
                }
            } else {
                sender.sendMessage(Language.PREFIX.toString() + ChatColor.RED + "Try: /givespawner <mobType> <amount> <user>");
                return false;
            }
        }
    }
}
