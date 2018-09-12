package me.ohvalsgod.bedrock.profiling.example;

import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.profiling.provider.AbstractBPlayerProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BPlayerListener<T extends BPlayer> implements Listener {

    private AbstractBPlayerProvider<T> provider;

    public BPlayerListener(AbstractBPlayerProvider<T> provider) {
        this.provider = provider;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //saving shit
        provider.save(provider.getByPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //loading shit
        provider.load(event.getPlayer().getUniqueId());
    }

}
