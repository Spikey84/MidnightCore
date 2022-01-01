package me.spikey.midnightcore.homes;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class HomesDAO {
    public static List<Home> getHomes(Connection connection) {
        PreparedStatement statement = null;
        List<Home> homes = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM namedhomes;";

            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Location location = new Location(Bukkit.getWorld(resultSet.getString("world_name")), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
                UUID uuid = UUIDUtils.build(resultSet.getString("uuid"));
                String name = resultSet.getString("name");
                Home home = new Home(location, uuid, name);
                home.setId(resultSet.getInt("id"));
                homes.add(home);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homes;
    }

    public static List<Home> getHomesByUUID(Connection connection, UUID uuid) {
        PreparedStatement statement = null;
        List<Home> homes = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM namedhomes WHERE uuid=?;";

            statement = connection.prepareStatement(query);
            statement.setString(1, UUIDUtils.strip(uuid));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Location location = new Location(Bukkit.getWorld(resultSet.getString("world_name")), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
                String name = resultSet.getString("name");
                Home home = new Home(location, uuid, name);
                home.setId(resultSet.getInt("id"));
                homes.add(home);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homes;
    }

    public static void removeHome(Connection connection, int id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM namedhomes WHERE id=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHome(Connection connection, Home home) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO namedhomes (uuid, x, y, z, world_name, name) \
                    VALUES\
                    (?, ?, ?, ?, ?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(home.getUuid()));
            statement.setInt(2, home.getLocation().getBlockX());
            statement.setInt(3, home.getLocation().getBlockY());
            statement.setInt(4, home.getLocation().getBlockZ());
            statement.setString(5, home.getLocation().getWorld().getName());
            statement.setString(6, home.getName());

            statement.execute();
            statement.close();

            if (home.getId() == -1) {
                String s = "SELECT id from namedhomes where id = (select max(id) from namedhomes);";
                Statement statement1 = connection.createStatement();
                statement1.execute(s);


                ResultSet resultSet = statement1.getResultSet();
                resultSet.next();
                home.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
