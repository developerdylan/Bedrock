package me.ohvalsgod.bedrock;

import lombok.Getter;
import me.ohvalsgod.bedrock.config.ConfigCursor;
import me.ohvalsgod.bedrock.permissions.rank.BRank;

import java.util.*;

public class Language {

    private static Language language;
    private Bedrock bedrock;
    @Getter private ConfigCursor cursor;
    private Map<String, String> singleStrings;

    public Language(Bedrock bedrock) {
        this.bedrock = bedrock;
        language = this;
        singleStrings = new HashMap<>();

        init();
    }

    public String getString(String key) {
        return singleStrings.get(key);
    }

    public List<String> getStringList(String key) {
        return null;
    }


    public void init() {
        cursor = new ConfigCursor(bedrock.getLangConfig(), "");

        //  PERMISSIONS
        cursor.setPath("PERMISSIONS.RANK");

        singleStrings.put("RANK_RESET", cursor.getString("RESET"));

        //  PROFILE
        cursor.setPath("PROFILE.ERROR");

        singleStrings.put("PROFILE_SAVED", cursor.getString("SAVED"));
        singleStrings.put("PROFILE_LOADED", cursor.getString("LOADED"));
        singleStrings.put("PROFILE_COULDNT_LOAD", cursor.getString("COULD_NOT_LOAD"));
    }

    public String rankFormat(String string, BRank rank) {
        return string.replace("{RANK}", rank.getName());
    }

    public static Language getInstance() {
        return language;
    }

}
