package me.spikey.midnightcore.discord.linking;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UnlinkMinecraftCommand implements CommandExecutor {
    private LinkingManager linkingManager;

    public UnlinkMinecraftCommand(LinkingManager linkingManager) {

        this.linkingManager = linkingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return true;
        }

        Player player = ((Player) sender);

        if (!linkingManager.isLinked(player.getUniqueId())) {
            player.sendMessage("You do not have a discord account to linked.");
            return true;
        }

        if (linkingManager.unlinkAccount(player.getUniqueId())) {
            player.sendMessage("Your account has been unlinked!");
        } else {
            player.sendMessage("You do not have a discord account to linked.");

        }
        return true;
    }
}
