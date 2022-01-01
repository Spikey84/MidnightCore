package me.spikey.midnightcore.staffChat;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StaffChatDiscordListener extends ListenerAdapter {
    private StaffChatManager staffChatManager;

    public StaffChatDiscordListener(StaffChatManager staffChatManager) {
        this.staffChatManager = staffChatManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {


        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) return;

        if (!event.getMessage().getTextChannel().equals(staffChatManager.getDiscordManager().getJda().getGuildById(staffChatManager.getDiscordManager().getServerID()).getTextChannelById(staffChatManager.getDiscordManager().getStaffChannelID()))) return;

        staffChatManager.sendStaffChatMessage(event.getMember().getEffectiveName(), event.getMessage().getContentRaw(), true);
    }
}
