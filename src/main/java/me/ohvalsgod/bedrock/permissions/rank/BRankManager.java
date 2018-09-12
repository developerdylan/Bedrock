package me.ohvalsgod.bedrock.permissions.rank;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import me.ohvalsgod.bedrock.BServer;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.Language;
import me.ohvalsgod.bedrock.player.BPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BRankManager {

    private Bedrock bedrock;
    @Getter private Set<BRank> ranks;
    @Getter private boolean loaded;

    public BRankManager(Bedrock bedrock) {
        this.bedrock = bedrock;
        ranks = new HashSet<>();
    }

    /**
     * Deletes the specified rank and weather it
     * should be updated through the database.
     *
     * @param rank the specified rank
     * @param update to update or not
     */
    public void delete(BRank rank, boolean update) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BPlayer bplayer = Bedrock.getInstance().getPlayerManager().getByPlayer(player);

            bplayer.getGrantBySource(rank.getName()).setActive(false);
            bplayer.save();
            bplayer.recalculate();

            if (bplayer.getActiveRank().equals(rank) || (bplayer.getGlobalRank() != null && bplayer.getGlobalRank().equals(rank))) {
                player.sendMessage(Language.getInstance().rankFormat(Language.getInstance().getCursor().getString("permissions.rank.reset"), bplayer.getActiveRank()));
            }
        }
    }

    /**
     * Loads all of the ranks from the database
     */
    public void load() {
        if (!loaded) {
            AtomicInteger amount = new AtomicInteger();
            try (MongoCursor<Document> cursor = bedrock.getMongo().getRanks().find().iterator()) {
                cursor.forEachRemaining(document -> {
                    amount.getAndIncrement();

                    String name = document.getString("name");

                    BRank rank = new BRank(name);
                    BRankData data = new BRankData();

                    boolean global = document.getBoolean("global");
                    double weight = document.getDouble("weight");
                    List<String> permissions = new ArrayList<>();
                    List<String> inherits = new ArrayList<>();
                    String prefix = document.getString("prefix");
                    String suffix = document.getString("suffix");
                    ChatColor color = ChatColor.valueOf(document.getString("color"));


                    if (document.containsKey("permissions")) {
                        for (JsonElement element : new JsonParser().parse(document.getString("permissions")).getAsJsonArray()) {
                            permissions.add(element.getAsString());
                        }
                    }

                    if (document.containsKey("inherits")) {
                        for (JsonElement element : new JsonParser().parse(document.getString("inherits")).getAsJsonArray()) {
                            permissions.add(element.getAsString());
                        }
                    }

                    data.setGlobal(global);
                    data.setWeight(weight);
                    data.setPermissions(permissions);
                    data.setInherits(inherits);
                    data.setPrefix(prefix);
                    data.setSuffix(suffix);
                    data.setColor(color);

                    if (!global) {
                        data.setServer(BServer.valueOf(document.getString("server")));
                    }

                    rank.setData(data);
                    ranks.add(rank);
                });
            }
            bedrock.getLogger().info("Bedrock has loaded " + amount.get() + " ranks from the database..");
            loaded = true;
        } else {
            bedrock.getLogger().severe("Bedrock attempting to load when ranks have already been loaded!");
        }
    }

    /**
     * This either grabs the default rank from
     * the database (searching by lowest weight),
     * or creates a new one.
     *
     * @return default rank
     */
    public BRank getDefaultRank() {
        BRank dRank = new HashSet<>(getRanks()).stream().max(Comparator.comparingDouble(BRank::getWeight)).get();
        return dRank != null ? dRank:new BRank("Default");
    }

    /**
     * Get a rank by its name
     *
     * @param name the name of the rank
     * @return the rank object
     */
    public BRank getByName(String name) {
        for (BRank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

}
