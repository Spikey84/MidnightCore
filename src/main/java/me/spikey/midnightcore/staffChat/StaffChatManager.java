package me.spikey.midnightcore.staffChat;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.DatabaseManager;
import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class StaffChatManager implements Listener {
    private HashMap<UUID, Boolean> staffChatSettings;


    private JDA jda;
    private DiscordManager discordManager;
    private Plugin plugin;

    public StaffChatManager(JDA jda, DiscordManager discordManager, Plugin plugin) {
        this.discordManager = discordManager;
        this.staffChatSettings = Maps.newHashMap();
        this.jda = jda;
        this.plugin = plugin;

//        SchedulerUtils.runDatabase((connection) -> {
//                staffChatSettings = StaffChatDAO.getStaffChatValues(connection);
//        });

        Bukkit.getPluginManager().registerEvents(new StaffChatMinecraftMessage(this), plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean getPlayerStaffChat(UUID uuid) {
        staffChatSettings.putIfAbsent(uuid, false);
        return staffChatSettings.get(uuid);
    }

    public void setStaffChat(UUID uuid, boolean value) {
        staffChatSettings.put(uuid, value);
        SchedulerUtils.runDatabaseAsync((connection) -> {
                StaffChatDAO.changeStaffChatAccount(connection, uuid, value);
        });
    }

    public void sendStaffChatMessage(String name, String message, boolean fromDiscord) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("core.staff")) continue;
            player.sendMessage(String.format(ChatColor.YELLOW + "[Staff] %s: %s", ChatColor.WHITE + "" + name, message));
        }

        if (fromDiscord) return;

        jda.getGuildById(discordManager.getServerID()).getTextChannelById(discordManager.getStaffChannelID()).sendMessage(String.format("%s: %s", name, message)).queue();

    }

    public DiscordManager getDiscordManager() {
        return discordManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            staffChatSettings.put(event.getUniqueId(), StaffChatDAO.getStaffChatValue(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        staffChatSettings.remove(event.getPlayer().getUniqueId());
    }
}
