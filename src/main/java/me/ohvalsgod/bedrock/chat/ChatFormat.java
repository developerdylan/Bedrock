package me.ohvalsgod.bedrock.chat;

import org.bukkit.entity.Player;

public interface ChatFormat {

    String format(Player sender, Player receiver, String message);

}
