package me.ohvalsgod.bedrock;

import lombok.Getter;
import me.ohvalsgod.bedrock.cache.UUIDCache;
import me.ohvalsgod.bedrock.chat.ChatManager;
import me.ohvalsgod.bedrock.command.CommandHandler;
import me.ohvalsgod.bedrock.config.ConfigCursor;
import me.ohvalsgod.bedrock.config.FileConfig;
import me.ohvalsgod.bedrock.jedis.BedrockJedis;
import me.ohvalsgod.bedrock.jedis.JedisSettings;
import me.ohvalsgod.bedrock.listener.ListenerHandler;
import me.ohvalsgod.bedrock.mongo.BedrockMongo;
import me.ohvalsgod.bedrock.permissions.rank.BRankManager;
import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.player.BPlayerManager;
import me.ohvalsgod.bedrock.player.profiling.provider.AbstractProfileProvider;
import me.ohvalsgod.bedrock.player.profiling.provider.implementation.ProfileProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Bedrock extends JavaPlugin {

    @Getter private static Bedrock instance;

    @Getter private boolean loaded;

    //  Files
    private FileConfig databaseConfig, mainConfig, langConfig;

    //  Database
    private BedrockJedis jedis;
    private BedrockMongo mongo;
    private AbstractProfileProvider<BPlayer> profileProvider;   //  For future projects, will compile this and use a default profile object,
                                                                //  new projects can just extend that object, and since we'll be using generics
                                                                //  it will be much easier.

    //  Handlers and such
    private Language language;
    private BSettings settings;
    private BPlayerManager playerManager;
    private BRankManager rankManager;
    private ChatManager chatManager;
    private UUIDCache uuidCache;

    @Override
    public void onEnable() {
        instance = this;

        initFiles();
        initDatabases();
        initHandlers();

        loaded = true;
    }

    private void initDatabases() {
        final ConfigCursor cursor = new ConfigCursor(databaseConfig, null);

        jedis = new BedrockJedis(new JedisSettings(
                cursor.getString("redis.host"),
                cursor.getInt("redis.port"),
                cursor.getString("redis.password")
        ));

        mongo = new BedrockMongo();

        if (mongo.connect()) {
            getLogger().info("Successfully connected to MongoDB in database '" + mongo.getDatabase().getName() + "'.");
        } else {
            getLogger().severe("[ERROR] Could not connect to MongoDB... this will cause the plugin to break!");
        }

        profileProvider = new ProfileProvider(instance);
        profileProvider.setMongo(mongo);
    }

    private void initFiles() {
        mainConfig = new FileConfig(instance, "config.yml");
        databaseConfig = new FileConfig(instance, "database.yml");
        langConfig = new FileConfig(instance, "lang.yml");
    }

    private void initHandlers() {
        language = new Language(instance);
        uuidCache = new UUIDCache(instance);
        playerManager = new BPlayerManager(instance);
        rankManager = new BRankManager(instance);
        chatManager = new ChatManager(instance);

        CommandHandler.loadCommandsFromPackage(instance, "me.ohvalsgod.bedrock.command.commands");
        ListenerHandler.loadListenersFromPackage(this, "me.ohvalsgod.bedrock.listener.listeners");

        language.init();
        rankManager.load();
    }

    public static boolean mainThread(Class clazz) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getLogger().severe("[WARNING] Attempting to query on main thread! Occuring in class '" + clazz.getName() + "'.");
            return false;
        }
        return true;
    }

}
