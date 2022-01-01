package me.spikey.midnightcore.staff;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpectatorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.gamemode")) {
            player.sendMessage("You do not have permission to run this command.");
            return true;
        }

        if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            player.sendMessage("You are already in spectator mode.");
            return true;
        }

        player.setGameMode(GameMode.SPECTATOR);
        return true;
    }
}
