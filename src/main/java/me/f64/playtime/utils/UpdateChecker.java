package me.f64.playtime.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import me.f64.playtime.Main;
import org.bukkit.Bukkit;

public class UpdateChecker {
    static Main plugin;
    public static int resourceId;

    public UpdateChecker(Main instance, int resourceId) {
        plugin = instance;
        UpdateChecker.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
                    .openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}
