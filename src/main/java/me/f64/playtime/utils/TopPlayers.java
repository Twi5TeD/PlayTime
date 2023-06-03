package me.f64.playtime.utils;

public class TopPlayers {
    public String name;
    public String uuid;
    public Integer time;

    public TopPlayers() {
        time = 0;
    }

    public TopPlayers(String name, String uuid, int time) {
        this.name = name;
        this.uuid = uuid;
        this.time = time;
    }
}
