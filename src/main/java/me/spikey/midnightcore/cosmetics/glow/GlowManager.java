package me.spikey.midnightcore.cosmetics.glow;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.MrGraycat.eGlow.API.EGlowAPI;
import me.MrGraycat.eGlow.API.Enum.EGlowColor;
import me.MrGraycat.eGlow.API.Enum.EGlowEffect;
import me.MrGraycat.eGlow.Manager.Interface.IEGlowEffect;
import me.MrGraycat.eGlow.Manager.Interface.IEGlowPlayer;
import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorDAO;
import me.spikey.midnightcore.cosmetics.tags.Tag;
import me.spikey.midnightcore.cosmetics.tags.TagsDAO;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GlowManager implements Listener {
    private Map<UUID, Integer> playerGlow;
    private List<Glow> glowTypes;
    private Plugin plugin;
    private EGlowAPI eGlowAPI;

    public GlowManager(Plugin plugin) {
        this.plugin = plugin;
        this.playerGlow = Maps.newHashMap();
        this.glowTypes = Lists.newArrayList();
        this.eGlowAPI = new EGlowAPI();
        SchedulerUtils.runDatabase(GlowDAO::createTable);

        Bukkit.getPluginManager().registerEvents(this, plugin);

        glowTypes.add(new Glow(EGlowColor.RED, Material.RED_STAINED_GLASS_PANE, C.lightRed + "Red", (byte) 0));
        glowTypes.add(new Glow(EGlowColor.GOLD, Material.ORANGE_STAINED_GLASS_PANE, C.lightOrange + "Orange", (byte) 1));
        glowTypes.add(new Glow(EGlowColor.YELLOW, Material.YELLOW_STAINED_GLASS_PANE, C.lightYellow + "Yellow", (byte) 2));
        glowTypes.add(new Glow(EGlowColor.GREEN, Material.GREEN_STAINED_GLASS_PANE, C.lightGreen + "Green", (byte) 3));
        glowTypes.add(new Glow(EGlowColor.BLUE, Material.BLUE_STAINED_GLASS_PANE, C.lightBlue + "Blue", (byte) 4));
        glowTypes.add(new Glow(EGlowColor.PURPLE, Material.PURPLE_STAINED_GLASS_PANE, C.lightPurple + "Purple", (byte) 5));
        glowTypes.add(new Glow(EGlowColor.WHITE, Material.WHITE_STAINED_GLASS_PANE, C.white + "White", (byte) 6));
        glowTypes.add(new Glow(EGlowColor.GRAY, Material.GRAY_STAINED_GLASS_PANE, C.gray + "Gray", (byte) 7));
        glowTypes.add(new Glow(EGlowColor.BLACK, Material.BLACK_STAINED_GLASS_PANE, ChatColor.BLACK + "Black", (byte) 8));
        glowTypes.add(new Glow(EGlowColor.DARK_BLUE, Material.BLUE_STAINED_GLASS_PANE, C.blue + "Dark Blue", (byte) 10));
        glowTypes.add(new Glow(EGlowColor.DARK_GRAY, Material.GRAY_STAINED_GLASS_PANE, C.gray + "Dark Grey", (byte) 11));
        glowTypes.add(new Glow(EGlowColor.DARK_GREEN, Material.GREEN_STAINED_GLASS_PANE, C.green + "Dark Green", (byte) 12));
        glowTypes.add(new Glow(EGlowColor.DARK_RED, Material.RED_STAINED_GLASS_PANE, C.red + "Dark Red", (byte) 13));
        glowTypes.add(new Glow((EGlowColor) null, Material.CACTUS, C.red + "Remove Glow", (byte) 14));
        //glowTypes.add(new Glow(EGlowEffect.RAINBOW_FAST, Material.WATER_BUCKET, C.white + "Rainbow Glow", (byte) 14));
    }

    public void setGlow(int id, UUID uuid) {
        playerGlow.put(uuid, id);
        SchedulerUtils.runDatabaseAsync((connection -> {
            GlowDAO.add(connection, uuid, id);
        }));
    }

    public void removeGlow(UUID uuid) {
        playerGlow.remove(uuid);
        SchedulerUtils.runDatabaseAsync((connection -> {
            GlowDAO.remove(connection, uuid);
        }));
    }

    public int getGlowID(UUID uuid) {
        return playerGlow.get(uuid);
    }

    public List<Glow> getGlowTypes() {
        return glowTypes;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            playerGlow.put(event.getUniqueId(), GlowDAO.get(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if (event.getPlayer() == null) return;
        eGlowAPI.disableGlow((new IEGlowPlayer(event.getPlayer())));
        if (playerGlow.get(event.getPlayer().getUniqueId()) == null || playerGlow.get(event.getPlayer().getUniqueId()) < 1) return;

        if (glowTypes.get(playerGlow.get(event.getPlayer().getUniqueId())).eGlowEffect() != null) {
            eGlowAPI.enableGlow((new IEGlowPlayer(event.getPlayer())), glowTypes.get(getGlowID(event.getPlayer().getUniqueId())).eGlowEffect());
            return;
        }

        eGlowAPI.enableGlow((new IEGlowPlayer(event.getPlayer())), glowTypes.get(getGlowID(event.getPlayer().getUniqueId())).getColor());
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        playerGlow.remove(event.getPlayer().getUniqueId());
    }


}
