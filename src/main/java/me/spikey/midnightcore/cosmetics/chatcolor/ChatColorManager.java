package me.spikey.midnightcore.cosmetics.chatcolor;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.homes.Home;
import me.spikey.midnightcore.homes.HomesDAO;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class ChatColorManager implements Listener {
    private Map<UUID, ChatColor> colors;
    private Plugin plugin;

    public ChatColorManager(Plugin plugin) {
        this.plugin = plugin;
        this.colors = Maps.newHashMap();
        SchedulerUtils.runDatabase(ChatColorDAO::createTable);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void setColor(ChatColor color, UUID uuid) {
        colors.put(uuid, color);
        SchedulerUtils.runDatabaseAsync((connection -> {
            ChatColorDAO.add(connection, uuid, color);
        }));
    }

    public void removeColor(UUID uuid) {
        colors.remove(uuid);
        SchedulerUtils.runDatabaseAsync((connection -> {
            ChatColorDAO.remove(connection, uuid);
        }));
    }

    public ChatColor getChatColor(UUID uuid) {
        return colors.get(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            colors.put(event.getUniqueId(), ChatColorDAO.get(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        colors.remove(event.getPlayer().getUniqueId());
    }


}
