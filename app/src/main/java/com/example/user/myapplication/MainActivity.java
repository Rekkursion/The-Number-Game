package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE_TO_GAME_PAGE = 4731;

    Button btnStartEasy = null;
    Button btnStartNormal = null;
    Button btnStartHard = null;
    Button btnStartLunatic = null;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case REQ_CODE_TO_GAME_PAGE:
                if(resultCode == GameManager.RES_CODE_SOME_ERROR_HAPPENED)
                    Toast.makeText(context, "Some error happened.", Toast.LENGTH_SHORT).show();
                break;
        }

        btnStartEasy.setEnabled(true);
        btnStartNormal.setEnabled(true);
        btnStartHard.setEnabled(true);
        btnStartLunatic.setEnabled(true);
    }

    private void initViews() {
        View.OnClickListener onStartBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartEasy.setEnabled(false);
                btnStartNormal.setEnabled(false);
                btnStartHard.setEnabled(false);
                btnStartLunatic.setEnabled(false);

                Intent intent2GamePage = new Intent(context, GameActivity.class);

                switch(view.getId()) {
                    case R.id.btn_start_easy:
                        intent2GamePage.putExtra("diff", GameDiff.GAME_DIFF_EASY.ordinal());
                        break;

                    case R.id.btn_start_normal:
                        intent2GamePage.putExtra("diff", GameDiff.GAME_DIFF_NORMAL.ordinal());
                        break;

                    case R.id.btn_start_hard:
                        intent2GamePage.putExtra("diff", GameDiff.GAME_DIFF_HARD.ordinal());
                        break;

                    case R.id.btn_start_lunatic:
                        intent2GamePage.putExtra("diff", GameDiff.GAME_DIFF_LUNATIC.ordinal());
                        break;
                }

                startActivityForResult(intent2GamePage, REQ_CODE_TO_GAME_PAGE);
            }
        };

        btnStartEasy = findViewById(R.id.btn_start_easy);
        btnStartEasy.setOnClickListener(onStartBtnClickListener);

        btnStartNormal = findViewById(R.id.btn_start_normal);
        btnStartNormal.setOnClickListener(onStartBtnClickListener);

        btnStartHard = findViewById(R.id.btn_start_hard);
        btnStartHard.setOnClickListener(onStartBtnClickListener);

        btnStartLunatic = findViewById(R.id.btn_start_lunatic);
        btnStartLunatic.setOnClickListener(onStartBtnClickListener);
    }
}
