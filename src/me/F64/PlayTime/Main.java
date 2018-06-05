package me.F64.PlayTime;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin;
	public static String second;
	public static String minute;
	public static String hour;
	public static String day;
	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getServer().getPluginManager().registerEvents(new PlayTime(this), this);
		getCommand("playtime").setExecutor(new PlayTime(this));
		getCommand("serveruptime").setExecutor(new PlayTime(this));
		getCommand("playtimereload").setExecutor(new PlayTime(this));
		  FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
			second = PlayTime.format(c.getString("time.second"));
			minute = PlayTime.format(c.getString("time.minute"));
			hour = PlayTime.format(c.getString("time.hour"));
			day = PlayTime.format(c.getString("time.day"));
    }
}