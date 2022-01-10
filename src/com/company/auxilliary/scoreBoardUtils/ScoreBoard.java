package com.company.auxilliary.scoreBoardUtils;

import java.util.LinkedList;

public class ScoreBoard {
    private LinkedList<Score> scores;

    public ScoreBoard(){
        this.scores= new LinkedList<Score>();
    }

    public LinkedList<Score> getScores() {
        return scores;
    }
    public void addScore(Score s){
        scores.add(s);
    }

    @Override
    public String toString() {
        return "ScoreBoard{" +
                "scores=" + scores +
                '}';
    }
}
