package org.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import org.sudoku.adapters.CellsAdapter;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class GameActivity extends Activity {

    public static final int LINE_SIZE = 9;
    public static final int LINE_SIZE_S = LINE_SIZE * LINE_SIZE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        GridView grid = (GridView) findViewById(R.id.grid);

        CellMask mask[] = new CellMask[LINE_SIZE_S];
        int[] m = getIntent().getIntArrayExtra("mask");
        if (m != null && m.length == LINE_SIZE_S)
            for (int i = 0; i < LINE_SIZE_S; i++)
                mask[i] = CellMask.values()[m[i]];
        grid.setNumColumns(LINE_SIZE);
        final CellsAdapter adapter = new CellsAdapter(
                this,
                getIntent().getIntArrayExtra("grid"),
                mask);
        grid.setAdapter(adapter);

        Button checkBtn = (Button) findViewById(R.id.btnCheck);
        Button clearBtn = (Button) findViewById(R.id.btnClear);
        Button genBtn = (Button) findViewById(R.id.btnGenerate);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = adapter.checkCells() ? "Wrong": "Correct";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearAnswers();
            }
        });
        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.generateGrid();
                adapter.notifyDataSetChanged();
            }
        });
    }


}
