package me.F64.PlayTime.Utils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.FileConfiguration;

import me.F64.PlayTime.Commands.Playtime;

public class TimeFormat {
    public static String getTime(Duration duration) {
        FileConfiguration c = Playtime.config.getConfig();
        final StringBuilder builder = new StringBuilder();
        long sec = duration.getSeconds();
        long min = sec / 60, hour = min / 60, day = hour / 24, week = day / 7;
        sec %= 60;
        min %= 60;
        hour %= 24;
        day %= 7;
        long fSec = sec, fMin = 0, fHour = 0, fDay = 0, fWeek = 0;
        if (min > 0) {
            if (c.getBoolean("time.minute.enabled")) {
                fMin = min;
            } else {
                fSec += min * 60;
            }
        }
        if (hour > 0) {
            if (c.getBoolean("time.hour.enabled")) {
                fHour = hour;
            } else {
                if (c.getBoolean("time.minute.enabled"))
                    fMin += hour * 60;
                else
                    fSec += hour * 60 * 60;
            }
        }
        if (day > 0) {
            if (c.getBoolean("time.day.enabled")) {
                fDay = day;
            } else {
                if (c.getBoolean("time.hour.enabled"))
                    fHour += day * 24;
                else if (c.getBoolean("time.minute.enabled"))
                    fMin += day * 24 * 60;
                else
                    fSec += day * 24 * 60 * 60;
            }
        }
        if (week > 0) {
            if (c.getBoolean("time.week.enabled")) {
                fWeek = week;
            } else {
                if (c.getBoolean("time.day.enabled"))
                    fDay += week * 7;
                else if (c.getBoolean("time.hour.enabled"))
                    fHour += week * 7 * 24;
                else if (c.getBoolean("time.minute.enabled"))
                    fMin += week * 7 * 24 * 60;
                else
                    fSec += week * 7 * 24 * 60 * 60;
            }
        }
        if (fWeek > 0) {
            builder.append(fWeek + Chat.format(c.getString("time.week.prefix")));
        }
        if (fDay > 0) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(fDay + Chat.format(c.getString("time.day.prefix")));
        }
        if (fHour > 0) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(fHour + Chat.format(c.getString("time.hour.prefix")));
        }
        if (fMin > 0) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(fMin + Chat.format(c.getString("time.minute.prefix")));
        }
        if (c.getBoolean("time.second.enabled") && fSec > 0) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(fSec + Chat.format(c.getString("time.second.prefix")));
        }
        return builder.toString();
    }

    public static String Uptime() {
        return TimeFormat.getTime(Duration.of(
                TimeUnit.MILLISECONDS.toSeconds(ManagementFactory.getRuntimeMXBean().getUptime()), ChronoUnit.SECONDS));
    }
}