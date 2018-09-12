package me.ohvalsgod.bedrock;

import lombok.Getter;
import me.ohvalsgod.bedrock.config.ConfigCursor;
import me.ohvalsgod.bedrock.permissions.rank.BRank;

public class Language {

    private static Language language;
    private Bedrock bedrock;
    @Getter private ConfigCursor cursor;

    public Language(Bedrock bedrock) {
        language = this;
        this.bedrock = bedrock;
    }

    public void init() {
        cursor = new ConfigCursor(bedrock.getLangConfig(), "");
    }

    public String rankFormat(String string, BRank rank) {
        return string.replace("{RANK}", rank.getName());
    }

    public static Language getInstance() {
        return language;
    }

}
