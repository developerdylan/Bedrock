package me.ohvalsgod.bedrock.chat.format;

import me.ohvalsgod.bedrock.chat.ChatFormat;
import org.bukkit.entity.Player;

public class DefaultChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        return "";
    }

}
