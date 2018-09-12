package me.ohvalsgod.bedrock.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerInfo {

    private UUID uniqueId;
    @Setter private String name;

    public PlayerInfo(Player player) {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
    }

    public PlayerInfo(UUID uuid) {
        this.uniqueId = uuid;
    }

    public PlayerInfo(UUID uuid, String name) {
        this.uniqueId = uuid;
        this.name = name;
    }

    /**
     * Converts the PlayerInfo object to Player object
     *
     * @return the player object
     */
    public Player toPlayer() {
        return Bukkit.getPlayer(this.getUniqueId());
    }

    /**
     * @return the player's display name
     */
    public String getDisplayName() {
        final Player player = this.toPlayer();

        return player == null ? this.getName() : player.getDisplayName();
    }

}
