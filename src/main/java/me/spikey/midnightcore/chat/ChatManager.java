package me.spikey.midnightcore.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.spikey.midnightcore.Main;
import me.spikey.midnightcore.utils.ChatUtils;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public class ChatManager implements Listener {
    private Main main;
    public static net.md_5.bungee.api.ChatColor blue = net.md_5.bungee.api.ChatColor.of("#34b4eb");
    public static net.md_5.bungee.api.ChatColor green = net.md_5.bungee.api.ChatColor.of("#42f563");

    public ChatManager(Main main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);
        if (event.getMessage().contains("[event cancelled by LiteBans]")) return;
        SchedulerUtils.runAsync(() -> {
            net.md_5.bungee.api.ChatColor chatColor = main.getCosmeticManager().getChatColorManager().getChatColor(event.getPlayer().getUniqueId());
            if (chatColor == null) chatColor = net.md_5.bungee.api.ChatColor.GRAY;

            String name = event.getPlayer().getName();

            String prefix = main.getChat().getPlayerPrefix(event.getPlayer());

            String placeholder = PlaceholderAPI.setPlaceholders(event.getPlayer(), "%townyadvanced_town%");
            placeholder = ChatColor.stripColor(placeholder);
            placeholder = green + placeholder;

            String msg = String.format("%s%s&f%s%s" + ChatColor.WHITE + ": ", placeholder, prefix, name, main.getCosmeticManager().getTagsManager().getTagString(event.getPlayer()));

            msg = ChatUtils.hexTranslation(msg);
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            msg = msg + chatColor + event.getMessage();

            Bukkit.getServer().broadcastMessage(msg);
        });
    }

}
