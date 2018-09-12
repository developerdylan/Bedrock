package me.ohvalsgod.bedrock.chat;

import lombok.Getter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.config.ConfigCursor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ChatManager {

    private Pattern urlRegex;
    private Pattern ipRegex;
    private List<String> linkWhitelist;

    private int chatDelay = 3;
    private boolean publicChatMuted = false;

    private List<String> filteredWords = Arrays.asList("nigger", "faggot", "queer", "paki", "slut");
    private List<String> filteredPhrases;

    public ChatManager(Bedrock bedrock) {
        urlRegex = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
        ipRegex = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

        final ConfigCursor cursor = new ConfigCursor(bedrock.getMainConfig(), "chat");
        linkWhitelist = cursor.getStringList("link-whitelist");
        filteredPhrases = cursor.getStringList("phrase-filter");
        filteredWords = cursor.getStringList("word-filter");
    }


    public boolean shouldFilter(String message) {
        String msg = message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");

        for (String word : words) {
            for (String filteredWord : filteredWords) {
                if (word.contains(filteredWord)) {
                    return true;
                }
            }
        }

        for (String word : message.replace("(dot)", ".").replace("[dot]", ".").trim().split(" ")) {
            boolean continueIt = false;

            for (String phrase : linkWhitelist) {
                if (word.toLowerCase().contains(phrase)) {
                    continueIt = true;
                    break;
                }
            }

            if (continueIt) {
                continue;
            }

            Matcher matcher = ipRegex.matcher(word);

            if (matcher.matches()) {
                return true;
            }

            matcher = urlRegex.matcher(word);

            if (matcher.matches()) {
                return true;
            }
        }

        Optional<String> optional = filteredPhrases.stream().filter(msg::contains).findFirst();
        return optional.isPresent();
    }

}
