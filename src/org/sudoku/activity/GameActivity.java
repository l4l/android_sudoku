package org.sudoku.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import org.sudoku.R;
import org.sudoku.adapter.CellsAdapter;
import org.sudoku.io.FileReader;
import org.sudoku.io.FileWriter;
import org.sudoku.model.CellMask;
import org.sudoku.model.Game;
import org.sudoku.io.RecordTable;

import java.io.IOException;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class GameActivity extends Activity {

    public static final int LINE_SIZE = 9;
    public static final int LINE_SIZE_S = LINE_SIZE * LINE_SIZE;

    private static final int DEFAULT_CHECKS_NUMBER = 3;

    private Game game;

    private long timer = -1;

    private int checksLeft = DEFAULT_CHECKS_NUMBER;

    private GridView grid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_game);
    }

    @Override
    protected void onResume() {
        super.onResume();

        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(LINE_SIZE);

        int[] cells = null;
        CellMask[] mask = null;
        SparseIntArray defined = null;

        if (getIntent().getBooleanExtra(getString(R.string.restore), false))

            try {
                FileReader reader = new FileReader(getFilesDir(),
                        getString(R.string.last_game));

                cells = reader.getObject();

                mask = reader.getObject();


                defined = new SparseIntArray();
                int[] tmp = reader.getObject();
                int j = 0, key = 0, value;
                for (int i: tmp) {
                    if (j == 0) {
                        key = i;
                    } else {
                        value = i;
                        defined.put(key, value);
                    }
                    j = (j+1) % 2;
                }

            } catch (Exception e) {
                e.printStackTrace();
                cells = null;
                mask = null;
                defined = null;
            }

        game = new Game(cells, mask, defined);
        setAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            FileWriter writer = new FileWriter(getFilesDir(),
                    getString(R.string.last_game));

            int[] cells = game.getCells();
            CellMask[] mask = game.getMask();
            SparseIntArray defined = game.getDefined();


            int i = 0;
            writer.write(cells);
            writer.write(mask);

            int[] data = new int[2*defined.size()];

            for (int j = 0; j < defined.size(); ++j) {
                data[i++] = defined.keyAt(j);
                data[i++] = defined.valueAt(j);
            }
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopTimer() {
        if (timer == -1)
            return;

        long t = System.currentTimeMillis();
        timer = t - timer;
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(getString(R.string.won));
        final EditText input = new EditText(GameActivity.this);
        builder.setView(input)
                .setPositiveButton(getString(R.string.apply),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insertValues(input.getText().toString());
                    }
                });
        builder.create().show();
    }

    private void setAdapter() {

        final CellsAdapter adapter = new CellsAdapter(this, game);
        grid.setAdapter(adapter);

        timer = System.currentTimeMillis();

        Button checkBtn = (Button) findViewById(R.id.btnCheck);
        Button clearBtn = (Button) findViewById(R.id.btnClear);
        Button genBtn = (Button) findViewById(R.id.btnGenerate);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;
                if (checksLeft <= 0) {
                    text = getString(R.string.exhaustion_check);
                } else if (game.checkCells()) {
                    adapter.setUnclicked();
                    text = getString(R.string.wrong);
                } else
                    text = getString(R.string.correct);

                checksLeft = checksLeft <= 0 ? 0: checksLeft - 1;
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),
                        text, Toast.LENGTH_SHORT).show();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.clearAnswers();
                adapter.notifyDataSetChanged();
            }
        });
        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.generateGrid();
                setAdapter();
                adapter.notifyDataSetChanged();
                timer = System.currentTimeMillis();
                checksLeft = DEFAULT_CHECKS_NUMBER;
            }
        });
    }

    private void insertValues(String name) {
        RecordTable table = RecordTable.getInstance(getBaseContext());
        table.insertPair(name, timer);
        finish();
    }
}
