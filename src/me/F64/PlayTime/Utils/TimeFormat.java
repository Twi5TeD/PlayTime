package me.F64.PlayTime.Utils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import me.F64.PlayTime.Main;

public class TimeFormat {
    public static String getTime(final Duration duration) {
        final StringBuilder builder = new StringBuilder();
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        days %= 7;
        if (seconds > 0) {
            builder.insert(0, seconds + Main.second);
        }
        if (minutes > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }
            builder.insert(0, minutes + Main.minute);
        }
        if (hours > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }
            builder.insert(0, hours + Main.hour);
        }
        if (days > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }
            builder.insert(0, days + Main.day);
        }
        if (weeks > 0) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }
            builder.insert(0, weeks + Main.week);
        }
        return builder.toString();
    }

    public static String Uptime() {
        return TimeFormat.getTime(
                Duration.of(TimeUnit.MILLISECONDS.toSeconds(ManagementFactory.getRuntimeMXBean().getUptime()),
                        ChronoUnit.SECONDS));
    }
}