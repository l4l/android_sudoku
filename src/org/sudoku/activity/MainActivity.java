package org.sudoku.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.sudoku.R;
import org.sudoku.sql.RecordTable;

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

    public void continueClick(View view) {
        Intent i = new Intent();
        i.setClass(this, GameActivity.class);
        i.putExtra("", new byte[GameActivity.LINE_SIZE_S]);
        startActivity(i);
    }

    public void newGameClick(View view) {
        Intent i = new Intent();
        i.setClass(this, GameActivity.class);
        startActivityForResult(i, 18);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 18:
                RecordTable table = RecordTable.getInstance(getBaseContext());
                String name = data.getStringExtra(RecordTable.Titles[0]);
                long time = data.getLongExtra(RecordTable.Titles[1], -1);
                table.insertPair(name, time);
                break;
            default:
        }
    }
}

