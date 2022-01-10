package com.company.battleTools;

public class BattleResult {
    private String winner;
    private String loser;
    private boolean draw;

    public BattleResult(){
        this.draw=false;
    }


    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }
}
