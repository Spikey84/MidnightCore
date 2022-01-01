package me.spikey.midnightcore.hardcore;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.DatabaseManager;
import me.spikey.midnightcore.homes.Home;
import me.spikey.midnightcore.homes.HomesDAO;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class TimeJoinedManager implements Listener {
    private HashMap<UUID, Timestamp> timeJoined;

    public TimeJoinedManager(Plugin plugin) {
        timeJoined = Maps.newHashMap();
        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    public void setTimeJoined(Player player, Timestamp timestamp) {
        timeJoined.put(player.getUniqueId(), timestamp);

        SchedulerUtils.runDatabaseAsync((connection -> {
            TimeJoinedDAO.setTime(connection, player.getUniqueId(), timestamp);
        }));
        ItemStack item = new ItemStack(Material.COOKED_BEEF);
        item.setAmount(20);
        player.getInventory().setItem(player.getInventory().firstEmpty(), item);
    }

    public boolean firstJoin(Player player) {
        return !timeJoined.containsKey(player.getUniqueId());
    }

    public boolean hasBeenOnLongerThan(Player player, long timeInMins) {
        long joined = timeJoined.get(player.getUniqueId()).getTime()/60000;

        long now = Timestamp.from(Instant.now()).getTime()/60000;

        long dif = now-joined;

        return dif > timeInMins;


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        SchedulerUtils.runDatabase((connection -> {
            Timestamp time = TimeJoinedDAO.getTimeJoined(connection, event.getUniqueId());
            if (time == null) return;
            timeJoined.put(event.getUniqueId(), time);
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        timeJoined.remove(event.getPlayer().getUniqueId());
    }

}
