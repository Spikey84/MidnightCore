package me.spikey.midnightcore.discord.linking;

import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.spikeycooldownapi.API;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UnLinkDiscordCommand extends ListenerAdapter {
    private DiscordManager discordManager;
    private API api;
    private LinkingManager linkingManager;

    public UnLinkDiscordCommand(DiscordManager discordManager, API api, LinkingManager linkingManager) {

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

        if (!event.getMessage().getContentRaw().startsWith("!mcunlink")) return;

        String msg = event.getMessage().getContentRaw();

        if (!linkingManager.isLinked(event.getMessage().getAuthor().getId())) {
            event.getMessage().reply("You do not have a mc account to linked.").queue();
            return;
        }

        if (linkingManager.unlinkAccount(event.getMessage().getAuthor().getId())) {
            event.getMessage().reply("Your account has been unlinked!").queue();
        } else {
            event.getMessage().reply("You do not have a mc account to linked.").queue();

        }

    }
}
