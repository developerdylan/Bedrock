package me.ohvalsgod.bedrock.jedis;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.ohvalsgod.bedrock.Bedrock;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor
public class JedisPublisher {

	private final JedisSettings jedisSettings;

	public void write(String channel, JsonObject payload) {
		Validate.notNull(Bedrock.getInstance().getJedis().getPool());

		Bedrock.getInstance().getJedis().runCommand(redis -> {
			if (jedisSettings.hasPassword()) {
				redis.auth(jedisSettings.getPassword());
			}

			redis.publish(channel, payload.toString());

			return null;
		});
	}

}
