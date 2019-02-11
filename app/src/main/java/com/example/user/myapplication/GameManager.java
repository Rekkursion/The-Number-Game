package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum GameDiff {
    GAME_DIFF_EASY, GAME_DIFF_NORMAL, GAME_DIFF_HARD, GAME_DIFF_LUNATIC
}

class GameManager {
    private static final GameManager ourInstance = new GameManager();
    private static final String btnBackgroundColorString_general = "#FFE9E9E9";
    private static final String btnTextColorString_general = "#FF000000";
    private static final String btnBackgroundColorString_pressed_correct = "#FF4A4A4A";
    private static final String btnTextColorString_pressed_correct = "#FFFFFFFF";
    private static final String btnBackgroundColorString_pressed_wrong = "#FFDF2B2B";
    private static final String btnTextColorString_pressed_wrong = "#FFFFFFFF";
    private static final long WRONG_CLICK_PENALTY_TIME_SECOND = 10L;
    public static final int REQ_CODE_TO_SCORE_PAGE = 2002;
    public static final int RES_CODE_SOME_ERROR_HAPPENED = 10037;
    public static final String INTENT_EXTRA_SCORE_NAME = "time";
    private Context context;
    private GameActivity gameActivity;
    private List<Button> gameButtonList = new ArrayList<>();
    private GameDiff gameDiff;
    private int btnColumnNum;
    private int scrnWidth, scrnHeight;
    private float btnSize;
    private int currentNumber;
    private RelativeLayout rlyGameButtons;
    private TextView txvTimer, txvPenaltyTime;
    private Runnable timerRunnable;
    private boolean shouldStop, shouldPause;
    private boolean shouldAddWrongClickPenaltyTime;

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        shouldStop = shouldPause = shouldAddWrongClickPenaltyTime = false;
        timerRunnable = new Runnable() {
            long counter = 0;

            @Override
            public void run() {
                txvTimer.setText(String.format("%d.%02d", counter / 100L, counter % 100L));

                ++counter;
                if(shouldAddWrongClickPenaltyTime) {
                    counter += WRONG_CLICK_PENALTY_TIME_SECOND * 100;
                    shouldAddWrongClickPenaltyTime = false;
                }

                if(shouldStop) {
                    counter = 0;
                    txvTimer.setText(R.string.str_timer_default);
                } else if(shouldPause) {

                } else
                    txvTimer.postDelayed(this, 1);
            }
        };
    }

    public void initGameDataAndSetButtons(GameActivity context, Display display, TextView txvTimer, TextView txvPenaltyTime, RelativeLayout rlyGameBtns, GameDiff gameDiff) {
        // context
        this.context = context;
        // GameActivity
        gameActivity = context;
        // game difficulty
        this.gameDiff = gameDiff;
        // column (and also row) number of game buttons
        this.btnColumnNum = this.gameDiff.ordinal() + 3;
        // screen width
        this.scrnWidth = display.getWidth();
        // screen height
        this.scrnHeight = display.getHeight();
        // game button size
        this.btnSize = (float)Math.min(this.scrnWidth, this.scrnHeight) / this.btnColumnNum;
        // current number which should be clicked
        this.currentNumber = 1;
        // game button group layout
        this.rlyGameButtons = rlyGameBtns;
        // timer text view
        this.txvTimer = txvTimer;
        // penalty time text view
        this.txvPenaltyTime = txvPenaltyTime;
        this.txvPenaltyTime.setVisibility(View.GONE);

        setButtons();
    }

    public void countDownThenStartTimer(TextView[] txvArrCountDown) {
        this.rlyGameButtons.setVisibility(View.INVISIBLE);
        gameActivity.llyImgbtnGroup.setVisibility(View.INVISIBLE);

        Animation[] countDownAnims = {
                AnimationUtils.loadAnimation(this.context, R.anim.count_down),
                AnimationUtils.loadAnimation(this.context, R.anim.count_down),
                AnimationUtils.loadAnimation(this.context, R.anim.count_down),
                AnimationUtils.loadAnimation(this.context, R.anim.count_down_go)
        };

        for(Animation anim: countDownAnims) {
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(animation == countDownAnims[0]) {
                        txvArrCountDown[0].setVisibility(View.INVISIBLE);
                        txvArrCountDown[1].setVisibility(View.VISIBLE);
                        txvArrCountDown[1].startAnimation(countDownAnims[1]);
                    } else if(animation == countDownAnims[1]) {
                        txvArrCountDown[1].setVisibility(View.INVISIBLE);
                        txvArrCountDown[2].setVisibility(View.VISIBLE);
                        txvArrCountDown[2].startAnimation(countDownAnims[2]);
                    } else if(animation == countDownAnims[2]) {
                        txvArrCountDown[2].setVisibility(View.INVISIBLE);
                        txvArrCountDown[3].setVisibility(View.VISIBLE);
                        txvArrCountDown[3].startAnimation(countDownAnims[3]);
                        rlyGameButtons.setVisibility(View.VISIBLE);
                        gameActivity.llyImgbtnGroup.setVisibility(View.VISIBLE);
                        startTimer();
                    } else
                        txvArrCountDown[3].setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }

        txvArrCountDown[1].setVisibility(View.INVISIBLE);
        txvArrCountDown[2].setVisibility(View.INVISIBLE);
        txvArrCountDown[3].setVisibility(View.INVISIBLE);

        txvArrCountDown[0].startAnimation(countDownAnims[0]);
    }

    public void endTheGame() {
        shouldStop = true;
        timerRunnable.run();
        stopTimer();
    }

    public void restartTheGame(TextView[] txvArrCountDown) {
        gameActivity.llyPausePageBtnGroup.setVisibility(View.INVISIBLE);
        gameActivity.llyImgbtnGroup.setVisibility(View.VISIBLE);
        gameActivity.rlyGameButtons.setVisibility(View.VISIBLE);

        shouldStop = true;
        timerRunnable.run();
        stopTimer();
        txvTimer.setText(R.string.str_timer_default);
        setButtons();
        countDownThenStartTimer(txvArrCountDown);
    }

    public void resumeTheGame() {
        gameActivity.llyPausePageBtnGroup.setVisibility(View.INVISIBLE);
        gameActivity.llyImgbtnGroup.setVisibility(View.VISIBLE);
        gameActivity.rlyGameButtons.setVisibility(View.VISIBLE);

        startTimer();
    }

    public void pauseTheGame() {
        pauseTimer();
    }

    private void setButtons() {
        gameButtonList.clear();
        this.rlyGameButtons.removeAllViews();
        this.currentNumber = 1;

        // Create a random-ordered number list for game buttons
        List<Integer> btnNumberList = Stream.iterate(1, i -> i + 1).limit(this.btnColumnNum * this.btnColumnNum).collect(Collectors.toList());
        for(int k = 0; k < btnNumberList.size(); ++k) {
            int j = new Random().nextInt(btnNumberList.size());
            int tmp = btnNumberList.get(k);
            btnNumberList.set(k, btnNumberList.get(j));
            btnNumberList.set(j, tmp);
        }

        Rect clickedRegion = new Rect(0, 0, 0, 0);
        // Create game buttons and add them to the layout
        for(int row = 0; row < this.btnColumnNum; ++row) {
            for(int col = 0; col < this.btnColumnNum; ++col) {
                Button btn = new Button(this.context);
                //btn.setText(String.valueOf(((row * this.btnColumnNum) + col) + 1));
                btn.setText(String.valueOf(btnNumberList.get((row * this.btnColumnNum) + col)));
                btn.setTextSize(this.btnSize * 0.13f);
                btn.setBackgroundColor(Color.parseColor(btnBackgroundColorString_general));
                btn.setTextColor(Color.parseColor(btnTextColorString_general));
                btn.setGravity(Gravity.CENTER);
                btn.setLayoutParams(new RelativeLayout.LayoutParams((int)this.btnSize, (int)this.btnSize));
                btn.setX(((col - (this.btnColumnNum / 2.0f)) * this.btnSize) + (this.btnSize / 2.0f));
                btn.setY(((row - (this.btnColumnNum / 2.0f)) * this.btnSize) + (this.btnSize / 2.0f));
                btn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // correct click
                                if(btn.getText().toString().equals(String.valueOf(currentNumber))) {
                                    btn.setBackgroundColor(Color.parseColor(btnBackgroundColorString_pressed_correct));
                                    btn.setTextColor(Color.parseColor(btnTextColorString_pressed_correct));
                                }
                                // wrong click
                                else {
                                    btn.setBackgroundColor(Color.parseColor(btnBackgroundColorString_pressed_wrong));
                                    btn.setTextColor(Color.parseColor(btnTextColorString_pressed_wrong));
                                }
                                clickedRegion.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                                break;

                            case MotionEvent.ACTION_UP:
                                btn.setBackgroundColor(Color.parseColor(btnBackgroundColorString_general));
                                btn.setTextColor(Color.parseColor(btnTextColorString_general));

                                // correct click
                                if(btn.getText().toString().equals(String.valueOf(currentNumber))) {
                                    ++currentNumber;
                                    btn.setVisibility(View.INVISIBLE);

                                    // reach the goal, finish the game
                                    if(currentNumber > btnColumnNum * btnColumnNum) {
                                        endTheGameAndGoToScorePage();
                                    }
                                }
                                // wrong click
                                else {
                                    showAndAddPenaltyTime();
                                }
                                break;

                            case MotionEvent.ACTION_MOVE:
                                // exit the button
                                if(!clickedRegion.contains(clickedRegion.left + (int)motionEvent.getX(), clickedRegion.top + (int)motionEvent.getY())) {
                                    for(int k = 0; k < rlyGameButtons.getChildCount(); ++k) {
                                        View rlyView = rlyGameButtons.getChildAt(k);

                                        if(rlyView instanceof Button) {
                                            Button button = (Button)rlyView;
                                            Rect region = new Rect(rlyView.getLeft(), rlyView.getTop(), rlyView.getRight(), rlyView.getBottom());
                                            // FIXME 2019 02 07: REGION ALWAYS THE SAME

                                            if(region.contains(region.left + (int)motionEvent.getX(), region.top + (int)motionEvent.getY())) {
                                                button.setBackgroundColor(Color.parseColor(btnBackgroundColorString_pressed_correct));
                                                button.setTextColor(Color.parseColor(btnTextColorString_pressed_correct));
                                                clickedRegion.set(region);
                                            } else {
                                                //button.setBackgroundColor(Color.parseColor(btnBackgroundColorString_general));
                                                //button.setTextColor(Color.parseColor(btnTextColorString_general));
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                        return false;
                    }
                });

                gameButtonList.add(btn);
                this.rlyGameButtons.addView(btn);
            } // end of inner for
        } // end of outer for
    }

    private void startTimer() {
        shouldStop = shouldPause = false;
        txvTimer.post(timerRunnable);
    }

    private void stopTimer() {
        shouldStop = true;
    }

    private void pauseTimer() {
        shouldPause = true;
    }

    private void showAndAddPenaltyTime() {
        shouldAddWrongClickPenaltyTime = true;

        Animation showPenaltyAnim = AnimationUtils.loadAnimation(this.context, R.anim.show_penalty_time);
        showPenaltyAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                txvPenaltyTime.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        txvPenaltyTime.setVisibility(View.VISIBLE);
        txvPenaltyTime.startAnimation(showPenaltyAnim);
    }

    private void endTheGameAndGoToScorePage() {
        Intent intent2ScorePage = new Intent(context, ScoreActivity.class);
        intent2ScorePage.putExtra(INTENT_EXTRA_SCORE_NAME, scoreString2Integer());
        endTheGame();
        gameActivity.startActivityForResult(intent2ScorePage, REQ_CODE_TO_SCORE_PAGE);
    }

    private int scoreString2Integer() {
        return scoreString2Integer(txvTimer.getText().toString());
    }

    public static int scoreString2Integer(String scoreString) {
        int dotIdx = scoreString.indexOf('.');

        if(dotIdx == -1)
            return Integer.valueOf(scoreString) * 100;
        return (Integer.valueOf(scoreString.substring(0, dotIdx)) * 100) + Integer.valueOf(scoreString.substring(dotIdx + 1));
    }
}
