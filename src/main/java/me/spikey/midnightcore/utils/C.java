package me.spikey.midnightcore.utils;

import net.md_5.bungee.api.ChatColor;

public class C {
    public static ChatColor red = ChatColor.of("#ff5252");
    public static ChatColor orange = ChatColor.of("#ff8352");
    public static ChatColor yellow = ChatColor.of("#fff652");
    public static ChatColor green = ChatColor.of("#7dff52");
    public static ChatColor blue = ChatColor.of("#52a9ff");
    public static ChatColor purple = ChatColor.of("#cb52ff");
    public static ChatColor pink = ChatColor.of("#ff52ee");
    public static ChatColor gray = ChatColor.GRAY;
    public static ChatColor white = ChatColor.WHITE;

    public static ChatColor lightRed = ChatColor.of("#ffbfbf");
    public static ChatColor lightOrange = ChatColor.of("#ffd1bf");
    public static ChatColor lightYellow = ChatColor.of("#fffcbf");
    public static ChatColor lightGreen = ChatColor.of("#cfffbf");
    public static ChatColor lightBlue = ChatColor.of("#bfdfff");
    public static ChatColor lightPurple =  ChatColor.of("#ecbfff");
    public static ChatColor lightPink = ChatColor.of("#ffbff9");

    public static String convertToColored(String s) {
        s = s.replaceAll("%red%", red + "");
        s = s.replaceAll("%orange%", orange + "");
        s = s.replaceAll("%yellow%", yellow + "");
        s = s.replaceAll("%green%", green + "");
        s = s.replaceAll("%blue%", blue + "");
        s = s.replaceAll("%purple%", purple + "");
        s = s.replaceAll("%pink%", pink + "");
        s = s.replaceAll("%gray%", gray + "");
        s = s.replaceAll("%white%", white + "");

        s = s.replaceAll("%lightred%", lightRed + "");
        s = s.replaceAll("%lightorange%", lightOrange + "");
        s = s.replaceAll("%lightyellow%", lightYellow + "");
        s = s.replaceAll("%lightgreen%", lightGreen + "");
        s = s.replaceAll("%lightblue%", lightBlue + "");
        s = s.replaceAll("%lightpurple%", lightPurple + "");
        s = s.replaceAll("%lightpink%", lightPink + "");

        return s;
    }
}
