package me.pureplugins.buyspawners.handler;

import org.bukkit.entity.EntityType;

public class Spawner {

    private final EntityType type;
    private final String name;
    private final double price;
    private final int dropChance;

    public Spawner(EntityType type, String name, double price, int dropChance) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.dropChance = dropChance;
    }

    public EntityType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getDropChance() {
        return this.dropChance;
    }
}
