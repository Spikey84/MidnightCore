package me.spikey.midnightcore.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class VectorUtils {

    public static Vector vectorBetweenTwoPoints(Location loc1, Location loc2) {
        Vector3d v1 = new Vector3d(loc1.getX(), loc1.getY(), loc1.getZ());
        Vector3d v2 = new Vector3d(loc2.getX(), loc2.getY(), loc2.getZ());

        v1.normalize(v2);

        Vector vector = new Vector(v1.x, v2.y, v2.z);
        return vector;
    }
}
