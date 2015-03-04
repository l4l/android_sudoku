package org.sudoku.adapters;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.sudoku.CellMask;
import org.sudoku.R;

import java.util.Vector;

import static org.sudoku.GameActivity.LINE_SIZE;
import static org.sudoku.GameActivity.LINE_SIZE_S;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.adapters.
 */
public class CellsAdapter extends BaseAdapter {

    Context context;
    private int[] cells;
    private CellMask[] mask;
    private SparseIntArray defined;

    public CellsAdapter(Context context, int[] cells, CellMask[] mask) {
        this.context = context;
        this.cells = cells;
        if (this.cells == null || this.cells.length != LINE_SIZE_S) {
            this.cells = new int[LINE_SIZE_S];
            generateGrid();
        }
        this.mask = mask;
        if (this.mask == null || this.mask.length != LINE_SIZE_S)
            this.mask = new CellMask[LINE_SIZE_S];
        for (int i = 0; i < LINE_SIZE_S; i++) {
            mask[i] = CellMask.SHOWED;
        }
        //TODO: hideCells();
        defined = new SparseIntArray();
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (view == null) {

            gridView = inflater.inflate(R.layout.cell, null);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.celltxt);
            int cell = getCell(i);
            textView.setText(cell == 0 ? " ": String.valueOf(cell));

            ImageView imageBottom = (ImageView) gridView
                    .findViewById(R.id.bottomborder);
            ImageView imageRight = (ImageView) gridView
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

        } else {
            gridView = view;
        }

        return gridView;
    }

    public int getCell(int i) {
        switch (mask[i]) {
            case SHOWED:
                return cells[i];
            case USER_DEFINED:
                return defined.get(i);
            case HIDDEN:
            default:
                return 0;
        }
    }

    public void generateGrid() {
        int u;
        int len = LINE_SIZE_S;
        int[] used = new int[len];
        for(int i = 0; i < len; i++) {
            do {

                do {
                    u = 0;
                    if(used[i] == 511) {
                        used[i] |= 1<<cells[i]-1;
                        used[i] = 0;
                        cells[i] = 0;
                        i -= 2;
                        u = 1;
                        break;
                    }
                    cells[i] = 1 + (int)(Math.random()*9);
                } while((used[i] & 1<<cells[i]-1) > 0);
                if(u == 0)
                    break;
            } while(!checkCell(i) && (used[i] |= 1<<cells[i]-1) != -1);
        }
    }

    public void checkGrid(View view) {
        Vector<Integer> p = new Vector<Integer>();
        for (int i = 0; i < LINE_SIZE_S; i++) {
            if (checkCell(i))
                p.add(i);
        }

        if (p.size() != 0) {
            // TODO: color wrong cells
        }
    }

    /**
     * @return if has error(s) true, else false
     */
    private boolean checkCells() {
        for (int i = 0; i < LINE_SIZE_S; i++) {
                if (checkCell(i))
                    return true;
        }
        return false;
    }

    private boolean checkCell(int i) {
        for (int k = 1; k < LINE_SIZE; k++)
            if (cells[i] == cells[(i + k*LINE_SIZE) % LINE_SIZE_S]) {

                return true;
            }
        for (int k = i % LINE_SIZE + 1; k != i % LINE_SIZE; k++)

            if (cells[i] == cells[i/9 < (i + k)/9 ? i + k - LINE_SIZE: i + k]) {
                return true;
            }

        for (int k: getBlock(i))
            if (cells[i] == cells[k])
                return true;

        return false;
    }

    private int[] getBlock(int i) {
        int block[] = new int[8];
        int xStart = 0, yStart = 0, xEnd, yEnd;

        if (i%LINE_SIZE < LINE_SIZE/3) {
            xEnd = LINE_SIZE/3;
        } else if (i%LINE_SIZE < 2*LINE_SIZE/3) {
            xStart = LINE_SIZE/3;
            xEnd = 2*LINE_SIZE/3;
        } else {
            xStart = 2*LINE_SIZE/3;
            xEnd = LINE_SIZE;
        }
        if (i*3 < LINE_SIZE_S) {
            yEnd = LINE_SIZE/3;
        } else if (i*3 < 2*LINE_SIZE_S) {
            yStart = LINE_SIZE/3;
            yEnd = 2*LINE_SIZE/3;
        } else {
            yStart = 2*LINE_SIZE/3;
            yEnd = LINE_SIZE;
        }
        /*
        * {0,1,2,   9,10,11,    18,19,20}
        * {3,4,5,   12,13,14,   21,22,23}
        * {6,7,8,   15,16,17,   24,25,26}
        * {27,28,29 36,37,38,   45,46,47}
        * */

        for (int t = 0, l = xStart; l < xEnd; l++, t++) {
            for (int k = yStart; k < yEnd; k++, t++) {
                if (l != i%LINE_SIZE && k != i/LINE_SIZE)
                    block[t] = l + k*LINE_SIZE;
            }
        }

        return block;
    }
    
}
