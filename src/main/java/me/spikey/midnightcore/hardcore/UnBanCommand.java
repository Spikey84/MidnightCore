package me.spikey.midnightcore.hardcore;

import me.spikey.midnightcore.utils.C;
import me.spikey.spikeycooldownapi.API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class UnBanCommand implements CommandExecutor {
    private HardcoreManager hardcoreManager;
    private API api;

    public UnBanCommand(HardcoreManager hardcoreManager, API api) {

        this.hardcoreManager = hardcoreManager;
        this.api = api;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;

            if (!player.hasPermission("core.unban")) {
                sender.sendMessage("You do not have permission to run this command.");
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage(C.purple + "Please enter a user to lookup their death ban status.");
            return true;
        }

        if (Bukkit.getOfflinePlayer(args[0]) == null) {
            sender.sendMessage(C.purple + "This user has never logged in.");
            return true;
        }

        UUID uuid =  Bukkit.getOfflinePlayer(args[0]).getUniqueId();

        if (!api.isOnCooldown(uuid, 0)) {
            sender.sendMessage(C.purple + "This user is not currently death banned.");
            return true;
        }

        api.updateCooldown(uuid, 0, LocalDateTime.MIN);
        sender.sendMessage(C.purple + "This user is now unbanned.");
        return true;
    }
}
