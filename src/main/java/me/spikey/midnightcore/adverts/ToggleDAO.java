package me.spikey.midnightcore.adverts;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.utils.UUIDUtils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class ToggleDAO {

    public static void createTable(Connection connection) {
        try {
            Statement statement = null;
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS %s (\
                    uuid VARCHAR NOT NULL\
                    );
                    """.formatted("adverts");

            statement.execute(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<UUID> getNoAdverts(Connection connection) {
        List<UUID> tmp = Lists.newArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                   SELECT * FROM adverts;
                   """;
            PreparedStatement statement = connection.prepareStatement(query);

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                tmp.add(UUIDUtils.build(resultSet.getString("uuid")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public static boolean getNoAdverts(Connection connection, UUID uuid) {
        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                   SELECT * FROM adverts WHERE uuid=?;
                   """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void add(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO adverts (uuid) \
                    VALUES\
                    (?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM adverts WHERE uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
