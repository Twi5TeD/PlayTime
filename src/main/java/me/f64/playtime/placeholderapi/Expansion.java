package me.f64.playtime.placeholderapi;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        TopPlayers[] top10 = Playtime.getTopTen();
        top10 = Playtime.checkOnlinePlayers(top10);
        if (top10.length <= pos - 1)
            return type.equals("name") ? Chat.format(c.getString("placeholder.top.name"))
                    : Chat.format(c.getString("placeholder.top.time"));
        TopPlayers top = top10[pos - 1];
        return type.equals("name") ? top.name : TimeFormat.getTime(Duration.of(top.time, ChronoUnit.SECONDS));
    }

    @Override
    public String onPlaceholderRequest(Player player, String commandLabel) {
        Chat chat = new Chat(plugin);

        if (commandLabel.equals("serveruptime"))
            return String.valueOf(TimeFormat.Uptime());
        if (commandLabel.equals("position")) {

            try {
                JSONParser jsonParser = new JSONParser();

                File dir = new File(plugin.storagePath);

                File[] fileList = dir.listFiles();

                int i = 0;
                if (fileList != null) {
                    ArrayList<TopPlayers> allPlayers = new ArrayList<>();
                    TopPlayers target = new TopPlayers();

                    for (File jsonFile : fileList) {
                        FileReader reader = new FileReader(jsonFile);
                        JSONObject playerJSON = (JSONObject) jsonParser.parse(reader);
                        reader.close();

                        TopPlayers element = new TopPlayers(playerJSON.get("lastName").toString(), playerJSON.get("uuid").toString(),
                                Integer.parseInt(playerJSON.get("time").toString()));

                        if(element.name == player.getName())
                            target = element;
                        allPlayers.add(element);
                    }

                    allPlayers.sort(Comparator.comparing(e -> e.time));
                    Collections.reverse(allPlayers);

                    i = allPlayers.indexOf(target);

                }
                return i >= 0 ? i + "" : "0";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
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
            return String.valueOf(TimeFormat.getTime(Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS)));
        if (commandLabel.equals("time_seconds"))
            return String.valueOf(Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds());
        if (commandLabel.equals("time_minutes")) {
            long sec = Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60;
            return String.valueOf(new Long(min).intValue());
        }
        if (commandLabel.equals("time_hours")) {
            long sec = Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60;
            return String.valueOf(new Long(hour).intValue());
        }
        if (commandLabel.equals("time_days")) {
            long sec = Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60, day = hour / 24;
            return String.valueOf(new Long(day).intValue());
        }
        if (commandLabel.equals("time_weeks")) {
            long sec = Duration.of(chat.ticksPlayed(player), ChronoUnit.SECONDS).getSeconds();
            long min = sec / 60, hour = min / 60, day = hour / 24, week = day / 7;
            return String.valueOf(new Long(week).intValue());
        }
        if (commandLabel.equals("session")) {
            return String.valueOf(TimeFormat.getTime(Duration.of(Expansion.plugin.getPlayerSession(player.getName()), ChronoUnit.SECONDS)));
        }
        if (commandLabel.equals("timesjoined"))
            return String.valueOf(chat.sessionsPlayed(player));
        return null;
    }
}
