package me.ohvalsgod.bedrock.jedis;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.jedis.handler.Payload;
import me.ohvalsgod.bedrock.jedis.handler.SubscriptionHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class BedrockJedis {

    private JedisSettings settings;
    private JedisPool pool;
    private JedisPublisher publisher;
    private JedisSubscriber subscriber;

    public BedrockJedis(JedisSettings settings) {
        this.settings = settings;
        this.pool = new JedisPool(this.settings.getAddress(), this.settings.getPort());

        try (Jedis jedis = this.pool.getResource()) {
            if (this.settings.hasPassword()) {
                jedis.auth(this.settings.getPassword());
            }

            this.publisher = new JedisPublisher(this.settings);
            this.subscriber = new JedisSubscriber("bedrock", this.settings, new SubscriptionHandler());
        }
    }

    public boolean isActive() {
        return this.pool != null && !this.pool.isClosed();
    }

    public void write(Payload payload, JsonObject data) {
        JsonObject object = new JsonObject();

        object.addProperty("payload", payload.name());
        object.add("data", data == null ? new JsonObject() : data);

        this.publisher.write("bedrock", object);
    }

    public <T> T runCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = this.pool.getResource();
        T result = null;

        try {
            result = redisCommand.execute(jedis);
        } catch (Exception e) {
            e.printStackTrace();

            if (jedis != null) {
                this.pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                this.pool.returnResource(jedis);
            }
        }

        return result;
    }

    public static BedrockJedis getInstance() {
        return Bedrock.getInstance().getJedis();
    }

}
