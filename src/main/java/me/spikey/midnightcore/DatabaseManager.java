package me.spikey.midnightcore;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static File databaseFile;
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initDatabase(Plugin plugin) {

        File databaseFolder = new File(plugin.getDataFolder(), "core.db");
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        databaseFile = databaseFolder;

        Statement statement = null;
        try (Connection connection = getConnection()){
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL,\
                    time TIMESTAMP NOT NULL\
                    );
                    """.formatted("test");

            statement.execute(query);
            statement.close();

            statement = connection.createStatement();
            query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL\
                    );
                    """.formatted("isdead");

            statement.execute(query);
            statement.close();

            statement = connection.createStatement();

            query = """
                    CREATE TABLE IF NOT EXISTS staffchat (\
                      uuid VARCHAR(36) NOT NULL,\
                      value BOOLEAN NOT NULL,\
                      PRIMARY KEY (uuid)\
                    );
                    """;
            statement.executeUpdate(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
