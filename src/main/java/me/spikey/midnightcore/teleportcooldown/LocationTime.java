package me.spikey.midnightcore.teleportcooldown;

import org.bukkit.Location;

public class LocationTime {
    private int ticksRemaining;
    private Location location;

    public LocationTime(int ticksRemaining, Location location) {
        this.ticksRemaining = ticksRemaining;

        this.location = location;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTicksRemaining(int ticksRemaining) {
        this.ticksRemaining = ticksRemaining;
    }

    public void removeOneSec() {
        this.ticksRemaining = this.ticksRemaining-20;
    }
}
