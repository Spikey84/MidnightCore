package me.spikey.midnightcore.adverts;



import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.spikey.midnightcore.discord.linking.LinkingDAO;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class ToggleManager implements Listener {
    private List<UUID> noAdverts;

    public ToggleManager(Plugin plugin) {
        SchedulerUtils.runDatabase((connection -> {
            ToggleDAO.createTable(connection);
        }));
        noAdverts = Lists.newArrayList();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addPlayerToBlackList(UUID uuid) {
        if (noAdverts.contains(uuid)) return;
        noAdverts.add(uuid);
        SchedulerUtils.runDatabaseAsync((connection -> {
            ToggleDAO.add(connection, uuid);
        }));
    }

    public void removePlayerFromBlacklist(UUID uuid) {
        if (!noAdverts.contains(uuid)) return;

        noAdverts.remove(uuid);

        SchedulerUtils.runDatabaseAsync((connection -> {
            ToggleDAO.remove(connection, uuid);
        }));
    }

    public boolean shouldSeeAdverts(UUID uuid) {
        return !noAdverts.contains(uuid);
    }

    //will return true if the player will now show announcements
    public boolean toggleAdverts(UUID uuid) {
        if (noAdverts.contains(uuid)) {
            removePlayerFromBlacklist(uuid);
            return true;
        } else {
            addPlayerToBlackList(uuid);
            return false;
        }
    }

    @EventHandler
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        SchedulerUtils.runDatabase((connection -> {
            if (!ToggleDAO.getNoAdverts(connection, event.getUniqueId())) return;
            noAdverts.add(event.getUniqueId());
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        noAdverts.remove(event.getPlayer().getUniqueId());
    }
}
