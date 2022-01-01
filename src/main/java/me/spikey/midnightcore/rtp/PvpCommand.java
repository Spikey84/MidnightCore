package me.spikey.midnightcore.rtp;

import me.spikey.midnightcore.pvparea.PvpAreaManager;
import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.spikeycooldownapi.API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PvpCommand implements CommandExecutor {
    private API api;
    private TeleportManager teleportManager;
    private PvpAreaManager pvpAreaManager;

    public PvpCommand(API api, TeleportManager teleportManager, PvpAreaManager pvpAreaManager) {

        this.api = api;
        this.teleportManager = teleportManager;
        this.pvpAreaManager = pvpAreaManager;

        api.registerCooldown(102, "pvp");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.pvp")) {
            sender.sendMessage("You do not have permission to run this command.");
            return true;
        }

        if (api.isOnCooldown(player.getUniqueId(), 102)) {
            sender.sendMessage("You cannot run this command for another %s minutes(s).".formatted(api.getRemainingFormattedLong(player.getUniqueId(), 102)));
            return true;
        }

        teleportManager.addTeleport(player, pvpAreaManager.getPvpSpawn());
        api.updateCooldown(player.getUniqueId(), 102);
        return true;
    }
}