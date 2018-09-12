package me.ohvalsgod.bedrock.player;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.cooldown.Cooldown;
import me.ohvalsgod.bedrock.permissions.grant.Grant;
import me.ohvalsgod.bedrock.permissions.grant.GrantType;
import me.ohvalsgod.bedrock.permissions.rank.BRank;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class BPlayer extends PlayerInfo {

    //  Necessities
    private boolean loaded;
    private long loadedAt;
    private long lastSave;

    //  Bedrock
    private UUID toReply;
    private Set<UUID> ignoredPlayers, friendsList;

    //  Settings //TODO: Change this later, to a transformable document
    private boolean seeGlobalChat, hearPingOnPrivateMessage, seePrivateMessages, seeGlobalAlerts;

    //  Cooldowns
    private Cooldown chatCooldown, loginCooldown, requestCooldown, reportCooldown;

    //  Logging info
    private long firstLogin;
    private long lastLogout;
    private String currentAddress, lastAddress;
    private List<String> addresses;
    private Set<UUID> alternateAccounts;

    //  Permissions and Groups
    private List<Grant> grants;
    private BRank activeRank;

    public BPlayer(UUID uniqueId) {
        super(uniqueId, null);
    }

    @Deprecated
    public Grant getGrantBySource(String source) {
        for (Grant grant : grants) {
            if (grant.getSource().equalsIgnoreCase(source)) {
                return grant;
            }
        }
        return null;
    }

    public void recalculate() {
        BRank globalRank = null;
        BRank serverRank = null;
        BRank lastRank;

        for (Grant grant : grants) {
            if (grant.getType() == GrantType.RANK) {
                BRank rank = Bedrock.getInstance().getRankManager().getByName(grant.getSource());

                if (rank != null) {
                    lastRank = rank;

                    if (rank.isGlobal()) {
                        if (globalRank == null) {
                            globalRank = rank;
                        } else {
                            if (lastRank != null) {
                                if (rank.getWeight() >= lastRank.getWeight()) {
                                    globalRank = rank;
                                }
                            }
                        }
                    } else {
                        if (serverRank == null) {
                            serverRank = rank;
                        } else {
                            if (lastRank != null) {
                                if (rank.getWeight() >= lastRank.getWeight()) {
                                    serverRank = rank;
                                }
                            }
                        }
                    }
                }
            }
        }


        if (globalRank == null && serverRank == null) {
            //TODO: get default rank
        } else if (globalRank != null && serverRank != null) {
            this.activeRank = globalRank.getWeight() >= serverRank.getWeight() ? globalRank : serverRank;
        } else {
            if (globalRank == null) {
                this.activeRank = serverRank;
            } else {
                this.activeRank = globalRank;
            }
        }
    }

}
