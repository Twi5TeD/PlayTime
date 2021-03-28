package me.F64.PlayTime.Utils;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import me.F64.PlayTime.Main;

public class TimeFormat {
    public static  String getTime(int seconds) {
        if (seconds < 60) {
            return seconds + Main.second;
        }
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60) {
            if (secondsLeft > 0) {
                return String.valueOf(minutes + Main.minute + " " + secondsLeft + Main.second);
            }
            return String.valueOf(minutes + Main.minute);
        }
        if (minutes < 1440) {
            String time = "";
            int hours = minutes / 60;
            time = hours + Main.hour;
            int inMins = 60 * hours;
            int leftOver = minutes - inMins;
            if (leftOver >= 1) {
                time = time + " " + leftOver + Main.minute;
            }
            if (secondsLeft > 0) {
                time = time + " " + secondsLeft + Main.second;
            }
            return time;
        }
        String time = "";
        int days = minutes / 1440;
        time = days + Main.day;
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + Main.minute;
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + Main.hour;
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;
                if (leftOver >= 1) {
                    time = time + " " + minsLeft + Main.minute;
                }
            }
        }
        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + Main.second;
        }
        return time;
    }

    public static  String Uptime() {
        return TimeFormat.getTime((int) TimeUnit.MILLISECONDS.toSeconds(ManagementFactory.getRuntimeMXBean().getUptime()));
    }
}