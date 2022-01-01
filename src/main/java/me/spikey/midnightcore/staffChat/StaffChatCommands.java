package me.spikey.midnightcore.staffChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommands implements CommandExecutor {
    private StaffChatManager staffChatManager;

    public StaffChatCommands(StaffChatManager staffChatManager) {
        this.staffChatManager = staffChatManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command only allowed from players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("core.staff")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {

            if(staffChatManager.getPlayerStaffChat(player.getUniqueId())) {
                staffChatManager.setStaffChat(player.getUniqueId(), false);
                sender.sendMessage("You are now not talking in staff chat");
            } else {
                staffChatManager.setStaffChat(player.getUniqueId(), true);
                sender.sendMessage("You are now talking in staff chat");
            }
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String string : args) {
            message.append(string + " ");
        }
        staffChatManager.sendStaffChatMessage(player.getName(), message.toString(), false);
        return true;
    }
}
