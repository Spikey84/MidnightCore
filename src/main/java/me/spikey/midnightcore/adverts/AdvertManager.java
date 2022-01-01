package me.spikey.midnightcore.adverts;

import me.spikey.midnightcore.utils.SchedulerUtils;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class AdvertManager {
    private Plugin plugin;
    private List<String> adverts;
    private int num;

    private ToggleManager toggleManager;

    public AdvertManager(Plugin plugin) {

        this.plugin = plugin;
        this.num = 0;

        this.toggleManager = new ToggleManager(plugin);

        adverts = (List<String>) plugin.getConfig().getList("adverts");


        SchedulerUtils.runRepeating(() -> {
            ChatColor chatColor = getFirstColor(adverts.get(num));

            String msg = formatAnnouncement(adverts.get(num));

            if (msg.contains("%discord%")) {
                msg = msg.replaceAll("%discord%", "");
                TextComponent component = new TextComponent(msg);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/4xsE9MUUKa"));
                component.setColor(ChatColor.BLUE);
                sendAdvert(component);

            } else if (msg.contains("%twitch%")) {
                msg = msg.replaceAll("%twitch%", "");
                TextComponent component = new TextComponent(msg);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.twitch.tv/mldnightcity"));
                component.setColor(ChatColor.LIGHT_PURPLE);
                sendAdvert(component);
            } else {
                TextComponent component = new TextComponent(msg);
                component.setColor(chatColor);
                sendAdvert(component);
            }

            num = (num + 1) % adverts.size();
        }, 8000/* 6k*/);
    }

    private void sendAdvert(TextComponent component) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!toggleManager.shouldSeeAdverts(player.getUniqueId())) return;
            player.sendMessage(component);
        }
    }

    private ChatColor getFirstColor(String input) {
        String out = input;

        if (out.contains("%purple%")) return ChatColor.LIGHT_PURPLE;
        if (out.contains("%pink%")) return ChatColor.LIGHT_PURPLE;
        if (out.contains("%orange%")) return ChatColor.YELLOW;
        if (out.contains("%red%")) return ChatColor.RED;
        if (out.contains("%yellow%")) return ChatColor.YELLOW;
        if (out.contains("%green%")) return ChatColor.GREEN;
        if (out.contains("%cyan%")) return ChatColor.AQUA;
        if (out.contains("%blue%")) return ChatColor.BLUE;

        return ChatColor.WHITE;
    }

    private String formatAnnouncement(String input) {
        String out = input;

        out = ChatColor.translateAlternateColorCodes('&', out);
        out = out.replaceAll("%purple%", ChatColor.LIGHT_PURPLE + "");
        out = out.replaceAll("%blue%", ChatColor.BLUE + "");
        out = out.replaceAll("%pink%", ChatColor.LIGHT_PURPLE + "");
        out = out.replaceAll("%orange%", ChatColor.YELLOW + "");
        out = out.replaceAll("%red%", ChatColor.RED + "");
        out = out.replaceAll("%yellow%", ChatColor.YELLOW + "");
        out = out.replaceAll("%green%", ChatColor.GREEN + "");
        out = out.replaceAll("%cyan%", ChatColor.AQUA + "");

        return out;
    }

    public static ChatColor purple = ChatColor.of("#ab34eb");
    public static ChatColor blue = ChatColor.of("#34b4eb");
    public static ChatColor pink = ChatColor.of("#e534eb");
    public static ChatColor orange = ChatColor.of("#f5ad42");
    public static ChatColor red = ChatColor.of("#f54242");
    public static ChatColor yellow = ChatColor.of("#f5f542");
    public static ChatColor green = ChatColor.of("#42f563");
    public static ChatColor cyan = ChatColor.of("#42f563");

    public static TextComponent discordLink = new net.md_5.bungee.api.chat.TextComponent(blue + "Click Here!");


    public ToggleManager getToggleManager() {
        return toggleManager;
    }
}
