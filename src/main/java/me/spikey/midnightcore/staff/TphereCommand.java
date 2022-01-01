package me.spikey.midnightcore.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TphereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("MUST BE PLAYER NERD!!!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.staff")) {
            player.sendMessage("You do not have permission to run this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Please provide a player to teleport to.");
            return true;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage("This player is not online.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        target.teleport(player);
        return true;
    }
}
