package me.ohvalsgod.bedrock.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.config.ConfigCursor;
import org.bson.Document;

import java.util.Collections;

@Getter
public class BedrockMongo {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> players;
    private MongoCollection<Document> ranks;
    private boolean connected;

    public BedrockMongo() {
        connected = false;
    }

    public boolean connect() {
        ConfigCursor cursor = new ConfigCursor(Bedrock.getInstance().getDatabaseConfig(), "mongo");
        try {
            if (!cursor.exists("host")
                    || !cursor.exists("port")
                    || !cursor.exists("database")
                    || !cursor.exists("authentication.enabled")
                    || !cursor.exists("authentication.username")
                    || !cursor.exists("authentication.password")
                    || !cursor.exists("authentication.database")) {
                throw new RuntimeException("Missing configuration option");
            }

            if (cursor.getBoolean("authentication.enabled")) {
                final MongoCredential credential = MongoCredential.createCredential(
                        cursor.getString("authentication.username"),
                        cursor.getString("authentication.database"),
                        cursor.getString("authentication.password").toCharArray()
                );

                client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")), Collections.singletonList(credential));
            } else {
                client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")));
            }

            database = client.getDatabase(cursor.getString("database"));
            players = database.getCollection("players");
            ranks = database.getCollection("ranks");

            connected = true;
            return true;
        } catch (Exception e) {
            connected = false;
            return false;
        }
    }

    public static BedrockMongo getInstance() {
        return Bedrock.getInstance().getMongo();
    }

}
