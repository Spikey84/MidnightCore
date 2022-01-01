package me.spikey.midnightcore.cosmetics.tags;

import me.spikey.midnightcore.utils.UUIDUtils;
import net.md_5.bungee.api.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.awt.*;

public class TagsDAO {
    public static void createTable(Connection connection) {
        try {
            Statement statement = null;
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL,\
                    id INT NOT NULL\
                    );
                    """.formatted("tags");

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int get(Connection connection, UUID uuid) {

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM tags WHERE uuid=?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void remove(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM tags WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(Connection connection, UUID uuid, int id) {
        remove(connection, uuid);
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO tags (uuid, id) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setInt(2, id);
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
