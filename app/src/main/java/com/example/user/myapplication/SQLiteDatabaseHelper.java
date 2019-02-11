package com.example.user.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ranking.db";
    public static final String RANKING_TABLE_NAME = "Ranking";
    public static final String RANKING_TABLE_COL_ID = "_id";
    public static final String RANKING_TABLE_COL_SCORE = "_score";
    public static final String RANKING_TABLE_COL_DATE = "_date";
    public static final String RANKING_TABLE_COL_GAME_DIFF = "_gameDiff";
    private final Context context;

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + RANKING_TABLE_NAME + " (" +
                RANKING_TABLE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RANKING_TABLE_COL_SCORE + " VARCHAR(20), " +
                RANKING_TABLE_COL_DATE + " VARCHAR(10), " +
                RANKING_TABLE_COL_GAME_DIFF + " VARCHAR(10)" +
                ");";
        sqLiteDatabase.execSQL(sqlCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // TODO: insert, read, update, delete

    // insert
    public void insertData(RankingRecord rr) {
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues cv = new ContentValues();

        cv.put(RANKING_TABLE_COL_SCORE, rr.getScoreString());
        cv.put(RANKING_TABLE_COL_DATE, rr.getRecordDateString());
        cv.put(RANKING_TABLE_COL_GAME_DIFF, rr.getGameDiff().name());
        final long result = db.insert(RANKING_TABLE_NAME, null, cv);

        if(result == -1L)
            Toast.makeText(this.context, "Some error happened when inserting data.", Toast.LENGTH_SHORT).show();

        db.close();
    }

    // read
    public List<RankingRecord> readData() {
        final SQLiteDatabase db = getReadableDatabase();
        final String sqlQuery = "SELECT * FROM " + RANKING_TABLE_NAME;
        final Cursor cursor = db.rawQuery(sqlQuery, null);
        final List<RankingRecord> retList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                final String _scoreString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_SCORE));
                final String _dateString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_DATE));
                final String _gameDiffString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_GAME_DIFF));

                if(_gameDiffString.equals("GAME_DIFF_EASY"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_EASY));
                else if(_gameDiffString.equals("GAME_DIFF_NORMAL"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_NORMAL));
                else if(_gameDiffString.equals("GAME_DIFF_HARD"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_HARD));
                else if(_gameDiffString.equals("GAME_DIFF_LUNATIC"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_LUNATIC));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return retList;
    }

    // read the data where their game diffs are the same
    public List<RankingRecord> readData(GameDiff gameDiff) {
        final SQLiteDatabase db = getReadableDatabase();
        final String sqlQuery = "SELECT * FROM " + RANKING_TABLE_NAME + " WHERE " + RANKING_TABLE_COL_GAME_DIFF + " = '" + gameDiff.name() + "'";
        final Cursor cursor = db.rawQuery(sqlQuery, null);
        final List<RankingRecord> retList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                final String _scoreString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_SCORE));
                final String _dateString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_DATE));
                final String _gameDiffString = cursor.getString(cursor.getColumnIndex(RANKING_TABLE_COL_GAME_DIFF));

                if(_gameDiffString.equals("GAME_DIFF_EASY"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_EASY));
                else if(_gameDiffString.equals("GAME_DIFF_NORMAL"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_NORMAL));
                else if(_gameDiffString.equals("GAME_DIFF_HARD"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_HARD));
                else if(_gameDiffString.equals("GAME_DIFF_LUNATIC"))
                    retList.add(new RankingRecord(_scoreString, _dateString, GameDiff.GAME_DIFF_LUNATIC));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return retList;
    }
}
