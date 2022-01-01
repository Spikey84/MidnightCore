package me.spikey.midnightcore.discord.linking;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LinkMinecraftCommand implements CommandExecutor {
    private LinkingManager linkingManager;

    public LinkMinecraftCommand(LinkingManager linkingManager) {

        this.linkingManager = linkingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Please enter the id sent to your discord account. If you do not have a key, run !mclink on discord.");
            return true;
        }

        String key = args[0];

        if (linkingManager.acceptLink(key, player.getUniqueId())) {
            String discordName = linkingManager.getDiscordManager().getJda().getUserById(linkingManager.getLinkedDiscordID(player.getUniqueId())).getName();
            player.sendMessage("Your minecraft account is now linked with %s on discord!".formatted(discordName));
        } else {
            player.sendMessage("Invalid Key!");
        }
        return true;
    }
}
