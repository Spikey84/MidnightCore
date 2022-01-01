package me.spikey.midnightcore.staffChat;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.utils.UUIDUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class StaffChatDAO {
    public static boolean getStaffChatValue(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM staffchat WHERE uuid=?;";

            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return resultSet.getBoolean("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<UUID, Boolean> getStaffChatValues(Connection connection) {
        PreparedStatement statement = null;
        HashMap<UUID, Boolean> values = Maps.newHashMap();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM staffchat;";

            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                values.put(UUIDUtils.build(resultSet.getString("uuid")), resultSet.getBoolean("value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static void removeStaffChatAccount(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM staffchat WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeStaffChatAccount(Connection connection, UUID uuid, boolean value) {
        PreparedStatement statement = null;

        removeStaffChatAccount(connection, uuid);

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO staffchat (uuid, value) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setBoolean(2, value);

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
