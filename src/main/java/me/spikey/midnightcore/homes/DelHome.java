package me.spikey.midnightcore.homes;

import me.spikey.midnightcore.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DelHome implements CommandExecutor {
    private HomeManager homeManager;
    private Plugin plugin;

    public DelHome(HomeManager homeManager, Plugin plugin) {
        this.homeManager = homeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;



        if (!sender.hasPermission("scepter.home")) {
            ChatUtils.messageHomes(player, "You do not have permission to run this command.");
            return true;
        }

        if (args.length < 1) {
            ChatUtils.messageHomes(player, "Please enter the name of the home you want to delete.");
            return true;
        }

        String name = args[0];

        Home tmpHome = null;

        for (Home home : homeManager.getHomes(player.getUniqueId())) {
            if (home.getName().toUpperCase().equals(name.toUpperCase())) {
                tmpHome = home;
            }
        }

        if (tmpHome == null) {
            ChatUtils.messageHomes(player, "You do not have a home with that name.");
            return true;
        }

        homeManager.delHome(tmpHome);
        ChatUtils.messageHomes(player, "Your home has been deleted.");
        return true;
    }
}
