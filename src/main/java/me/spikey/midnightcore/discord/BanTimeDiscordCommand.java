package me.spikey.midnightcore.discord;

import me.spikey.midnightcore.hardcore.HardcoreManager;
import me.spikey.spikeycooldownapi.API;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BanTimeDiscordCommand extends ListenerAdapter {
    private DiscordManager discordManager;
    private API api;

    public BanTimeDiscordCommand(DiscordManager discordManager, API api) {

        this.discordManager = discordManager;
        this.api = api;

        discordManager.getJda().addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getGuild().getId().equals(discordManager.getServerID())) return;

        if (!event.getChannel().getId().equals(discordManager.getChatChannelID())) return;

        if (event.getAuthor().isBot()) return;

        if (!event.getMessage().getContentRaw().startsWith("!bantime")) return;

        String msg = event.getMessage().getContentRaw();

        if (msg.split(" ").length == 1) {
            event.getMessage().reply(new MessageBuilder("Please enter a user to lookup their death ban status.").build()).queue();
            return;
        }

        if (Bukkit.getOfflinePlayer(msg.split(" ")[1]) == null) {
            event.getMessage().reply(new MessageBuilder("This user has never logged in.").build()).queue();
            return;
        }

        UUID uuid =  Bukkit.getOfflinePlayer(msg.split(" ")[1]).getUniqueId();

        if (!api.isOnCooldown(uuid, 0)) {
            event.getMessage().reply(new MessageBuilder("This user is not currently death banned.").build()).queue();
            return;
        }

        event.getMessage().reply(new MessageBuilder("%s is currently death banned for another %s.".formatted(msg.split(" ")[1], api.getRemainingFormattedLong(uuid, 0))).build()).queue();
    }
}
