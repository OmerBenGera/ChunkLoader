package it.flowzz.chunkloader.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Util {

    public static Location deserializeLocation(String location) {
        String[] split = location.split(",");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        return new Location(world,x,y,z);
    }

    public static String[] getTimeFromSecond(int timeInSeconds) {

        int secondsLeft = timeInSeconds % 86400 % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds % 86400 / 3600);
        int days = (int) Math.floor(timeInSeconds / 86400);

        String DD = days < 10 ? "0" + days : String.valueOf(days);
        String HH = hours < 10 ? "0" + hours : String.valueOf(hours);
        String MM = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String SS = secondsLeft < 10 ? "0" + secondsLeft : String.valueOf(secondsLeft);

        return new String[]{
                DD,HH,MM,SS
        };
    }

}
