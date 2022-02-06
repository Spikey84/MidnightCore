package me.spikey.midnightcore.rtp;

import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.spikeycooldownapi.API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    private API api;
    private TeleportManager teleportManager;

    public SpawnCommand(API api, TeleportManager teleportManager) {

        this.api = api;
        this.teleportManager = teleportManager;

        //api.registerCooldown(101, "spawn");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.spawn")) {
            sender.sendMessage("You do not have permission to run this command.");
            return true;
        }

//        if (api.isOnCooldown(player.getUniqueId(), 101)) {
//            sender.sendMessage("You cannot run this command for another %s.".formatted(api.getRemainingFormattedLong(player.getUniqueId(), 101)));
//            return true;
//        }

        teleportManager.addTeleport(player, Bukkit.getWorld("world").getSpawnLocation());
        //api.updateCooldown(player.getUniqueId(), 101);
        return true;
    }
}
