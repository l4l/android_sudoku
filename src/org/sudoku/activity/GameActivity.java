package org.sudoku.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.sudoku.R;
import org.sudoku.adapter.CellsAdapter;
import org.sudoku.model.CellMask;
import org.sudoku.model.Game;
import org.sudoku.sql.RecordTable;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class GameActivity extends Activity {

    public static final int LINE_SIZE = 9;
    public static final int LINE_SIZE_S = LINE_SIZE * LINE_SIZE;

    private Game game;

    private long timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_game);

        GridView grid = (GridView) findViewById(R.id.grid);

        grid.setNumColumns(LINE_SIZE);

        int[] cells = null;
        CellMask[] mask = null;
        SparseIntArray defined = null;

        //FIXME change to database queries
        if (savedInstanceState != null) {
            cells = savedInstanceState.getIntArray("cells");
            int i = 0;
            mask = new CellMask[LINE_SIZE_S];
            for (char c: savedInstanceState.getCharArray("mask"))
                mask[i++] = CellMask.getByChar(c);
            int[] k = savedInstanceState.getIntArray("defined-key");
            int[] v = savedInstanceState.getIntArray("defined-val");
            if (k.length == v.length) {
                defined = new SparseIntArray();
                for (i = 0; i < k.length; i++) {
                    defined.put(k[i], v[i]);
                }
            }
        }

        game = new Game(cells, mask, defined);

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
                if (game.checkCells()) {
                    text = "Wrong";
                    adapter.setUnclicked();
                } else
                    text = "Correct";
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
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
                adapter.notifyDataSetChanged();
            }
        });

        //TODO: add adv block
    }


    public void stopTimer() {
        long t = System.currentTimeMillis();
        Intent resultIntent = new Intent();
        //TODO: ask name
        String name = "Kitsu";
        resultIntent.putExtra(RecordTable.Titles[0], name);
        resultIntent.putExtra(RecordTable.Titles[1], t-timer);
        setResult(Activity.RESULT_OK, resultIntent);
        timer = t;
        finish();
    }
}
