package org.sudoku.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.sudoku.R;
import org.sudoku.sql.RecordTable;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.activity.
 */
public class RecordActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_top);
        final int recordsSize = 10;

        final Pair<String, Long>[] top =
                RecordTable.getInstance(getBaseContext())
                        .getTop(recordsSize);
        if (top == null) {
            final TextView textView = (TextView) findViewById(R.id.errorText);
            textView.setText("Недостаточно результатов!");
            return;
        }
        ListView listView = (ListView) findViewById(R.id.topView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return top.length;
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
                if (view == null) {
                    String s = top[i].first + "   "
                            + (top[i].second / 1000.)
                            + " sec.";
                    TextView textView = new TextView(getBaseContext());
                    textView.setText(s);
                    return textView;
                }
                return view;
            }
        });
    }
}
