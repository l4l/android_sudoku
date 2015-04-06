package org.sudoku.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.sudoku.R;
import org.sudoku.adapter.SimpleCallback;

import static org.sudoku.activity.GameActivity.LINE_SIZE;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class KeypadDialog extends Dialog {

    public KeypadDialog(final Context context, final SimpleCallback callback, boolean isSet) {
        super(context);
        setCanceledOnTouchOutside(true);
        View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.keypad, null);
        setContentView(view);
        GridView gridView = (GridView) view.findViewById(R.id.keys);
        gridView.setNumColumns(LINE_SIZE / 3);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return LINE_SIZE;
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
            public View getView(final int i, View view, ViewGroup viewGroup) {
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (view == null) {
                    view = inflater.inflate(R.layout.cell, null);
                    ((TextView) view
                            .findViewById(R.id.celltxt))
                            .setText(String.valueOf(i + 1));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callback.deed(i);
                            dismiss();
                        }
                    });

                }
                return view;
            }
        });
        Button clear = (Button) view.findViewById(R.id.clearCellBtn);
        clear.setEnabled(isSet);
        if (isSet)
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.deed(LINE_SIZE);
                    dismiss();
                }
            });
    }

}