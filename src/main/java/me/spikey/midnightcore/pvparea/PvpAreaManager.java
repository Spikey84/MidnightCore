package me.spikey.midnightcore.pvparea;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import it.unimi.dsi.fastutil.Pair;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.List;


public class PvpAreaManager implements Listener {
    private Pair<Location, Location> pvpBounds;
    private Pair<Location, Location> hillBounds;

    private Location pvpSpawn;

    private Map<Location, Color> pvpParticles;
    private Plugin plugin;

    private Timestamp lastHillCheck;


    public PvpAreaManager(Plugin plugin) {
        this.plugin = plugin;
        pvpBounds = Pair.of(new Location(Bukkit.getWorld("world"), 138, 79, 270), new Location(Bukkit.getWorld("world"), 123, 70, 254));
        hillBounds = Pair.of(new Location(Bukkit.getWorld("world"), 133, 72, 264), new Location(Bukkit.getWorld("world"), 129, 70, 260));
        pvpSpawn = new Location(Bukkit.getWorld("world"), 138, 79, 270);

        pvpParticles = generateParticles();

        Bukkit.getPluginManager().registerEvents(this, plugin);

        SchedulerUtils.runRepeating(() -> {
            showParticles(Bukkit.getOnlinePlayers());
        }, 20);
        lastHillCheck = Timestamp.from(Instant.now());

        SchedulerUtils.runRepeating(() -> {

            if (getTimeRemainingTimeHillMillis() > 0) return;
            List<Player> onHill = Lists.newArrayList();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!isOnHill(player)) continue;
                onHill.add(player);
            }

            HashMap<Town, Integer> towns = Maps.newHashMap();
            for (Player player : onHill) {
                Town townOrNull = null;
                try {
                    townOrNull = TownyAPI.getInstance().getResident(player).getTown();
                } catch (NotRegisteredException ignored) {
                }
                    if (townOrNull == null) continue;
                    if (towns.containsKey(townOrNull)) towns.put(townOrNull, towns.get(townOrNull) + 1);
                    towns.put(townOrNull, 1);
            }



            if (towns.size() == 0) {
                Bukkit.broadcastMessage(C.red + "" + "PVP: " + ChatColor.RESET + "" + C.gray + "The hill is not currently controlled!");
                return;
            }

            Map.Entry<Town, Integer> max = null;

            for (Map.Entry<Town, Integer> entry : towns.entrySet()) {
                if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
                    max = entry;
                }
            }

