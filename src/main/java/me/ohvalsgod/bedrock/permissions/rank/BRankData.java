package me.ohvalsgod.bedrock.permissions.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bedrock.BServer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BRankData {

    private boolean global;
    private double weight;
    private List<String> permissions;
    private List<String> inherits;
    private String prefix, suffix;
    private ChatColor color;
    private BServer server;

    BRankData() {
        prefix = "";
        suffix = "";
        color = ChatColor.WHITE;
        global = true;
        weight = -1;
        permissions = new ArrayList<>();
        inherits = new ArrayList<>();
    }

}
