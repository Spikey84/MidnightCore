package me.spikey.midnightcore.homes;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Home {
    private Location location;
    private UUID uuid;
    private int id = -1;
    private String name;

    public Home(Location location, UUID uuid, String name) {
        this.location = location;
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void teleportToHome(Player player) {
        player.teleport(location);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
