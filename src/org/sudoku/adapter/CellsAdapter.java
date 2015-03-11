package org.sudoku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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


    private Context context;
    private Game game;

    private boolean clicked = true;

    public static interface Callback {
        public void deed(int a, int b);
    }

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
                        final KeypadDialog dialog = new KeypadDialog(context, new Callback() {
                            @Override
                            public void deed(int a, int b) {
                                game.define(a, b);
                            }
                        });
                        dialog.setCell(t);
                        dialog.show();
                        closure.notifyDataSetChanged();
                    }
                });

            ImageView imageBottom = (ImageView) view
                    .findViewById(R.id.bottomborder);
            ImageView imageRight = (ImageView) view
                    .findViewById(R.id.rightborder);

            if (i % LINE_SIZE != LINE_SIZE-1 &&
                i / LINE_SIZE != LINE_SIZE-1) {
                if (i % 3 == 2)
                    imageRight.setImageResource(R.drawable.black_border);
                else
                    imageRight.setImageResource(R.drawable.gray_border);
                if (i / 3 == 2)
                    imageBottom.setImageResource(R.drawable.black_border);
                else
                    imageBottom.setImageResource(R.drawable.gray_border);
            }

        } else
            if (!clicked && game.checkCell(i))
                view.setBackgroundColor(Color.RED);

        TextView textView = (TextView) view
                .findViewById(R.id.celltxt);
        textView.setText(cell == 0 ? " ": String.valueOf(cell));

        return view;
    }

    public void setUnclicked() {
        this.clicked = false;
    }

}
