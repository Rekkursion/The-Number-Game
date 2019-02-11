package com.example.user.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GameActivity extends AppCompatActivity {
    TextView[] txvArrCountDown = null;
    TextView txvTimer = null;
    TextView txvPenaltyTime = null;
    Button btnResumeAtPausePage = null;
    Button btnRetryAtPausePage = null;
    Button btnBackToMenuAtPausePage = null;
    ImageButton imgbtnRetry = null;
    ImageButton imgbtnPause = null;
    RelativeLayout rlyGameButtons = null;
    LinearLayout llyImgbtnGroup = null;
    LinearLayout llyPausePageBtnGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnResumeAtPausePage = findViewById(R.id.btn_resume_at_pause_page);
        btnResumeAtPausePage.setOnClickListener(buttonsAtPausePageOnClickListener);
        btnRetryAtPausePage = findViewById(R.id.btn_retry_at_pause_page);
        btnRetryAtPausePage.setOnClickListener(buttonsAtPausePageOnClickListener);
        btnBackToMenuAtPausePage = findViewById(R.id.btn_back_to_menu_at_pause_page);
        btnBackToMenuAtPausePage.setOnClickListener(buttonsAtPausePageOnClickListener);

        imgbtnRetry = findViewById(R.id.imgbtn_retry);
        imgbtnRetry.setOnClickListener(imageButtonsOnClickListener);
        imgbtnPause = findViewById(R.id.imgbtn_pause);
        imgbtnPause.setOnClickListener(imageButtonsOnClickListener);

        llyImgbtnGroup = findViewById(R.id.lly_imgbtn_group);
        llyPausePageBtnGroup = findViewById(R.id.lly_pause_page_buttons);
        llyPausePageBtnGroup.setVisibility(View.INVISIBLE);
        rlyGameButtons = findViewById(R.id.rly_game_buttons);

        Display display = getWindowManager().getDefaultDisplay();
        Intent fromMainIntent = getIntent();
        final int gameDiffOrdinal = fromMainIntent.getIntExtra("diff", 1);

        txvArrCountDown = new TextView[4];
        txvArrCountDown[0] = findViewById(R.id.txv_count_down_3);
        txvArrCountDown[1] = findViewById(R.id.txv_count_down_2);
        txvArrCountDown[2] = findViewById(R.id.txv_count_down_1);
        txvArrCountDown[3] = findViewById(R.id.txv_count_down_go);

        txvTimer = findViewById(R.id.txv_timer);
        txvPenaltyTime = findViewById(R.id.txv_penalty_time);

        try {
            GameManager.getInstance().initGameDataAndSetButtons(this, display, txvTimer, txvPenaltyTime, rlyGameButtons, Arrays.stream(GameDiff.values()).filter(diff -> diff.ordinal() == gameDiffOrdinal).collect(Collectors.toList()).get(0));
            GameManager.getInstance().countDownThenStartTimer(txvArrCountDown);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            setResult(GameManager.RES_CODE_SOME_ERROR_HAPPENED);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        GameManager.getInstance().endTheGame();

        txvArrCountDown = null;
        txvTimer = null;
        txvPenaltyTime = null;
        btnResumeAtPausePage = null;
        btnRetryAtPausePage = null;
        btnBackToMenuAtPausePage = null;
        imgbtnRetry = null;
        imgbtnPause = null;
        rlyGameButtons = null;
        llyImgbtnGroup = null;
        llyPausePageBtnGroup = null;

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case GameManager.REQ_CODE_TO_SCORE_PAGE:
                // retry
                if(resultCode == RESULT_OK) {
                    if(txvArrCountDown != null)
                        GameManager.getInstance().restartTheGame(txvArrCountDown);
                }

                // back to menu
                else {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // resume
        GameManager.getInstance().resumeTheGame();
    }

    View.OnClickListener imageButtonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgbtn_retry:
                    if(txvArrCountDown != null)
                        GameManager.getInstance().restartTheGame(txvArrCountDown);
                    break;

                case R.id.imgbtn_pause:
                    rlyGameButtons.setVisibility(View.INVISIBLE);
                    llyImgbtnGroup.setVisibility(View.INVISIBLE);
                    llyPausePageBtnGroup.setVisibility(View.VISIBLE);
                    GameManager.getInstance().pauseTheGame();
                    break;
            }
        }
    };

    View.OnClickListener buttonsAtPausePageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // resume
                case R.id.btn_resume_at_pause_page:
                    GameManager.getInstance().resumeTheGame();
                    break;

                // retry
                case R.id.btn_retry_at_pause_page:
                    if(txvArrCountDown != null)
                        GameManager.getInstance().restartTheGame(txvArrCountDown);
                    break;

                // back to menu
                case R.id.btn_back_to_menu_at_pause_page:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };
}
