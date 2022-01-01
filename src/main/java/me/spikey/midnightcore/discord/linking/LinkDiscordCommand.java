package me.spikey.midnightcore.discord.linking;

import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.spikeycooldownapi.API;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LinkDiscordCommand extends ListenerAdapter {

    private DiscordManager discordManager;
    private API api;
    private LinkingManager linkingManager;

    public LinkDiscordCommand(DiscordManager discordManager, API api, LinkingManager linkingManager) {

        this.discordManager = discordManager;
        this.api = api;
        this.linkingManager = linkingManager;

        discordManager.getJda().addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannelType().equals(ChannelType.PRIVATE)) return;
        if (!event.getGuild().getId().equals(discordManager.getServerID())) return;

        if (event.getAuthor().isBot()) return;

        if (!event.getMessage().getContentRaw().startsWith("!mclink")) return;

        String msg = event.getMessage().getContentRaw();

        String key = linkingManager.newLinkAttempt(event.getAuthor().getId());
        if (key == null) {
            event.getMessage().reply("You already have a mc account linked to your discord.").queue();
            return;
        }

        event.getAuthor().openPrivateChannel().queue((privateChannel -> {
            privateChannel.sendMessage("Run the command ```/link %s``` in minecraft to link your accounts.".formatted(key)).queue();
        }));

    }
}
