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

    private static final int[][] base = new int[][]{
            {1,2,3,4,5,6,7,8,9},
            {4,5,6,7,8,9,1,2,3},
            {7,8,9,1,2,3,4,5,6},
            {2,3,4,5,6,7,8,9,1},
            {5,6,7,8,9,1,2,3,4},
            {8,9,1,2,3,4,5,6,7},
            {3,4,5,6,7,8,9,1,2},
            {6,7,8,9,1,2,3,4,5},
            {9,1,2,3,4,5,6,7,8}
    };

    Context context;
    private int[] cells;
    private CellMask[] mask;
    private SparseIntArray defined;
    private static final int CLOSED_CELLS = 40;

    public CellsAdapter(Context context, int[] cells, CellMask[] mask) {
        this.context = context;
        this.cells = cells;
        defined = new SparseIntArray();
        if (this.cells == null || this.cells.length != LINE_SIZE_S) {
            this.cells = new int[LINE_SIZE_S];
            generateGrid();
        } else {
            this.mask = mask;
            if (this.mask == null || this.mask[0] == null || this.mask.length != LINE_SIZE_S) {
                generateMask();
            }
        }
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

    private void generateMask() {

        mask = new CellMask[LINE_SIZE_S];
        for (int i = 0; i < LINE_SIZE_S; i++)
            mask[i] = CellMask.SHOWED;

        Vector<Integer> a = new Vector<Integer>(81);
        for (int i = 0; i < LINE_SIZE_S; i++)
            a.add(i);

        int t;
        for (int i = 0; i < CLOSED_CELLS; i++) {
            t = (int)(Math.random() * a.size());
            mask[t] = CellMask.HIDDEN;
            a.remove(t);
        }
    }

    public void generateGrid() {
        clearAnswers();
        for (int i = 0; i < LINE_SIZE; i++)
            System.arraycopy(base[i], 0, cells, i * 9, LINE_SIZE);
        for (int i = (int)(Math.random()*10)+100; i > 0; i--) {
            int t = (int)(Math.random()*3);
            int x = (int)(Math.random()*3);
            int y; do {y = (int)(Math.random()*3);} while (y == x);
            int z = (int)(Math.random()*3);
            /*{
                Log.i("Random nums", t + " " + x + " " + y + " " + z);
                int[] tmp = new int[LINE_SIZE];
                Log.i("Array", "####################################");
                for (int j = 0; j < LINE_SIZE; j++) {
                    System.arraycopy(cells, j * LINE_SIZE, tmp, 0, LINE_SIZE);
                    Log.i("Array", Arrays.toString(tmp));
                }
                Log.i("Array", "####################################");
            }*/
            switch (t) {
                case 0:
                    transpose(x + y + z);
                    break;
                case 1:
                    swapRows(z*3 + x, z*3 + y);
                    break;
                case 2:
                    swapColumns(z*3 + x, z*3 + y);
                    break;
                case 3:
                    swapRowBlock(x, y);
                    break;
                case 4:
                    swapColumnBlock(x, y);
                    break;
            }
        }
        generateMask();
    }

    private void transpose(int n) {
        if (n % 2 == 0)
            for (int i = 0; i < LINE_SIZE; i++)
                for (int j = i; j < LINE_SIZE; j++) {
                    int l = i*LINE_SIZE + j;
                    int k = j*LINE_SIZE + i;
                    int t = cells[l];
                    cells[l] = cells[k];
                    cells[k] = t;
                }
        else
            for (int i = 0; i < LINE_SIZE; i++)
                for (int j = LINE_SIZE - i - 1; j >= 0; j--) {
                    int l = i*LINE_SIZE + j;
                    int k = (LINE_SIZE - j - 1)*LINE_SIZE + (LINE_SIZE - i - 1);
                    int t = cells[l];
                    cells[l] = cells[k];
                    cells[k] = t;
                }
    }

    private void swapRows(int r1, int r2) {
        for (int i = 0; i < LINE_SIZE; i++) {
            int l = r1*LINE_SIZE + i;
            int k = r2*LINE_SIZE + i;
            int t = cells[l];
            cells[l] = cells[k];
            cells[k] = t;
        }
    }

    private void swapColumns(int c1, int c2) {
        for (int i = 0; i < LINE_SIZE; i++) {
            int l = i*LINE_SIZE + c1;
            int k = i*LINE_SIZE + c2;
            int t = cells[l];
            cells[l] = cells[k];
            cells[k] = t;
        }
    }

    private void swapRowBlock(int r1, int r2) {
        //TODO
    }

    private void swapColumnBlock(int c1, int c2) {
        //TODO
    }

    /**
     * @return if has error(s) true, else false
     */
    public boolean checkCells() {
        for (int i = 0; i < LINE_SIZE_S; i++) {
            int t = defined.get(i, -1);
            if (t != -1 && t != cells[i])
                return true;
        }
        return false;
    }

    public void clearAnswers() {
        defined.clear();
    }

    @Deprecated
    private boolean checkCell(int i) {
        for (int k = 1; k < LINE_SIZE; k++)
            if (cells[i] == cells[(i + k*LINE_SIZE) % LINE_SIZE_S])
                return true;
        for (int k = i % LINE_SIZE + 1; k != i % LINE_SIZE; k++)

            if (cells[i] == cells[i/9 < (i + k)/9 ? i + k - LINE_SIZE: i + k])
                return true;

        for (int k: getBlock(i))
            if (cells[i] == cells[k])
                return true;

        return false;
    }

    @Deprecated
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
        *
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
