package me.spikey.midnightcore.customend;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Player only.");
            return true;
        }

        Player player = (((Player) commandSender));

        if (!player.getName().equals("creativename11")) {
            player.sendMessage("Only spikey can run this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("no args");
            return true;
        }

        if (args[0].equals("heart")) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), CustomItems.getEndermanHeart());
            return true;
        }

        if (args[0].equals("endChest")) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), CustomItems.getEndChestplate());
            return true;
        }

        if (args[0].equals("ess")) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), CustomItems.getCreeperEssence());
            return true;
        }
        return true;
    }
}
