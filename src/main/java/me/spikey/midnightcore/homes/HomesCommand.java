package me.spikey.midnightcore.homes;

import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class HomesCommand implements CommandExecutor {
    private HomeManager homeManager;
    private TeleportManager teleportManager;
    private Plugin plugin;

    public HomesCommand(HomeManager homeManager, TeleportManager teleportManager, Plugin plugin) {
        this.homeManager = homeManager;
        this.teleportManager = teleportManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        new HomesInventory(plugin, player, homeManager, teleportManager).open(player);
        return true;
    }
}
