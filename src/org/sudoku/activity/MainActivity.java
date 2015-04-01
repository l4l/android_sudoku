package org.sudoku.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import org.sudoku.R;

import java.io.File;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File last = new File(getFilesDir(), "lastgame");
        if (last.exists() && last.canRead()) {
            Button btn = (Button) findViewById(R.id.buttonContinue);
            btn.setEnabled(true);
        }
    }

    public void continueClick(View view) {
        Intent i = new Intent();
        i.setClass(this, GameActivity.class);
        i.putExtra("resume", true);
        startActivity(i);
    }

    public void newGameClick(View view) {
        Intent i = new Intent();
        i.setClass(this, GameActivity.class);
        startActivity(i);
    }

    public void recordsClick(View view) {
        Intent i = new Intent();
        i.setClass(this, RecordActivity.class);
        startActivity(i);
    }

    public void authorClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.author);
        dialog.setTitle(R.string.author);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}

