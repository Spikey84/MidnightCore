package me.spikey.midnightcore.staffChat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatMinecraftMessage implements Listener {
    private StaffChatManager staffChatManager;

    public StaffChatMinecraftMessage(StaffChatManager staffChatManager) {
        this.staffChatManager = staffChatManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() == null) return;

        if (!staffChatManager.getPlayerStaffChat(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
        staffChatManager.sendStaffChatMessage(event.getPlayer().getName(), event.getMessage(), false);
    }
}
