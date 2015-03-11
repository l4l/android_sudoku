package org.sudoku.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.sudoku.R;
import org.sudoku.adapter.CellsAdapter;
import org.sudoku.model.Game;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class GameActivity extends Activity {

    public static final int LINE_SIZE = 9;
    public static final int LINE_SIZE_S = LINE_SIZE * LINE_SIZE;

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        GridView grid = (GridView) findViewById(R.id.grid);

        grid.setNumColumns(LINE_SIZE);
        game = new Game();
        final CellsAdapter adapter = new CellsAdapter(this, game);
        grid.setAdapter(adapter);

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

                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.clearAnswers();
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


}
