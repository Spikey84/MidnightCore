package me.spikey.midnightcore.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class ServerMoveEvent implements Listener {
    private DiscordManager discordManager;

    public ServerMoveEvent(DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        if(discordManager == null) return;

        MessageEmbed embed = new EmbedBuilder()
                .setDescription(String.format("%s has left the server.", event.getPlayer().getName()))
                .setColor(Color.pink)
                .build();
        Message message = new MessageBuilder().setEmbeds(embed)
                .build();

        discordManager.getJda().getGuildById(discordManager.getServerID()).getTextChannelById(discordManager.getChatChannelID()).sendMessage(message).queue();
        updateServerDescription();
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if(discordManager == null) return;

        MessageEmbed embed = new EmbedBuilder()
                .setDescription(String.format("%s has joined the server.", event.getPlayer().getName()))
                .setColor(Color.pink)
                .build();
        Message message = new MessageBuilder().setEmbeds(embed)
                .build();

        discordManager.getJda().getGuildById(discordManager.getServerID()).getTextChannelById(discordManager.getChatChannelID()).sendMessage(message).queue();
        updateServerDescription();
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        if(discordManager == null) return;

        MessageEmbed embed = new EmbedBuilder()
                .setDescription(event.getDeathMessage())
                .setColor(Color.pink)
                .build();
        Message message = new MessageBuilder().setEmbeds(embed)
                .build();

        discordManager.getJda().getGuildById(discordManager.getServerID()).getTextChannelById(discordManager.getChatChannelID()).sendMessage(message).queue();
    }

    public void updateServerDescription() {
        String playersOnline = Integer.toString(Bukkit.getOnlinePlayers().size());
        discordManager.getJda().getGuildById(discordManager.getServerID()).getGuildChannelById(discordManager.getChatChannelID()).getManager().setTopic("Players Online: %s".formatted(playersOnline)).queue();
    }
}
