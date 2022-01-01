package me.spikey.midnightcore.homes;

import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.midnightcore.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {
    private HomeManager homeManager;
    private TeleportManager teleportManager;

    public HomeCommand(HomeManager homeManager, TeleportManager teleportManager) {
        this.homeManager = homeManager;
        this.teleportManager = teleportManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            ChatUtils.messageHomes(player, "Please enter a name of the home you wish to travel to.");
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

        teleportManager.addTeleport(player, tmpHome.getLocation());
        return true;
    }
}
