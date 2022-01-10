package com.company.auxilliary.scoreBoardUtils;

public class Score {
    private String username;
    private int elo;

    public Score(String username, int elo) {
        this.username = username;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    @Override
    public String toString() {
        return "Score{" +
                "username='" + username + '\'' +
                ", elo=" + elo +
                '}';
    }
}
