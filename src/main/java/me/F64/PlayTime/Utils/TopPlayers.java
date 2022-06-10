package me.F64.PlayTime.Utils;

public class TopPlayers {
    public String name;
    public String uuid;
    public int time;

    public TopPlayers() {
        time = 0;
    }

    public TopPlayers(String name, String uuid, int time) {
        this.name = name;
        this.uuid = uuid;
        this.time = time;
    }
}