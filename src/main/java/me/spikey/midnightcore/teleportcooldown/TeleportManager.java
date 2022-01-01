package me.spikey.midnightcore.teleportcooldown;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class TeleportManager implements Listener {
    private Map<Player, LocationTime> pendingTeleports;

    private Plugin plugin;

    public TeleportManager(Plugin plugin) {
        this.plugin = plugin;
        pendingTeleports = Maps.newHashMap();

        Bukkit.getPluginManager().registerEvents(this, plugin);

        SchedulerUtils.runRepeating(() -> {
            for (Map.Entry<Player, LocationTime> entry : pendingTeleports.entrySet()) {
                LocationTime locTime = entry.getValue();
                locTime.removeOneSec();
                if (locTime.getTicksRemaining() < 1) completeTeleport(entry.getKey(), locTime);
            }
        }, 20);

    }

    public boolean addTeleport(Player player, Location location) {
        if (pendingTeleports.containsKey(player)) {
            player.sendMessage("You already have a pending teleportation");
            return false;
        }
        pendingTeleports.put(player, new LocationTime(60, location));
        player.sendMessage("You will be teleported in 3 seconds, do not move!");
        return true;
    }

    public void completeTeleport(Player player, LocationTime locationTime) {
        player.sendMessage("You have been teleported!");
        player.teleport(locationTime.getLocation());
        pendingTeleports.remove(player);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        if (!pendingTeleports.containsKey(event.getPlayer())) return;
        if (event.getTo().distance(event.getFrom()) < 0.1) return;
        pendingTeleports.remove(event.getPlayer());
        event.getPlayer().sendMessage("Teleport Canceled, you moved!");
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!pendingTeleports.containsKey(player)) return;
        pendingTeleports.remove(player);
        player.sendMessage("Teleport Canceled, you moved!");
    }

}
