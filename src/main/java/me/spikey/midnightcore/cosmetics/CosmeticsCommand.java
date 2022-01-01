package me.spikey.midnightcore.cosmetics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CosmeticsCommand implements CommandExecutor {
    private Plugin plugin;
    private CosmeticManager cosmeticManager;

    public CosmeticsCommand(Plugin plugin, CosmeticManager cosmeticManager) {
        this.plugin = plugin;
        this.cosmeticManager = cosmeticManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        player.openInventory(new MasterInventory(plugin, player, cosmeticManager).getInventory());
        return true;
    }
}
