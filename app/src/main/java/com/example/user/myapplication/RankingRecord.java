package com.example.user.myapplication;

import android.support.annotation.NonNull;

import java.util.Date;

public class RankingRecord implements Comparable<RankingRecord> {
    private String scoreString;
    private int score;
    private String recordDateString;
    private GameDiff gameDiff;

    public RankingRecord(String scoreString, Date date, GameDiff gameDiff) {
        this.scoreString = scoreString;
        this.score = GameManager.scoreString2Integer(this.scoreString);
        this.recordDateString = date.toString();
        this.gameDiff = gameDiff;
    }

    public RankingRecord(int score, Date date, GameDiff gameDiff) {
        this.scoreString = String.format("%d.%02d", score / 100, score % 100);
        this.score = score;
        this.recordDateString = date.toString();
        this.gameDiff = gameDiff;
    }

    public RankingRecord(String scoreString, String dateString, GameDiff gameDiff) {
        this.scoreString = scoreString;
        this.score = GameManager.scoreString2Integer(this.scoreString);
        this.recordDateString = dateString;
        this.gameDiff = gameDiff;
    }

    public RankingRecord(int score, String dateString, GameDiff gameDiff) {
        this.scoreString = String.format("%d.%02d", score / 100, score % 100);
        this.score = score;
        this.recordDateString = dateString;
        this.gameDiff = gameDiff;
    }

    public String getScoreString() {
        return this.scoreString;
    }
    public void setScoreString(String scoreString) {
        this.scoreString = scoreString;
    }

    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public String getRecordDateString() {
        return this.recordDateString;
    }
    public void setRecordDateString(String recordDateString) {
        this.recordDateString = recordDateString;
    }

    public GameDiff getGameDiff() {
        return this.gameDiff;
    }

    @Override
    public String toString() {
        return this.scoreString + ", " + this.recordDateString;
    }

    @Override
    public int compareTo(@NonNull RankingRecord rr) {
        return this.score - rr.score;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof RankingRecord))
            return false;
        return this.recordDateString.equals(((RankingRecord)obj).recordDateString);
    }

    @Override
    public int hashCode() {
        return this.recordDateString.hashCode();
    }
}
