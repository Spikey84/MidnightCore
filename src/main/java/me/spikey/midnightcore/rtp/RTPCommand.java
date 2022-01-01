package me.spikey.midnightcore.rtp;


import me.spikey.midnightcore.utils.SchedulerUtils;
import me.spikey.spikeycooldownapi.API;
import me.spikey.spikeycooldownapi.utils.PermissionUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class RTPCommand implements CommandExecutor {

    private API api;

    public RTPCommand(API api) {

        this.api = api;

        api.registerCooldown(100, "rtp");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("spikey.rtp")) {
            sender.sendMessage("You do not have permission to run this command.");
            return true;
        }

        if (api.isOnCooldown(player.getUniqueId(), 100)) {
            sender.sendMessage("You cannot run this command for another %s minutes(s).".formatted(api.getRemainingFormattedLong(player.getUniqueId(), 100)));
            return true;
        }

        rtp(player);
        sender.sendMessage("You have been Teleported.");
        api.updateCooldown(player.getUniqueId(), 100);
        return true;
    }

    public boolean rtp(Player player) {

        int x;
        int z;
        Location loc = player.getLocation();
        boolean validLoc = false;
        int attempts = 0;
        //get init loc
        while(validLoc == false) {
            x = genLoc(loc.getWorld());
            z = genLoc(loc.getWorld());
            //go down and get sea lev
            loc = new Location(Bukkit.getWorld(player.getWorld().getName()), x, 255, z);
            while (loc.getBlock().getType() == Material.AIR) {
                loc.setY(loc.getBlockY() - 1);
            }
            //if its not valid find new
            if (loc.getBlock().getType() == Material.GRASS_BLOCK && loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ()).getType() == Material.AIR && loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY()+2,loc.getBlockZ()).getType() == Material.AIR) validLoc = true;
            attempts++;
            if(attempts > 80) {
                player.sendMessage("Unable to find a suitable location. Please re-run the command or re-enter portal. Sorry.");
                return false;
            }
        }
        //teleport player
        loc.setY(loc.getBlockY() + 1);
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        loc.getChunk().load();
        Location finalLoc = loc;
        teleLater(finalLoc, player);
        return true;
    }

    //generates a random number between the max distance and the negative max distance
    private static int genLoc(World world) {
        return (int) (Math.round(((Math.random() * 10000) % world.getWorldBorder().getSize()) - (world.getWorldBorder().getSize()/2)));
        //return (int) Math.round(((Math.random() * 10000) % ((int) (world.getWorldBorder().getSize()*2) * 2)) - ( (int) (world.getWorldBorder().getSize()*2)));
    }

    //returns a list of all blocks inside a box made by two locations given in a specified world
    public static ArrayList<Location> blocksBetweenPoints(World world, Location loc1, Location loc2){
        ArrayList<Location> locations = new ArrayList<Location>();

        int lowX;
        int lowY;
        int lowZ;
        int highX;
        int highY;
        int highZ;
        if(loc1.getBlockX() < loc2.getBlockX()) {
            lowX = loc1.getBlockX();
            highX = loc2.getBlockX();
        } else {
            highX = loc1.getBlockX();
            lowX = loc2.getBlockX();
        }

        if(loc1.getBlockY() < loc2.getBlockY()) {
            lowY = loc1.getBlockY();
            highY = loc2.getBlockY();
        } else {
            highY = loc1.getBlockY();
            lowY = loc2.getBlockY();
        }

        if(loc1.getBlockZ() < loc2.getBlockZ()) {
            lowZ = loc1.getBlockZ();
            highZ = loc2.getBlockZ();
        } else {
            highZ = loc1.getBlockZ();
            lowZ = loc2.getBlockZ();
        }
        Bukkit.getLogger().info("low:"+lowY + " high:" + highY);
        for(int x = lowX; x < highX; x++) {
            for(int y = lowY; y<highY; y++) {
                for(int z = lowZ; z<highZ; z++) {
                    locations.add(new Location(world,x,y,z));
                }
            }
        }
        return locations;
    }

    private void teleLater(Location finalLoc, Player player) {
        SchedulerUtils.runLater(() -> {
            if (!finalLoc.getChunk().isLoaded()) {
                teleLater(finalLoc, player);
            };
            player.teleport(finalLoc);
            player.sendMessage(ChatColor.WHITE + "Teleported to " + ChatColor.DARK_AQUA + finalLoc.getBlockX() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + finalLoc.getBlockY() + ChatColor.WHITE + ", " + ChatColor.DARK_AQUA + finalLoc.getBlockZ() + ChatColor.WHITE + ".");

        }, 10);
    }

}
