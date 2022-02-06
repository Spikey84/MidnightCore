package me.spikey.midnightcore.discord;

import me.spikey.midnightcore.utils.SchedulerUtils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collections;

public class DiscordChat extends ListenerAdapter implements Listener {
    private DiscordManager discordManager;

    public DiscordChat(DiscordManager discordManager) {

        this.discordManager = discordManager;
        discordManager.getJda().addEventListener(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void mcChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("[event cancelled by LiteBans]")) return;
        if (event.isCancelled()) return;
        TextChannel channel = discordManager.getJda().getGuildById(discordManager.getServerID()).getTextChannelById(discordManager.getChatChannelID());


        String prefix = discordManager.getPlugin().getChat().getPlayerPrefix(event.getPlayer())
                .replaceAll("([&][#][123456789abcdefghijkmnopqrstuvwxyz][123456789abcdefghijkmnopqrstuvwxyz][123456789abcdefghijkmnopqrstuvwxyz][123456789abcdefghijkmnopqrstuvwxyz][123456789abcdefghijkmnopqrstuvwxyz][123456789abcdefghijkmnopqrstuvwxyz])", "")
                .replaceAll("([&][a-z0-9])", "");
        channel.sendMessage(new MessageBuilder(prefix + event.getPlayer().getName() + ": " + event.getMessage().replaceAll("@", "")).setAllowedMentions(Collections.singleton(Message.MentionType.USER)).build()).queue();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getGuild().getId().equals(discordManager.getServerID())) return;

        if (!event.getChannel().getId().equals(discordManager.getChatChannelID())) return;

        if (event.getAuthor().isBot()) return;


        SchedulerUtils.runSync(() -> {
            Bukkit.broadcastMessage(ChatColor.WHITE + event.getAuthor().getName() + ": " + discordBlue + event.getMessage().getContentDisplay());
        });
    }


    private static ChatColor discordBlue = ChatColor.of("#5865F2");
}

