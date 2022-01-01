package me.spikey.midnightcore.discord.sub;

import me.spikey.midnightcore.Main;
import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.midnightcore.discord.linking.LinkingManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SubManager implements Listener {

    private LinkingManager linkingManager;
    private DiscordManager discordManager;

    public SubManager(Plugin plugin, LinkingManager linkingManager) {
        this.linkingManager = linkingManager;
        this.discordManager = linkingManager.getDiscordManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (!linkingManager.isLinked(event.getPlayer().getUniqueId())) {
            if (player.hasPermission("group.sub")) {
                net.luckperms.api.model.user.User lpUser = Main.getLuckPerms().getUserManager().getUser(player.getUniqueId());
                Main.getLuckPerms().getUserManager().getUser(player.getUniqueId()).data().remove(Node.builder("group.sub").build());
                Main.getLuckPerms().getUserManager().saveUser(lpUser);
            }
            return;
        }


        Guild guild = discordManager.getJda().getGuildById(discordManager.getServerID());

        User user = discordManager.getJda().getUserById(linkingManager.getLinkedDiscordID(event.getPlayer().getUniqueId()));

        Bukkit.getLogger().info(user.getName());

        List<Role> roles = guild.getMember(user).getRoles();
        Role subRole = guild.getRoleById(discordManager.getSubRoleID());

        if (!roles.contains(subRole)) {
            if (player.hasPermission("group.sub")) {
                net.luckperms.api.model.user.User lpUser = Main.getLuckPerms().getUserManager().getUser(player.getUniqueId());
                Main.getLuckPerms().getUserManager().getUser(player.getUniqueId()).data().remove(Node.builder("group.sub").build());
                Main.getLuckPerms().getUserManager().saveUser(lpUser);
            }
            Bukkit.getLogger().info("no sub role");
            return;
        }
//        if (player.hasPermission("group.sub")) {
//            Bukkit.getLogger().info("has sub group");
//            return;
//        }


        net.luckperms.api.model.user.User lpUser = Main.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        lpUser.data().add(Node.builder("group.sub").build());
        Main.getLuckPerms().getUserManager().saveUser(lpUser);
    }
}
