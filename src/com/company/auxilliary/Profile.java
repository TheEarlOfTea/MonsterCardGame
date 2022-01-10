package com.company.auxilliary;

public class Profile {
    private String username;
    private String name;
    private String bio;
    private String picture;
    private boolean isBattleReady;
    private int wins;
    private int losses;
    private int elo;
    private int coins;

    public String getWinrate(){
        float w= (float) wins;
        float l= (float) losses;
        return (""+(w/l)*100+"%");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isBattleReady() {
        return isBattleReady;
    }

    public void setBattleReady(boolean battleReady) {
        isBattleReady = battleReady;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
