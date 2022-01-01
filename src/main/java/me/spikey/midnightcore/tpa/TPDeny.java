package me.spikey.midnightcore.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPDeny implements CommandExecutor {
    private TPAManager tpaManager;

    public TPDeny(TPAManager tpaManager) {

        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        tpaManager.denyRequest(player);

        return true;
    }
}
