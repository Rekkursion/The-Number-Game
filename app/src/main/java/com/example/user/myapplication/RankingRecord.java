package com.example.user.myapplication;

import java.util.Date;

public class RankingRecord {
    private String scoreString;
    private int score;
    private String recordDateString;

    public RankingRecord(String scoreString, Date date) {
        this.scoreString = scoreString;
        this.score = GameManager.scoreString2Integer(this.scoreString);
        this.recordDateString = date.toString();
    }

    public RankingRecord(int score, Date date) {
        this.scoreString = String.format("%d.%02d", score / 100, score % 100);
        this.score = score;
        this.recordDateString = date.toString();
    }

    public RankingRecord(String scoreString, String dateString) {
        this.scoreString = scoreString;
        this.score = GameManager.scoreString2Integer(this.scoreString);
        this.recordDateString = dateString;
    }

    public RankingRecord(int score, String dateString) {
        this.scoreString = String.format("%d.%02d", score / 100, score % 100);
        this.score = score;
        this.recordDateString = dateString;
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
}
