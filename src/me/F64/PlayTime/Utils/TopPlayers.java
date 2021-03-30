package me.F64.PlayTime.Utils;

public class TopPlayers {
    public String name;
    public String uuid;
    public int time;

    public TopPlayers() {
        time = 0;
    }

    public TopPlayers(String n, String u, int t) {
        name = n;
        uuid = u;
        time = t;
    }
}