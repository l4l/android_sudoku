package org.awesome_sudoku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import android.widget.Toast;
import org.awesome_sudoku.activity.GameActivity;
import org.awesome_sudoku.model.Game;
import org.awesome_sudoku.activity.KeypadDialog;
import org.awesome_sudoku.R;


import static org.awesome_sudoku.activity.GameActivity.LINE_SIZE;
import static org.awesome_sudoku.activity.GameActivity.LINE_SIZE_S;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.adapter.
 */
public class CellsAdapter extends BaseAdapter {


    private final Context context;
    private final Game game;

    private boolean clicked = true;

    public CellsAdapter(Context context, Game game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public int getCount() {
        return LINE_SIZE_S;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int cell = game.getCell(i);

        if (view == null) {

            view = inflater.inflate(R.layout.cell, null);
            final BaseAdapter closure = this;
            final int t = i;
            if (!game.isShowed(t))
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked = true;
                        final KeypadDialog dialog = new KeypadDialog(context, new SimpleCallback() {
                            @Override
                            public void deed(int a) {
                                if (a == LINE_SIZE) {
                                    game.undefine(t);
                                } else if (game.define(t, a)) {
                                    String text;
                                    if (game.checkCells()) {
                                        text = context.getString(R.string.mistake);
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                    } else {
                                        text = context.getString(R.string.won);
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                        final GameActivity activity = (GameActivity) context;
                                        activity.endGame();
                                    }
                                }
                                closure.notifyDataSetChanged();
                                Log.i("Values defining", t + " " + a);
                            }
                        }, game.isUserDefined(t));
                        dialog.show();
                    }
                });

        }

        TextView textView = (TextView) view
                .findViewById(R.id.celltxt);
        textView.setText(cell == 0 ? " ": String.valueOf(cell));
        if (game.isUserDefined(i))
            if (!clicked && game.checkCell(i))
                textView.setTextColor(Color.RED);
            else
                textView.setTextColor(Color.GRAY);
        else
            textView.setTextColor(Color.BLACK);
        return view;
    }

    /**
     * Reset flag clicked
     * Used for the coloring wrong cells
     */
    public void setUnclicked() {
        this.clicked = false;
    }

}
