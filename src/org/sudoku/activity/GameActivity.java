package org.sudoku.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import org.sudoku.model.CellMask;
import org.sudoku.model.Game;
import org.sudoku.sql.RecordTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class GameActivity extends Activity {

    public static final int LINE_SIZE = 9;
    public static final int LINE_SIZE_S = LINE_SIZE * LINE_SIZE;

    private Game game;

    private long timer = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_game);

        GridView grid = (GridView) findViewById(R.id.grid);

        grid.setNumColumns(LINE_SIZE);

        int[] cells = null;
        CellMask[] mask = null;
        SparseIntArray defined = null;

        File last = new File(getFilesDir(), "lastgame");
        if (last.canRead()) {
            try {
                InputStream in = new FileInputStream(last);
                cells = new int[LINE_SIZE_S];
                mask = new CellMask[LINE_SIZE_S];
                defined = new SparseIntArray();
                for (int i = 0; i < cells.length; ++i)
                    cells[i] = in.read();
                for (int i = 0; i < mask.length; ++i)
                    mask[i] = CellMask.getByNum(in.read());
                while (in.available() != 0)
                    defined.put(in.read(), in.read());

            } catch (Exception e) {
                e.printStackTrace();
                cells = null;
                mask = null;
                defined = null;
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
                adapter.notifyDataSetChanged();
                timer = System.currentTimeMillis();
            }
        });

        //TODO: add adv block
    }

    @Override
    protected void onStop() {
        super.onStop();
        final String filename = "lastgame";
        FileOutputStream out;
        try {
            out = openFileOutput(filename, Context.MODE_PRIVATE);

            int[] cells = game.getCells();
            CellMask[] mask = game.getMask();
            SparseIntArray defined = game.getDefined();

            byte[] data = new byte[cells.length
                    + mask.length + 2*defined.size()];

            int i = 0;
            for (int cell: cells)
                data[i++] = (byte) cell;

            for (CellMask m: mask)
                data[i++] = m.num;

            for (int j = 0; j < defined.size(); ++j) {
                data[i++] = (byte) defined.keyAt(j);
                data[i++] = (byte) defined.valueAt(j);
            }
            out.write(data);
            out.close();
        }catch (IOException e) {
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
        builder.setTitle("You won!");
        final EditText input = new EditText(GameActivity.this);
        builder.setView(input)
                .setPositiveButton("Apply",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insertValues(input.getText().toString());
                    }
                });
        builder.create().show();
    }

    private void insertValues(String name) {
        RecordTable table = RecordTable.getInstance(getBaseContext());
        table.insertPair(name, timer);
        finish();
    }
}
