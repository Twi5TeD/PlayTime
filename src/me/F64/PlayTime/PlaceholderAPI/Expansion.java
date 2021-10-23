package me.F64.PlayTime.PlaceholderAPI;

import java.io.FileReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Commands.Playtime;
import me.F64.PlayTime.Commands.PlaytimeTop;
import me.F64.PlayTime.Utils.Chat;
import me.F64.PlayTime.Utils.TimeFormat;
import me.F64.PlayTime.Utils.TopPlayers;
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
        if (commandLabel.equals("timesjoined"))
            return String.valueOf(player.getStatistic(Statistic.LEAVE_GAME) + 1);
        return null;
    }
}