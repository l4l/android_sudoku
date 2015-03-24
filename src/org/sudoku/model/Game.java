package org.sudoku.model;

import java.util.Vector;

import android.util.SparseIntArray;

import static org.sudoku.activity.GameActivity.LINE_SIZE;
import static org.sudoku.activity.GameActivity.LINE_SIZE_S;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public class Game {
    /**
     * <b>MAGIC PEOPLE! VOODOO PEOPLE!</b>
     */ //TODO: auto-generating on common field size
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

    private int[] cells;
    private CellMask[] mask;
    private SparseIntArray defined;

    private int stillOpened = CLOSED_CELLS;
    /**
     * How much cells hide at the beginning
     */
    public static final int CLOSED_CELLS = 40;

    public Game(int[] cells, CellMask[] mask, SparseIntArray defined) {
        this.cells = cells;
        this.mask = mask;
        this.defined = defined;
        if (cells == null || mask == null || defined == null) {
            this.cells = new int[LINE_SIZE_S];
            this.mask = new CellMask[LINE_SIZE_S];
            this.defined = new SparseIntArray();
            generateGrid();
        }
    }

    public boolean define(int cell, int value) {
        if (cell < 0 || cell >= LINE_SIZE_S || value < 0 || value >= LINE_SIZE)
            return false;
        defined.put(cell, value + 1);
        mask[cell] = CellMask.USER_DEFINED;
        return --stillOpened == 0;
    }

    /**
     * Get representation of cell based on high math
     * @param i cell number
     * @return Value of cell
     */
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

    /**
     * Just generate array of hided cells
     */
    private void generateMask() {

        for (int i = 0; i < LINE_SIZE_S; i++)
            mask[i] = CellMask.SHOWED;

        Vector<Integer> a = new Vector<>(81);
        for (int i = 0; i < LINE_SIZE_S; i++)
            a.add(i);

        int t;
        for (int i = 0; i < CLOSED_CELLS; i++) {
            t = (int)(Math.random() * a.size());
            mask[a.get(t)] = CellMask.HIDDEN;
            a.remove(t);
        }
    }

    /**
     * Generate <u>ultra-random</u> Sudoku grid and automatically hide cells
     */
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

    /**
     * Cell matrix transposing
     * @param n if n dividable by 2 then transpose by <b>main</b> diagonal
     *          else by <b>secondary</b> diagonal
     */
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
        for (int i = 0; i < LINE_SIZE_S; i++)
            if (checkCell(i))
                return true;
        return false;
    }

    /**
     * Checks chosen cell
     * @param i is number of cell
     * @return true if wrong, else correct
     */
    public boolean checkCell(int i) {
         return defined.get(i, cells[i]) != cells[i];
    }

    /**
     * Just clear all user-defined cells
     */
    public void clearAnswers() {
        while (defined.size() > 0) {
            int key = defined.keyAt(0);
            defined.removeAt(0);
            mask[key] = CellMask.HIDDEN;
        }
        stillOpened = CLOSED_CELLS;
    }
}
