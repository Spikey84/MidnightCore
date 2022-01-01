package me.spikey.midnightcore.discord.linking;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.midnightcore.hardcore.DeathDAO;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class LinkingManager implements Listener {

    private DiscordManager discordManager;

    private Random random;

    private Map<UUID, String> linkedAccounts;
    private Map<String, String> registerIDs;

    public LinkingManager(DiscordManager discordManager) {
        this.random = new Random();
        this.discordManager = discordManager;
        SchedulerUtils.runDatabase((connection -> {
            LinkingDAO.createTable(connection);
        }));
        registerIDs = Maps.newHashMap();
        linkedAccounts = Maps.newHashMap();
        Bukkit.getPluginManager().registerEvents(this, discordManager.getPlugin());
    }

    public boolean linkAccount(UUID uuid, String id) {

        if (linkedAccounts.containsKey(uuid) || linkedAccounts.containsValue(id)) return false;

        linkedAccounts.put(uuid, id);
        SchedulerUtils.runDatabaseAsync((connection -> {
            LinkingDAO.linkAccount(connection, uuid, id);
        }));
        return true;
    }

    public boolean unlinkAccount(UUID uuid) {

        if (!linkedAccounts.containsKey(uuid)) return false;

        String id = linkedAccounts.get(uuid);

        linkedAccounts.remove(uuid);

        SchedulerUtils.runDatabaseAsync((connection -> {
            LinkingDAO.unlinkAccount(connection, uuid, id);
        }));

        return true;
    }

    public boolean unlinkAccount(String id) {

        if (!linkedAccounts.containsValue(id)) return false;

        UUID uuid = getUuidFRomID(id);

        linkedAccounts.remove(uuid);

        SchedulerUtils.runDatabaseAsync((connection -> {
            LinkingDAO.unlinkAccount(connection, uuid, id);
        }));

        return true;
    }

    public boolean isLinked(UUID uuid) {
        return linkedAccounts.containsKey(uuid);
    }

    public boolean isLinked(String id) {
        return (getUuidFRomID(id) != null);
    }

    public String newLinkAttempt(String id) {
        if (getUuidFRomID(id) != null) return null;
        registerIDs.remove(id);
        String key = String.valueOf(random.nextInt(10000));
        registerIDs.put(id, key);
        return key;
    }

    public String getLinkedDiscordID(UUID uuid) {
        return linkedAccounts.get(uuid);
    }

    public UUID getLinkedDiscordID(String id) {
        return getUuidFRomID(id);
    }


    public boolean acceptLink(String key, UUID uuid) {
        if (linkedAccounts.containsKey(uuid)) return false;
        String id = getIDFromKey(key);
        registerIDs.remove(id);
        linkedAccounts.put(uuid, id);
        SchedulerUtils.runDatabaseAsync((connection -> {
            LinkingDAO.linkAccount(connection, uuid, id);
        }));
        return true;
    }


    private UUID getUuidFRomID(String id) {
        for (Map.Entry<UUID, String> entry : linkedAccounts.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getIDFromKey(String key) {
        for (Map.Entry<String, String> entry : registerIDs.entrySet()) {
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public DiscordManager getDiscordManager() {
        return discordManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            String discord = LinkingDAO.getLinkedAccounts(connection, event.getUniqueId());
            if (discord == null) return;
            linkedAccounts.put(event.getUniqueId(), discord);
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        linkedAccounts.remove(event.getPlayer().getUniqueId());
    }
}
