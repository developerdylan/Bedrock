package me.ohvalsgod.bedrock.jedis.handler;

import com.google.gson.JsonObject;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.jedis.JedisSubscriptionHandler;

public class SubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject object) {
        Payload payload;

        try {
            payload = Payload.valueOf(object.get("payload").getAsString());
        } catch (IllegalArgumentException e) {
            Bedrock.getInstance().getLogger().warning("Payload type '" + object.get("payload").getAsString() + "' could not be parsed.");
            return;
        }

        JsonObject data = object.get("data").getAsJsonObject();

        switch (payload) {

        }
    }

}
