package me.spikey.midnightcore.hardcore;

import me.spikey.spikeycooldownapi.API;
import net.dv8tion.jda.api.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BanTimeCommand implements CommandExecutor {
    private API api;

    public static ChatColor purple = ChatColor.of("#ab34eb");

    public BanTimeCommand(API api) {

        this.api = api;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage(purple + "Please enter a user to lookup their death ban status.");
            return true;
        }

        if (Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(purple + "This user has never logged in.");
            return true;
        }

        UUID uuid =  Bukkit.getOfflinePlayer(args[0]).getUniqueId();

        if (!api.isOnCooldown(uuid, 0)) {
            sender.sendMessage(purple + "This user is not currently death banned.");
            return true;
        }

        sender.sendMessage(purple + "%s is currently death banned for another %s.".formatted(args[0], api.getRemainingFormattedLong(uuid, 0)));

        return true;
    }
}
