package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreActivity extends AppCompatActivity {
    Context context = this;
    TextView txvShowScore = null;
    Button btnRetry = null;
    Button btnBackToMenu = null;
    RecyclerView rcvRanking = null;
    SQLiteDatabaseHelper sqlHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent fromGamePageIntent = getIntent();
        txvShowScore = findViewById(R.id.txv_show_score);
        btnRetry = findViewById(R.id.btn_retry);
        btnBackToMenu = findViewById(R.id.btn_back_to_menu);
        rcvRanking = findViewById(R.id.rcv_ranking);
        sqlHelper = new SQLiteDatabaseHelper(context);

        btnRetry.setOnClickListener(buttonOnClickListener);
        btnBackToMenu.setOnClickListener(buttonOnClickListener);

        int score = fromGamePageIntent.getIntExtra(GameManager.INTENT_EXTRA_SCORE_NAME, -1);
        GameDiff gameDiff = GameManager.getInstance().getGameDiff();
        RankingRecord newRR = new RankingRecord(score, new Date(), gameDiff);

        if(score >= 0) {
            txvShowScore.setText(String.format("Score: %d.%02d", score / 100, score % 100));
            sqlHelper.insertData(newRR);
        } else {
            txvShowScore.setText(R.string.str_show_score_default);
        }

        List<RankingRecord> rrList = sqlHelper.readData(gameDiff);
        List<String> itemList = rrList.stream().sorted().map(RankingRecord::toString).collect(Collectors.toList());
        int newRecordIdx = itemList.indexOf(newRR.toString());

        rcvRanking.setLayoutManager(new LinearLayoutManager(context));
        rcvRanking.addItemDecoration(new RankingItemDecoration(context, newRecordIdx));
        rcvRanking.setAdapter(new RankingAdapter(itemList));
    }

    @Override
    protected void onDestroy() {
        txvShowScore = null;
        btnRetry = null;
        btnBackToMenu = null;

        super.onDestroy();
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_retry:
                    setResult(RESULT_OK);
                    finish();
                    break;

                case R.id.btn_back_to_menu:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
            }
        }
    };
}
