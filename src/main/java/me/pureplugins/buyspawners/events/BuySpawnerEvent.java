package me.pureplugins.buyspawners.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuySpawnerEvent extends Event implements Cancellable {

    private final Player user;
    private final EntityType type;
    private final String name;
    private final double price;
    private boolean cancel;
    private static HandlerList handlers = new HandlerList();

    public BuySpawnerEvent(Player user, EntityType type, String name, double price) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public Player getUser() {
        return this.user;
    }

    public EntityType getType() {
        return this.type;
    }

    public String getEntityName() {
        return this.name;
    }

    public double getCost() {
        return this.price;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return BuySpawnerEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BuySpawnerEvent.handlers;
    }
}
