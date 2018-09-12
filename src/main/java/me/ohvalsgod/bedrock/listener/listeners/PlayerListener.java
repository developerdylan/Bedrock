package me.ohvalsgod.bedrock.listener.listeners;

import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.player.BPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener {

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        Bedrock.getInstance().getProfileProvider().load(event.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BPlayer profile = Bedrock.getInstance().getProfileProvider().getProfileByPlayer(event.getPlayer());

        if ((System.currentTimeMillis()/1000 - profile.getLastSave()/1000) < 60) {
            Bedrock.getInstance().getProfileProvider().save(profile);
        }

    }

}
