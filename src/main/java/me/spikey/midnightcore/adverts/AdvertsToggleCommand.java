package me.spikey.midnightcore.adverts;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdvertsToggleCommand implements CommandExecutor {
    private ToggleManager toggleManager;

    public AdvertsToggleCommand(ToggleManager toggleManager) {

        this.toggleManager = toggleManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (toggleManager.toggleAdverts(player.getUniqueId())) player.sendMessage(ChatColor.YELLOW + "You will now receive announcements. Use /toggle to turn them off.");
        else player.sendMessage(ChatColor.YELLOW + "You will now not receive announcements. Use /toggle to turn them back on");

        return true;
    }
}
