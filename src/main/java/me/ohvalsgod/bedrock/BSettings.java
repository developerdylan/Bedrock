package me.ohvalsgod.bedrock;

import lombok.Getter;
import me.ohvalsgod.bedrock.config.ConfigCursor;
import me.ohvalsgod.bedrock.util.CC;

@Getter
public class BSettings {

    protected Bedrock bedrock;

    private String serverId;
    private BServer server;
    private String serverMotd;

    public BSettings(Bedrock bedrock) {
        this.bedrock = bedrock;
    }

    public void load() {
        ConfigCursor cursor = new ConfigCursor(bedrock.getMainConfig(), "server");
        serverId = cursor.getString("id");
        server = BServer.valueOf(cursor.getString("HUB"));
        serverMotd = CC.translate(cursor.getString("motd"));
    }

}
