package me.ohvalsgod.bedrock.util;

import me.ohvalsgod.bedrock.util.bukkit.SimpleLocation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MathUtil {

    public static SimpleLocation getLocationInFrontOfPlayer(Player player, double distance) {
        return new SimpleLocation(player.getLocation().getWorld().getName(), player.getLocation().getX() + distance, player.getLocation().getY(), player.getLocation().getZ() + distance, player.getLocation().getYaw(), player.getLocation().getPitch());
    }

    public static SimpleLocation getLocationInFrontOfLocation(double x, double y, double z, float yaw, float pitch, double distance) {
        return new SimpleLocation("world", x + distance, y, z + distance, yaw, pitch);
    }

    public static boolean isMouseOverEntity(Player player) {
        return rayTrace(player, 6.0) != null;
    }

    public static Entity rayTrace(Player player, double distance) {
        SimpleLocation playerLocation = new SimpleLocation(player.getLocation());
        Entity currentTarget = null;
        float lowestFov = Float.MAX_VALUE;

        for (Entity entity : player.getNearbyEntities(distance, distance, distance)) {
            SimpleLocation entityLocation = new SimpleLocation(entity.getLocation());
            float fov = getRotationFromPosition(playerLocation, entityLocation)[0] - playerLocation.getYaw();
            double groundDistance = playerLocation.getGroundDistanceTo(entityLocation);
            if (lowestFov >= fov || (double) fov >= groundDistance + 2.0) continue;
            currentTarget = entity;
            lowestFov = fov;
        }

        return currentTarget;
    }

    public static float[] getRotationFromPosition(SimpleLocation playerLocation, SimpleLocation targetLocation) {
        double xDiff = targetLocation.getX() - playerLocation.getX();
        double zDiff = targetLocation.getZ() - playerLocation.getZ();
        double yDiff = targetLocation.getY() - (playerLocation.getY() + 0.4);

        double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);

        return new float[]{yaw, pitch};
    }

    public static double getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) distance = 360.0f - distance;

        return distance;
    }

    public static int pingFormula(long ping) {
        return (int) Math.ceil((double) ping / 50.0);
    }
}

