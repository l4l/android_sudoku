package org.sudoku.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import org.sudoku.R;
import org.sudoku.io.FileReader;

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
        Button btn = (Button) findViewById(R.id.buttonContinue);
        try {
            new FileReader(getFilesDir(), getString(R.string.last_game));
            btn.setEnabled(true);
        } catch (Exception e) {
            btn.setEnabled(false);
        }
    }

    public void continueClick(View view) {
        Intent i = new Intent();
        i.setClass(this, GameActivity.class);
        i.putExtra(getString(R.string.restore), true);
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

