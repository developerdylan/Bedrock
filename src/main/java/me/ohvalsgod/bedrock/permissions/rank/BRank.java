package me.ohvalsgod.bedrock.permissions.rank;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bedrock.BServer;
import me.ohvalsgod.bedrock.Bedrock;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Setter
public class BRank {

    @Getter private String name;
    private BRankData data;

    public BRank(String name) {
        this.name = name;
        data = new BRankData();
    }

    public BRank(String name, BRankData data) {
        this.name = name;
        this.data = data;
    }

    /**
     * The rank's global value, if it is a global rank
     * the #getServer() method will return null.
     *
     * @return if the rank is global
     */
    public boolean isGlobal() {
        return data.isGlobal();
    }

    /**
     * The rank's weight, used as their rank on a ladder,
     * if one is higher, it has more priority/power over others.
     *
     * @return the rank's weight
     */
    public double getWeight() {
        return data.getWeight();
    }

    /**
     * The rank's permissions, this is only the direct permissions
     * that the rank has access to, so this doesn't count for all of
     * the parent's permissions.
     *
     * @return the rank's permissions
     */
    @Deprecated
    public List<String> getPermissions() {
        return data.getPermissions();
    }

    /**
     * The rank's parents, this rank will inherit all of the parent
     * permissions.
     *
     * @return a string list of the parent rank names
     */
    public List<String> getInherits() {
        return data.getInherits();
    }

    /**
     * The rank's prefix, this comes before the rank's name.
     *
     * @return the rank's prefix
     */
    public String getPrefix() {
        return data.getPrefix();
    }

    /**
     * The rank's suffix, this comes after the rank's name.
     *
     * @return the rank's suffix
     */
    public String getSuffix() {
        return data.getSuffix();
    }

    /**
     * The rank's color that is used when displaying the name, and
     * any player names that have the rank.
     *
     * @return the rank's color
     */
    public ChatColor getColor() {
        return data.getColor();
    }

    /**
     * If this rank is not global, this will return the specific
     * server it belongs to.
     *
     * @return the rank's server
     */
    public BServer getServer() {
        return data.getServer();
    }

    /**
     * This ia  list of all effective permissions that the rank has,
     * so it will return all the permissions it directly contains,
     * and all of its parents permissions'.
     *
     * @return effective permissions
     */
    public List<String> getEffectivePermissions() {
        List<String> toReturn = new ArrayList<>();
        toReturn.addAll(data.getPermissions());
        data.getInherits().forEach(name -> toReturn.addAll(Bedrock.getInstance().getRankManager().getByName(name).getEffectivePermissions()));
        return toReturn;
    }

}
