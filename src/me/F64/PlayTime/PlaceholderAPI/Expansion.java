package me.F64.PlayTime.PlaceholderAPI;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Commands.PlaytimeTop;
import me.F64.PlayTime.Utils.Chat;
import me.F64.PlayTime.Utils.TimeFormat;
import me.F64.PlayTime.Utils.TopPlayers;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Expansion extends PlaceholderExpansion {
    private Main plugin;

    public Expansion(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "playtime";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String commandLabel) {
        if (commandLabel.equals("serveruptime"))
            return String.valueOf(TimeFormat.Uptime());
        if (commandLabel.startsWith("top_")) {
            TopPlayers[] top10 = PlaytimeTop.getTopTen();
            top10 = PlaytimeTop.checkOnlinePlayers(top10);
            for (int i = 0; i < top10.length; i++) {
                if (top10[i].time == 0) {
                    break;
                }
                if (commandLabel.equals("top_" + (i + 1) + "_place"))
                    return Integer.toString(i + 1);
                if (commandLabel.equals("top_" + (i + 1) + "_name"))
                    return top10[i].name;
                if (commandLabel.equals("top_" + (i + 1) + "_time"))
                    return TimeFormat.getTime(Duration.of(top10[i].time, ChronoUnit.SECONDS));
            }
        }
        if (player == null)
            return null;
        if (commandLabel.equals("player"))
            return String.valueOf(player.getName());
        if (commandLabel.equals("time"))
            return String.valueOf(TimeFormat.getTime(Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS)));
        if (commandLabel.equals("timesjoined"))
            return String.valueOf(player.getStatistic(Statistic.LEAVE_GAME) + 1);
        return null;
    }
}