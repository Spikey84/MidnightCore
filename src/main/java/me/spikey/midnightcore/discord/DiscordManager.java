package me.spikey.midnightcore.discord;

import me.spikey.midnightcore.Main;
import me.spikey.midnightcore.discord.linking.LinkDiscordCommand;
import me.spikey.midnightcore.discord.linking.LinkingManager;
import me.spikey.midnightcore.discord.linking.UnLinkDiscordCommand;
import me.spikey.midnightcore.discord.sub.SubManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class DiscordManager {
    private JDA jda;
    private Main plugin;
    private String serverID;
    private String chatChannelID;
    private String staffChannelID;
    private String subRoleID;

    private DiscordChat discordChat;

    private LinkingManager linkingManager;
    private SubManager subManager;


    private BanTimeDiscordCommand banTimeDiscordCommand;
    private UnLinkDiscordCommand unLinkDiscordCommand;
    private LinkDiscordCommand linkDiscordCommand;

    public DiscordManager(Main plugin) {
        this.plugin = plugin;

        this.serverID = plugin.getConfig().getString("discordServerID");
        this.chatChannelID = plugin.getConfig().getString("discordChatChannelID");
        this.staffChannelID = plugin.getConfig().getString("discordStaffChannelID");
        this.subRoleID = plugin.getConfig().getString("subroleID");

        try {
            jda = JDABuilder.createLight(plugin.getConfig().getString("botToken"))
                    .setActivity(Activity.playing("MidnightSMP"))
                    .setEnabledIntents(Arrays.stream(GatewayIntent.values()).toList())
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        this.linkingManager = new LinkingManager(this);
        this.subManager = new SubManager(plugin, linkingManager);

        this.discordChat = new DiscordChat(this);

        this.banTimeDiscordCommand = new BanTimeDiscordCommand(this, plugin.getApi());
        this.linkDiscordCommand = new LinkDiscordCommand(this, plugin.getApi(), linkingManager);
        this.unLinkDiscordCommand = new UnLinkDiscordCommand(this, plugin.getApi(), linkingManager);

        Bukkit.getPluginManager().registerEvents(discordChat, plugin);
        Bukkit.getPluginManager().registerEvents(new ServerMoveEvent(this), plugin);
    }

    public String getServerID() {
        return serverID;
    }

    public String getChatChannelID() {
        return chatChannelID;
    }

    public JDA getJda() {
        return jda;
    }

    public Main getPlugin() {
        return plugin;
    }

    public String getStaffChannelID() {
        return staffChannelID;
    }

    public LinkingManager getLinkingManager() {
        return linkingManager;
    }

    public String getSubRoleID() {
        return subRoleID;
    }
}
