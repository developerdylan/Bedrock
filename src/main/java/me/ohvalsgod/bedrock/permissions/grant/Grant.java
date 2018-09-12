package me.ohvalsgod.bedrock.permissions.grant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.ohvalsgod.bedrock.Bedrock;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Grant {

    private UUID uuid, issuer;
    private String source;
    private GrantType type;
    private long timeAdded;
    private long duration;
    private String reason;
    private boolean active;

    public Grant(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() <  timeAdded + duration;
    }

    public static Grant getDefaultGrant() {
        return new Grant(UUID.randomUUID(),
                UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670"),
                Bedrock.getInstance().getRankManager().getDefaultRank().getName(),
                GrantType.RANK,
                System.currentTimeMillis(),
                -1,
                "Default rank grant",
                true);
    }

}
