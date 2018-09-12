package me.ohvalsgod.bedrock.listener.listeners;

import me.ohvalsgod.bedrock.Bedrock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListener {

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        Bedrock.getInstance().getUuidCache().update(event.getName(), event.getUniqueId());
    }

}
