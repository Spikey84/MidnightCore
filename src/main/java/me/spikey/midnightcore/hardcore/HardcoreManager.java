package me.spikey.midnightcore.hardcore;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.pvparea.PvpAreaManager;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.SchedulerUtils;
import me.spikey.spikeycooldownapi.API;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class HardcoreManager implements Listener {
    private API api;
    private Plugin plugin;
    private TimeJoinedManager timeJoinedManager;
    private PvpAreaManager pvpAreaManager;
    private Economy eco;

    private List<UUID> isDead;

    private List<UUID> bypassDeath;

    public HardcoreManager(API api, Plugin plugin, PvpAreaManager pvpAreaManager, Economy eco) {
        this.api = api;
        this.plugin = plugin;
        this.timeJoinedManager = new TimeJoinedManager(plugin);
        this.pvpAreaManager = pvpAreaManager;
        this.eco = eco;

        this.bypassDeath = Lists.newArrayList();
        this.isDead = Lists.newArrayList();

        api.registerCooldown(0, "death");


        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (pvpAreaManager.isInPvpZone(player)) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            player.sendMessage("You are inside a pvp zone, therefore you will not be banned. However you have lost $%s.".formatted(Math.round((eco.getBalance(player))*0.1)));
            if (event.getPlayer().getKiller() != null) {
                Player killer = event.getPlayer().getKiller();
                killer.sendMessage("You have killed %s providing you a reward of $%s.".formatted(player.getName(), Math.round((eco.getBalance(player))*0.1)));
                eco.depositPlayer(killer, Math.round((eco.getBalance(player))*0.1));
            }
            eco.withdrawPlayer(player, Math.round((eco.getBalance(player))*0.1));

            Bukkit.broadcastMessage(C.red + "" + "PVP: " + ChatColor.RESET + "" + C.gray + event.getDeathMessage());
            event.setDeathMessage("");
            return;
        }
        if (player.hasPermission("core.bypassdeath")) return;

        if (bypassDeath.contains(player.getUniqueId())) {
            bypassDeath.remove(player.getUniqueId());
            for (ItemStack i : Lists.newArrayList(event.getDrops())) {
                event.getDrops().remove(i);
            }
            return;
        }

        if (!timeJoinedManager.hasBeenOnLongerThan(event.getPlayer(), 1440)) {// 1440 for 1 day
            player.sendMessage("You have died, however you will not be banned as you first joined within a day of now. After today every death with result in you being banned for short period of time.");
            return;
        }

        api.updateCooldown(player, 0);

        event.getPlayer().kick(Component.text(org.bukkit.ChatColor.LIGHT_PURPLE + "You have died. You may rejoin in %s%s%s. Please join our discord at https://discord.gg/4xsE9MUUKa".formatted(org.bukkit.ChatColor.DARK_PURPLE, api.getRemainingFormattedLong(event.getPlayer().getUniqueId(), 0), ChatColor.LIGHT_PURPLE)), PlayerKickEvent.Cause.BANNED);
        event.setCancelled(true);
        setIsDead(player.getUniqueId(), true);
        Bukkit.broadcast(new TextComponent(ChatColor.YELLOW + event.getDeathMessage()));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {

        if (event.getPlayer().hasPermission("core.bypassdeath")) return;

        if (timeJoinedManager.firstJoin(event.getPlayer())) {
            timeJoinedManager.setTimeJoined(event.getPlayer(), Timestamp.from(Instant.now()));
        }

        if (!api.isOnCooldown(event.getPlayer(), 0)) {

            if (isDead.contains(event.getPlayer().getUniqueId())) {
                bypassDeath.add(event.getPlayer().getUniqueId());
                event.getPlayer().setHealth(0);
                setIsDead(event.getPlayer().getUniqueId(), false);
            }

            return;
        }

        event.getPlayer().kick(Component.text(org.bukkit.ChatColor.LIGHT_PURPLE + "You have died. You may rejoin in %s%s%s. Please join our discord at https://discord.gg/4xsE9MUUKa".formatted(org.bukkit.ChatColor.DARK_PURPLE, api.getRemainingFormattedLong(event.getPlayer().getUniqueId(), 0), ChatColor.LIGHT_PURPLE)), PlayerKickEvent.Cause.BANNED);
        event.setJoinMessage("");


        }

    @EventHandler
    public void playerLeave(PlayerKickEvent event) {
        if (!api.isOnCooldown(event.getPlayer(), 0)) return;
        event.leaveMessage(Component.text(""));
    }

    public TimeJoinedManager getTimeJoinedManager() {
        return timeJoinedManager;
    }


    public void setIsDead(UUID uuid, Boolean bool) {
        if (bool) {
            isDead.add(uuid);
            SchedulerUtils.runDatabaseAsync((connection -> {
                DeathDAO.addDeath(connection, uuid);
            }));
        } else {
            isDead.remove(uuid);
            SchedulerUtils.runDatabaseAsync((connection -> {
                DeathDAO.removeDeath(connection, uuid);
            }));
        }
    }

    @EventHandler
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            if (!DeathDAO.getDead(connection, event.getUniqueId())) return;
            isDead.add(event.getUniqueId());
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        isDead.remove(event.getPlayer().getUniqueId());
    }

}
