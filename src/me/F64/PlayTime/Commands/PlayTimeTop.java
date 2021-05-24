package me.F64.PlayTime.Commands;

import java.io.FileReader;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Utils.Chat;
import me.F64.PlayTime.Utils.TimeFormat;
import me.F64.PlayTime.Utils.TopPlayers;

public class PlayTimeTop implements CommandExecutor {
    static Main plugin;

    public PlayTimeTop(Main instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender s, Command cmd, String commandLabel, String[] args) {
        if (s instanceof Player) {
            Player p = (Player) s;
            FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtimetop")) {
                if (!(s.hasPermission("playtime.check"))) {
                    for (String NoPermission : c.getStringList("messages.no_permission"))
                        Chat.message(s, p, NoPermission);
                    return true;
                }
                TopPlayers[] top10;
				top10 = getTopTen();
                top10 = checkOnlinePlayers(top10);
                for (String header : c.getStringList("messages.playtimetop.header"))
                    Chat.message(s, p, header);
                for (int i = 0; i < top10.length; i++) {
                    if (top10[i].time == 0) {
                        break;
                    }
                    for (String message : c.getStringList("messages.playtimetop.message"))
                        Chat.message(s, p,
                                message.replace("%position%", Integer.toString(i + 1))
                                        .replace("%player%", top10[i].name)
                                        .replace("%playtime%", TimeFormat.getTime(top10[i].time)));
                }
                for (String footer : c.getStringList("messages.playtimetop.footer"))
                    Chat.message(s, p, footer);
            }
        }
        return true;
    }

    public static TopPlayers[] getTopTen() {
        TopPlayers[] topTen = {};
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(plugin.storagePath);
            JSONArray ps = (JSONArray) jsonParser.parse(reader);
            int len = Math.min(ps.size(), 10);
            topTen = new TopPlayers[len];
            for (int i = 0; (i < len); i++) {
                JSONObject p = (JSONObject) ps.get(i);
                TopPlayers t = new TopPlayers(p.get("lastName").toString(), p.get("uuid").toString(),
                        Integer.parseInt(p.get("time").toString()));
                topTen[i] = t;
            }
            return topTen;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return topTen;
    }

    public static TopPlayers[] checkOnlinePlayers(TopPlayers[] top10) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (Chat.TicksPlayed(p) > top10[top10.length-1].time) {
                TopPlayers np = new TopPlayers(p.getName(), p.getUniqueId().toString(), Chat.TicksPlayed(p));
                for (int i = 0; i < top10.length; i++) {
                    if (top10[i].time < np.time) {
                        if (top10[i].uuid.equals(np.uuid)) {
                            top10[i] = np;
                            break;
                        } else {
                            TopPlayers temp = top10[i];
                            top10[i] = np;
                            np = temp;
                        }
                    }
                }
            }
        }
        return top10;
    }
}