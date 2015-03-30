package org.sudoku.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.sudoku.R;
import org.sudoku.model.DatabaseHelper;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.
 */
public class MainActivity extends Activity {

    SQLiteDatabase database;

    final String[] Titles = {"NAME", "TIME"};

    final DatabaseHelper.TableColumn[] recordsColumns = {
            new DatabaseHelper.TableColumn(Titles[0], "TEXT"),
            new DatabaseHelper.TableColumn(Titles[1], "INTEGER")
    };

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
        startActivity(i);
    }

    public void recordsClick(View view) {
        try {
            database = new DatabaseHelper(getBaseContext(),
                    new DatabaseHelper.TableEntry("Records", recordsColumns))
                    .getWritableDatabase();
        } catch (Exception e) {
            Log.i("Excep", e.getMessage());
        }

        database.query("Records", Titles, null, null, null, null, null);

    }

    public void authorClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.author);
        dialog.setTitle(R.string.author);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}

