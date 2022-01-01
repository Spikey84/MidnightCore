package me.spikey.midnightcore.cosmetics.tags;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorDAO;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TagsManager implements Listener {
    private Map<UUID, Integer> playerTags;
    private List<Tag> tags;
    private Plugin plugin;

    public TagsManager(Plugin plugin) {
        this.plugin = plugin;
        this.playerTags = Maps.newHashMap();
        this.tags = Lists.newArrayList();
        SchedulerUtils.runDatabase(TagsDAO::createTable);

        Bukkit.getPluginManager().registerEvents(this, plugin);


        tags.add(new Tag(" %white%[%lightred%Cool dude%white%]", (byte) 0));
        tags.add(new Tag(" %green%[%lightgreen%$%vault_eco_balance_formatted%%green%]", (byte) 1));
        tags.add(new Tag(" %white%[%lightblue%Joined: %player_first_join_date%%white%]", (byte) 2));

    }

    public void setTag(int id, UUID uuid) {
        playerTags.put(uuid, id);
        SchedulerUtils.runDatabaseAsync((connection -> {
            TagsDAO.add(connection, uuid, id);
        }));
    }

    public void removeTag(UUID uuid) {
        playerTags.remove(uuid);
        SchedulerUtils.runDatabaseAsync((connection -> {
            ChatColorDAO.remove(connection, uuid);
        }));
    }

    public int getTagID(UUID uuid) {
        return playerTags.get(uuid);
    }

    public String getTagString(Player player) {
        byte id = (byte) getTagID(player.getUniqueId());
        for (Tag tag : tags) {
            if (tag.getId() == id) return tag.getFormattedTag(player);
        }
        return "";
    }

    public List<Tag> getTags() {
        return tags;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            playerTags.put(event.getUniqueId(), TagsDAO.get(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        playerTags.remove(event.getPlayer().getUniqueId());
    }


}
