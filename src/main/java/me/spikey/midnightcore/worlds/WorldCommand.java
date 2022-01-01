package me.spikey.midnightcore.worlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class WorldCommand implements CommandExecutor {
    private Plugin plugin;

    public WorldCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.worlds")) {
            sender.sendMessage("You do not have permission to run this command.");
            return true;
        }

        player.openInventory(new WorldGUI(plugin, player).getInventory());

        return true;
    }
}
