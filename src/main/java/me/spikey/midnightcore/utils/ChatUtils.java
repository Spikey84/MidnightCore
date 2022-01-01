package me.spikey.midnightcore.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;

public class ChatUtils {
    public static ChatColor purple = ChatColor.of("#ab34eb");
    public static ChatColor blue = ChatColor.of("#34b4eb");
    public static ChatColor pink = ChatColor.of("#e534eb");
    public static ChatColor orange = ChatColor.of("#f5ad42");
    public static ChatColor red = ChatColor.of("#f54242");
    public static ChatColor yellow = ChatColor.of("#f5f542");
    public static ChatColor green = ChatColor.of("#42f563");
    public static ChatColor cyan = ChatColor.of("#42f563");


    public static String hexTranslation(String input) {
        String out = input;
        out = out.replaceAll("&#ab34eb", purple + "");
        out = out.replaceAll("&#34b4eb", blue + "");
        out = out.replaceAll("&#e534eb", pink + "");
        out = out.replaceAll("&#f5ad42", orange + "");
        out = out.replaceAll("&#f54242", red + "");
        out = out.replaceAll("&#f5f542", yellow + "");
        out = out.replaceAll("&#42f563", green + "");
        out = out.replaceAll("&#42f563", cyan + "");
        return out;
    }

    public static void messageHomes(Player player, String string) {
        player.sendMessage(purple + "" + ChatColor.BOLD + "Homes" + ChatColor.RESET + "" + ChatColor.WHITE + " " + string);
    }
}
