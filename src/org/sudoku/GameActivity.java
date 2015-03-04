package org.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
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
        grid.setAdapter(new CellsAdapter(
                        this,
                        getIntent().getIntArrayExtra("grid"),
                        mask));


    }


}
