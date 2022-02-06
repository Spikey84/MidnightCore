package me.spikey.midnightcore.homes;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.DatabaseManager;
import me.spikey.midnightcore.staffChat.StaffChatDAO;
import me.spikey.midnightcore.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager implements Listener {
    private ArrayList<Home> homes;

    public HomeManager(Plugin plugin) {

        SchedulerUtils.runDatabase((connection -> {
            String query = """
            CREATE TABLE IF NOT EXISTS namedhomes (\
            id INTEGER PRIMARY KEY AUTOINCREMENT, \
            uuid VARCHAR NOT NULL,\
            x INT NOT NULL,\
            y INT NOT NULL,\
            z INT NOT NULL,\
            world_name TEXT NOT NULL,\
            name TEXT NOT NULL\
            );
            """;

            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }));

        this.homes = Lists.newArrayList();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addHome(Home home) {
        homes.add(home);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                HomesDAO.addHome(connection, home);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delHome(Home home) {
        homes.remove(home);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                HomesDAO.removeHome(connection, home.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Home getHome(int id) {
        return homes.get(id);
    }

    public boolean hasHome(UUID uuid) {
        for (Home home : homes) {
            if (home.getUuid().equals(uuid)) return true;
        }
        return false;
    }

    public List<Home> getHomes(UUID uuid) {
        List<Home> list = Lists.newArrayList();
        for (Home home : homes) {
            if (home.getUuid().equals(uuid)) list.add(home);
        }
        return list;
    }

    public int getMaxHomeAmount(Player player) {
        List<Integer> homeAmount = Lists.newArrayList();

        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getPermission().startsWith("core.home")) {
                if (attachmentInfo.getPermission().equals("core.home")) continue;
                homeAmount.add(Integer.parseInt(attachmentInfo.getPermission().substring(attachmentInfo.getPermission().lastIndexOf(".") + 1)));
            }
        }

        int finalHomeAmount = 0;
        for (int i : homeAmount) {
            if (i > finalHomeAmount) finalHomeAmount = i;
        }
        return finalHomeAmount;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        SchedulerUtils.runDatabase((connection -> {
            homes.addAll(HomesDAO.getHomesByUUID(connection, event.getUniqueId()));
        }));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        for (Home home : homes.stream().toList()) {
            if (home.getUuid().equals(event.getPlayer().getUniqueId())) homes.remove(home);
        }
    }
}
