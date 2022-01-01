package me.spikey.midnightcore.discord.linking;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.utils.UUIDUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

public class LinkingDAO {

    public static void createTable(Connection connection) {
        try {
            Statement statement = null;
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL,\
                    id VARCHAR NOT NULL\
                    );
                    """.formatted("linking");

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<UUID, String> getLinkedAccounts(Connection connection) {
        Map<UUID, String> tmp = Maps.newHashMap();

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM linking;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                tmp.put(UUIDUtils.build(resultSet.getString("uuid")), resultSet.getString("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public static String getLinkedAccounts(Connection connection, UUID uuid) {
        Map<UUID, String> tmp = Maps.newHashMap();

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM linking WHERE uuid=?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return resultSet.getString("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void linkAccount(Connection connection, UUID uuid, String id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO linking (uuid, id) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setString(2, id);
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unlinkAccount(Connection connection, UUID uuid, String id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM linking WHERE uuid=? AND id=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setString(2, id);
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
