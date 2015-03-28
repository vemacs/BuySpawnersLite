package me.pureplugins.buyspawners.listeners;

import me.pureplugins.buyspawners.Main;
import me.pureplugins.buyspawners.events.BuySpawnerEvent;
import me.pureplugins.buyspawners.handler.Spawner;
import me.pureplugins.buyspawners.handler.SpawnerManager;
import me.pureplugins.buyspawners.language.Language;
import me.pureplugins.buyspawners.permissions.Permissions;
import me.pureplugins.buyspawners.util.CreateSpawner;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements Listener {

    Main instance = Main.getInstance();
    private final boolean permissionToBuy;

    public SignClickListener() {
        permissionToBuy = instance.getConfig().getBoolean("use permissions to buy");
    }

    @EventHandler
    private void onClick(PlayerInteractEvent evt) {
        Player user = evt.getPlayer();
        Block block = evt.getClickedBlock();

        if (block != null) {
            if (block.getState() instanceof Sign) {
                if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Sign sign = (Sign) block.getState();
                    String[] line = sign.getLines();
                    Spawner spawner;

                    if (user.isSneaking()) {
                        if (!user.hasPermission(Permissions.get("buyspawners.signs.update"))) {
                            instance.message.send(user, Language.NO_PERMS_UPDATE);
                            return;
                        }

                        spawner = SpawnerManager.getSpawner(ChatColor.stripColor(line[2]));
                        if (spawner != null) {
                            sign.setLine(3, "$" + spawner.getPrice());
                            sign.update();
                            instance.message.send(user, Language.SUCCESS_UPDATE);
                            return;
                        }
                    } else if (line[0].contains("[Spawner]") && instance.signLocations.contains(sign.getLocation().toString())) {
                        spawner = SpawnerManager.getSpawner(ChatColor.stripColor(line[2].toLowerCase()));
                        if (spawner != null) {
                            String mob = spawner.getName();

                            if (permissionToBuy && (!user.hasPermission(Permissions.get("buyspawners.signs.use." + mob)) || !user.hasPermission(Permissions.get("buyspawners.signs.use.*")))) {
                                instance.message.send(user, Language.NO_PERMS_BUY);
                                return;
                            }

                            int amount = Integer.parseInt(ChatColor.stripColor(line[1]));

                            if (user.isOp()) {
                                user.getInventory().addItem(CreateSpawner.get(mob, amount));
                                user.updateInventory();
                                BuySpawnerEvent cost1 = new BuySpawnerEvent(user, EntityType.valueOf(mob.toUpperCase()), mob, 0.0D);

                                Bukkit.getPluginManager().callEvent(cost1);
                                instance.message.send(user, Language.SUCCESS_PURCHASE.toString().replace("%amount%", Integer.toString(amount)).replace("%type%", mob));
                                return;
                            }

                            double cost = Double.parseDouble(ChatColor.stripColor(line[3].replace("$", "")));
                            EconomyResponse er = instance.econ.withdrawPlayer(user, cost);

                            if (er.transactionSuccess()) {
                                user.getInventory().addItem(CreateSpawner.get(mob, amount));
                                user.updateInventory();
                                BuySpawnerEvent event = new BuySpawnerEvent(user, EntityType.valueOf(mob.toUpperCase()), mob, cost);

                                Bukkit.getPluginManager().callEvent(event);
                                instance.message.send(user, Language.SUCCESS_PURCHASE.toString().replace("%amount%", Integer.toString(amount)).replace("%type%", mob));
                                return;
                            }

                            instance.message.send(user, Language.NO_FUNDS);
                            return;
                        }
                    }
                }

            }
        }
    }
}
