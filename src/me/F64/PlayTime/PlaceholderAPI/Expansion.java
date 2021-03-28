package me.F64.PlayTime.PlaceholderAPI;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Commands.PlayTime;
import me.F64.PlayTime.Utils.TimeFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class Expansion extends PlaceholderExpansion {

    private Main plugin;

    public Expansion(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "playtime";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }
        if (identifier.equals("player")) {
            return String.valueOf(p.getName());
        }
        if (identifier.equals("time")) {
            return String.valueOf(TimeFormat.getTime(PlayTime.TicksPlayed(p)/20));
        }
        if (identifier.equals("timesjoined")) {
            return String.valueOf(p.getStatistic(Statistic.LEAVE_GAME) + 1);
        }
        if (identifier.equals("serveruptime")) {
            return String.valueOf(TimeFormat.Uptime());
        }
        return null;
    }
}