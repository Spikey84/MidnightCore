package me.spikey.midnightcore.hardcore;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.utils.UUIDUtils;


import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class TimeJoinedDAO {
    public static Timestamp getTimeJoined(Connection connection, UUID uuid) {
        Timestamp temp = null;

        try {
            Class.forName("org.sqlite.JDBC");

            String query = """
                    SELECT * FROM test WHERE uuid=?;
                    """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                temp = resultSet.getTimestamp("time");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static void setTime(Connection connection, UUID uuid, Timestamp timestamp) {
        PreparedStatement statement = null;

        try {

            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO test (uuid, time) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setTimestamp(2, timestamp);

            statement.execute();
            statement.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
