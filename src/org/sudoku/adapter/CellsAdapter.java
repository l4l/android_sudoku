package org.sudoku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import org.sudoku.activity.GameActivity;
import org.sudoku.model.Game;
import org.sudoku.custom.KeypadDialog;
import org.sudoku.R;


import static org.sudoku.activity.GameActivity.LINE_SIZE;
import static org.sudoku.activity.GameActivity.LINE_SIZE_S;

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
            if (cell == 0)
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked = true;
                        final KeypadDialog dialog = new KeypadDialog(context, new SimpleCallback() {
                            @Override
                            public void deed(int a) {
                                if (game.define(t, a)) {
                                    String text;
                                    if (game.checkCells()) {
                                        text = "Check again, you have mistake(s)";
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                    } else {
                                        text = "You won!";
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                        final GameActivity activity = (GameActivity) context;
                                        activity.stopTimer();
                                    }
                                }
                                closure.notifyDataSetChanged();
                                Log.i("Values defining", t + " " + a);
                            }
                        });
                        dialog.show();
                    }
                });

        } /*else
            if (!clicked && game.checkCell(i))
                view.setBackgroundColor(Color.RED);
            else
                view.setBackgroundColor(Color.WHITE);*/


        ImageView imageBottom = (ImageView) view
                .findViewById(R.id.bottomborder);
        ImageView imageRight = (ImageView) view
                .findViewById(R.id.rightborder);
        Log.i("Iters", String.valueOf(i));
        if (i % 3 == 2) {
            imageRight.setImageResource(R.drawable.black_border_right);
            Log.i("Iters-black-rgt", String.valueOf(i));
        } else {
            imageRight.setImageResource(R.drawable.gray_border_right);
            Log.i("Iters-gray-rgt", String.valueOf(i));
        }
        if (i / LINE_SIZE % 3 == 2) {
            imageBottom.setImageResource(R.drawable.black_border_bottom);
            Log.i("Iters-black-btm", String.valueOf(i));
        } else {
            imageBottom.setImageResource(R.drawable.gray_border_bottom);
            Log.i("Iters-gray-btm", String.valueOf(i));
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

    public void setUnclicked() {
        this.clicked = false;
    }

}