            try {
                max.getKey().depositToBank(max.getKey().getMayor(), 100);
            } catch (TownyException e) {
                e.printStackTrace();
            }
            Bukkit.broadcastMessage(C.red + "" + "PVP: " + ChatColor.RESET + "" + C.gray + "The hill is currently controlled by %s. They have gained $100.".formatted(max.getKey().getName()));
            lastHillCheck = Timestamp.from(Instant.now());
        }, 100);
    }

    public boolean isInPvpZone(Player player) {
        if (!(player.getLocation().getY() > pvpBounds.right().getY() && player.getLocation().getY() < pvpBounds.left().getY())) return false;
        if (!(player.getLocation().getX() > pvpBounds.right().getX() && player.getLocation().getX() < pvpBounds.left().getX())) return false;
        if (!(player.getLocation().getZ() > pvpBounds.right().getZ() && player.getLocation().getZ() < pvpBounds.left().getZ())) return false;
        return true;
    }

    public boolean isInPvpZone(Location loc) {
        if (!(loc.getY() > pvpBounds.right().getY() && loc.getY() < pvpBounds.left().getY())) return false;
        if (!(loc.getX() > pvpBounds.right().getX() && loc.getX() < pvpBounds.left().getX())) return false;
        if (!(loc.getZ() > pvpBounds.right().getZ() && loc.getZ() < pvpBounds.left().getZ())) return false;
        return true;
    }

    public boolean isOnHill(Player player) {
        if (!(player.getLocation().getY() > hillBounds.right().getY() && player.getLocation().getY() < hillBounds.left().getY())) return false;
        if (!(player.getLocation().getX() > hillBounds.right().getX() && player.getLocation().getX() < hillBounds.left().getX())) return false;
        if (!(player.getLocation().getZ() > hillBounds.right().getZ() && player.getLocation().getZ() < hillBounds.left().getZ())) return false;
        return true;
    }

    public boolean isOnHill(Location loc) {
        if (!(loc.getY() > hillBounds.right().getY() && loc.getY() < hillBounds.left().getY())) return false;
        if (!(loc.getX() > hillBounds.right().getX() && loc.getX() < hillBounds.left().getX())) return false;
        if (!(loc.getZ() > hillBounds.right().getZ() && loc.getZ() < hillBounds.left().getZ())) return false;
        return true;
    }

    private void showParticles(Collection<? extends Player> onlinePlayers) {
        for (Player player : hillBounds.right().getNearbyPlayers(300)) {
            for (Map.Entry<Location, Color> particle : pvpParticles.entrySet()) {
                particle.getKey().setY(player.getLocation().getY() + 1);
                if (particle.getKey().getY() > pvpBounds.left().getY() && particle.getValue().equals(Color.red)) particle.getKey().setY(pvpBounds.left().getY());
                if (particle.getKey().getY() < pvpBounds.right().getY() && particle.getValue().equals(Color.red)) particle.getKey().setY(pvpBounds.right().getY());

                if (particle.getKey().getY() > hillBounds.left().getY() && particle.getValue().equals(Color.green)) particle.getKey().setY(hillBounds.left().getY());
                if (particle.getKey().getY() < hillBounds.right().getY() && particle.getValue().equals(Color.green)) particle.getKey().setY(hillBounds.right().getY());
                if (player.getWorld() == particle.getKey().getWorld() && player.getLocation().distance(particle.getKey()) < 10) pR(particle.getKey(), player, particle.getValue());
            }
        }

    }

    public void pR(Location location, Player player, Color color) {
        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
        particleBuilder.color(color.getRed(), color.getGreen(), color.getBlue());
        player.spawnParticle(Particle.REDSTONE, location,1, particleBuilder.data());
    }

    public Map<Location, Color> generateParticles() {
        Map<Location, Color> locations = Maps.newHashMap();
        int xLength = hillBounds.left().getBlockX()-hillBounds.right().getBlockX();
        int zLength = hillBounds.left().getBlockZ()-hillBounds.right().getBlockZ();

        Location loc = hillBounds.right().clone();

        locations.put(loc.clone(), Color.green);

        for (double x = 0; x < xLength + 1; x++) {
            loc.setX(loc.getX() + 1);
            locations.put(loc.clone(), Color.green);

        }

        for (double x = 0; x < zLength + 1; x++) {
            loc.setZ(loc.getZ() + 1);
            locations.put(loc.clone(), Color.green);

        }

        for (double x = 0; x < xLength + 1; x++) {
            loc.setX(loc.getX() - 1);
            locations.put(loc.clone(), Color.green);

        }

        for (double x = 0; x < zLength + 1; x++) {
            loc.setZ(loc.getZ() - 1);
            locations.put(loc.clone(), Color.green);
        }

        //

        xLength = pvpBounds.left().getBlockX()-pvpBounds.right().getBlockX();
        zLength = pvpBounds.left().getBlockZ()-pvpBounds.right().getBlockZ();

        loc = pvpBounds.right().clone();

        locations.put(loc.clone(), Color.red);

        for (double x = 0; x < xLength + 1; x++) {
            loc.setX(loc.getX() + 1);
            locations.put(loc.clone(), Color.red);

        }

        for (double x = 0; x < zLength + 1; x++) {
            loc.setZ(loc.getZ() + 1);
            locations.put(loc.clone(), Color.red);

        }

        for (double x = 0; x < xLength + 1; x++) {
            loc.setX(loc.getX() - 1);
            locations.put(loc.clone(), Color.red);

        }

        for (double x = 0; x < zLength + 1; x++) {
            loc.setZ(loc.getZ() - 1);
            locations.put(loc.clone(), Color.red);
        }


        return locations;
    }

    public Location getPvpSpawn() {
        return pvpSpawn;
    }

    public String getTimeRemainingTimeHill() {

        //millis
        long nextHill = lastHillCheck.getTime() + 300000;
        long current = Timestamp.from(Instant.now()).getTime();

        //remaining
        long remaining = nextHill-current;

        return getRemainingFormatted(remaining);

    }

    public long getTimeRemainingTimeHillMillis() {

        //millis
        long nextHill = lastHillCheck.getTime() + 300000;
        long current = Timestamp.from(Instant.now()).getTime();

        //remaining
        long remaining = nextHill-current;

        return remaining;

    }

    public String getRemainingFormatted(long millis) {

        long minutes = (long) Math.floor(millis/60000);
        long seconds = (millis/1000) % 60;

        if (seconds < 10) return String.format("%s:0%s", minutes, seconds);
        return String.format("%s:%s", minutes, seconds);

    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        if (event.getPlayer().getWorld() != pvpBounds.right().getWorld()) return;
        if (isOnHill(event.getTo()) && !isOnHill(event.getFrom())) event.getPlayer().sendMessage(C.red + "" + "PVP: " + ChatColor.RESET + "" + C.lightRed + "You are now on the hill! If your town is occupies the hill in %s you will get a reward!".formatted(getTimeRemainingTimeHill()));
        if (isInPvpZone(event.getTo()) && !isInPvpZone(event.getFrom())) event.getPlayer().sendMessage(C.red + "" + "PVP: " + ChatColor.RESET + "" + C.lightRed + "You are now in the PVP arena, death in this area will cost you money and will not result in a death ban.");
    }


}
