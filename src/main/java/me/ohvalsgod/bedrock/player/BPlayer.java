package me.ohvalsgod.bedrock.player;

import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.cooldown.Cooldown;
import me.ohvalsgod.bedrock.mongo.BedrockMongo;
import me.ohvalsgod.bedrock.permissions.grant.Grant;
import me.ohvalsgod.bedrock.permissions.grant.GrantType;
import me.ohvalsgod.bedrock.permissions.rank.BRank;
import me.ohvalsgod.bedrock.permissions.rank.BRankManager;
import me.ohvalsgod.bedrock.util.MongoUtil;
import org.bson.Document;

import java.util.*;

@Setter
@Getter
public class BPlayer {

    //  Necessities
    private final UUID uniqueId;
    private boolean loaded;
    private long loadedAt;

    //  Bedrock
    private UUID toReply;
    private Set<UUID> ignoredPlayers, friendsList;

    //  Settings //TODO: Change this later, to a transformable document
    private boolean seeGlobalChat, hearPingOnMessage, seePrivateMessages, seeGlobalAlerts;

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

    public BPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

}
