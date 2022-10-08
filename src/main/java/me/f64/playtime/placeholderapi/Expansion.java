package me.f64.playtime.placeholderapi;

import java.io.FileReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.f64.playtime.commands.Playtime;
import me.f64.playtime.Main;
import me.f64.playtime.utils.TimeFormat;
import me.f64.playtime.utils.TopPlayers;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import me.f64.playtime.commands.PlaytimeTop;
import me.f64.playtime.utils.Chat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Expansion extends PlaceholderExpansion {
    static Main plugin;
    static Pattern topPlaceholder = Pattern.compile("top_([0-9]+)_(name|time)");
    Pattern positionPlaceholder = Pattern.compile("position_([_A-Za-z0-9]+)");

    public Expansion(Main instance) {
        plugin = instance;
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

    public String get(int pos, String type) {
        FileConfiguration c = Playtime.config.getConfig();
        TopPlayers[] top10 = PlaytimeTop.getTopTen();
        top10 = PlaytimeTop.checkOnlinePlayers(top10);
        if (top10.length <= pos - 1)
            return type.equals("name") ? Chat.format(c.getString("placeholder.top.name"))
                    : Chat.format(c.getString("placeholder.top.time"));
        TopPlayers top = top10[pos - 1];
        return type.equals("name") ? top.name : TimeFormat.getTime(Duration.of(top.time, ChronoUnit.SECONDS));
    }

    @Override
    public String onPlaceholderRequest(Player player, String commandLabel) {
        if (commandLabel.equals("serveruptime"))
            return String.valueOf(TimeFormat.Uptime());
        if (commandLabel.equals("position")) {
            int i = 0;
            try {
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(plugin.storagePath);
                JSONArray players = (JSONArray) jsonParser.parse(reader);
                while (true) {
                    if (players.size() == i)
                        break;
                    JSONObject p = (JSONObject) players.get(i++);
                    if (p.get("lastName").toString().equals(player.getName()))
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return i + "";
        }
        if (commandLabel.startsWith("top_")) {
            Matcher m = topPlaceholder.matcher(commandLabel);
            if (m.find()) {
                int pos = Integer.parseInt(m.group(1));
                String type = m.group(2);
                return get(pos, type);
            }
        }
        if (player == null)
            return null;
        if (commandLabel.equals("player"))
            return String.valueOf(player.getName());
        if (commandLabel.equals("time"))
            return String.valueOf(TimeFormat.getTime(Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS)));
        if (commandLabel.equals("time_seconds"))
            return String.valueOf(Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds());
        if (commandLabel.equals("time_minutes")) {
            long sec = Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60;
            return String.valueOf(new Long(min).intValue());
        }
        if (commandLabel.equals("time_hours")) {
            long sec = Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60;
            return String.valueOf(new Long(hour).intValue());
        }
        if (commandLabel.equals("time_days")) {
            long sec = Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60, day = hour / 24;
            return String.valueOf(new Long(day).intValue());
        }
        if (commandLabel.equals("time_weeks")) {
            long sec = Duration.of(Chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60, day = hour / 24, week = day / 7;
            return String.valueOf(new Long(week).intValue());
        }
        if (commandLabel.equals("session")) {
            return String.valueOf(TimeFormat.getTime(Duration.of(Expansion.plugin.getPlayerSession(player.getName()), ChronoUnit.SECONDS)));
        }
        if (commandLabel.equals("timesjoined"))
            return String.valueOf(player.getStatistic(Statistic.LEAVE_GAME) + 1);
        return null;
    }
}
