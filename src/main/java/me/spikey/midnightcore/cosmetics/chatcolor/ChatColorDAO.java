package me.spikey.midnightcore.cosmetics.chatcolor;

import me.spikey.midnightcore.utils.UUIDUtils;
import net.md_5.bungee.api.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.awt.*;

public class ChatColorDAO {
    public static void createTable(Connection connection) {
        try {
            Statement statement = null;
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL,\
                    color INT NOT NULL\
                    );
                    """.formatted("chatColor");

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ChatColor get(Connection connection, UUID uuid) {

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM chatColor WHERE uuid=?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return ChatColor.of(new Color(resultSet.getInt("color")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void remove(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM chatColor WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void add(Connection connection, UUID uuid, ChatColor chatColor) {
        remove(connection, uuid);
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO chatColor (uuid, color) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setInt(2, chatColor.getColor().getRGB());
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
