package me.F64.PlayTime;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.F64.PlayTime.Commands.PlayTime;
import me.F64.PlayTime.Commands.Uptime;
import me.F64.PlayTime.PlaceholderAPI.Expansion;

public class Main extends JavaPlugin implements Listener {
    public static Plugin plugin;
    public static String second;
    public static String minute;
    public static String hour;
    public static String day;
    private final String storagePath = getDataFolder() + "/userdata.json";

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getServer().getPluginManager().registerEvents(new PlayTime(this), this);
        getCommand("playtime").setExecutor(new PlayTime(this));
        getCommand("serveruptime").setExecutor(new Uptime(this));
        checkStorage();
        FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
        second = PlayTime.format(c.getString("time.second"));
        minute = PlayTime.format(c.getString("time.minute"));
        hour = PlayTime.format(c.getString("time.hour"));
        day = PlayTime.format(c.getString("time.day"));

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Expansion(this).register();
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(this::savePlayer);
    }

    private void checkStorage() {
        File pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }

        File userdataFile = new File(storagePath);
        if (!userdataFile.exists()) {
            try {
                FileWriter writer = new FileWriter(userdataFile.getAbsoluteFile());
                writer.write((new JSONArray()).toJSONString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void savePlayer(Player player) {
        JSONObject target = new JSONObject();
        target.put("uuid", player.getUniqueId().toString());
        target.put("lastName", player.getDisplayName());
        target.put("time", Integer.valueOf(PlayTime.TicksPlayed(player)));
        target.put("joins", Integer.valueOf(player.getStatistic(Statistic.LEAVE_GAME) + 1));
        writePlayer(target);
    }

    @SuppressWarnings("unchecked")
    private void writePlayer(JSONObject target) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);

            List<JSONObject> list = new ArrayList<>();
            for (Object player : players) {
                JSONObject player_JSON = (JSONObject) player;
                if (!player_JSON.get("uuid").equals(target.get("uuid")))
                    list.add(player_JSON);
            }
            for (int i = 0; i < list.size(); i++) {
                if (Integer.parseInt(target.get("time").toString()) > Integer.parseInt(list.get(i).get("time").toString())) {
                    JSONObject temp = list.get(i);
                    list.set(i, target);
                    target = temp;
                }
            }
            list.add(target);

            JSONArray sortedPlayers = new JSONArray();
            sortedPlayers.addAll(list);

            FileWriter writer = new FileWriter(storagePath);
            writer.write(sortedPlayers.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerTime(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            for (Object o : players) {
                JSONObject player = (JSONObject) o;
                if (player.get("lastName").equals(name)) {
                    return player.get("time").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerJoins(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            for (Object o : players) {
                JSONObject player = (JSONObject) o;
                if (player.get("lastName").equals(name)) {
                    return player.get("joins").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        savePlayer(event.getPlayer());
    }
}   