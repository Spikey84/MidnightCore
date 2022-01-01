package me.spikey.midnightcore.homes;

import me.spikey.midnightcore.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

public class SethomeCommand implements CommandExecutor {
    private HomeManager homeManager;

    public SethomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            ChatUtils.messageHomes(player, "Please enter a name for your home.");
            return true;
        }

        String name = args[0];

        for (Home home : homeManager.getHomes(player.getUniqueId())) {
            if (home.getName().toUpperCase().equals(name.toUpperCase())) {
                ChatUtils.messageHomes(player, "You already have a home with this name.");
                return true;
            }
        }



        int homeAmount = homeManager.getMaxHomeAmount(player);

        if (homeManager.getHomes(player.getUniqueId()).size() >= homeAmount) {
            ChatUtils.messageHomes(player, "You are already at your max number of homes.");
            return true;
        }

        homeManager.addHome(new Home(player.getLocation(), player.getUniqueId(), name));
        ChatUtils.messageHomes(player, "Home created!");
        return true;
    }
}
