package org.sudoku.model;

import java.util.Random;
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
    private static final int[][] BASE = new int[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {4, 5, 6, 7, 8, 9, 1, 2, 3},
            {7, 8, 9, 1, 2, 3, 4, 5, 6},
            {2, 3, 4, 5, 6, 7, 8, 9, 1},
            {5, 6, 7, 8, 9, 1, 2, 3, 4},
            {8, 9, 1, 2, 3, 4, 5, 6, 7},
            {3, 4, 5, 6, 7, 8, 9, 1, 2},
            {6, 7, 8, 9, 1, 2, 3, 4, 5},
            {9, 1, 2, 3, 4, 5, 6, 7, 8}
    };

    /**
     * How much cells hide at the beginning.
     */
    public static final int CLOSED_CELLS = 40;
    /**
     * Number of non user-defined cells.
     */
    private int stillOpened = CLOSED_CELLS;
    /**
     * Matrix of cells.
     */
    private int[] cells;
    /**
     * Matrix of cells mask.
     */
    private CellMask[] mask;
    /**
     * Array of cells defined by user.
     */
    private SparseIntArray defined;

    /**
     * @param cells matrix of cells
     * @param mask matrix of cells mask
     * @param defined array of cells defined by user
     */
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

    /**
     * Set cell to user-defined.
     * @param c cell number
     * @param v value to replace current
     * @return true if everything is ok
     */
    public boolean define(int c, int v) {
        if (c < 0 || c >= LINE_SIZE_S || v < 0 || v >= LINE_SIZE)
            return false;
        defined.put(c, v + 1);
        mask[c] = CellMask.USER_DEFINED;
        return --stillOpened == 0;
    }

    /**
     * Get representation of cell based on high math.
     * @param c cell number
     * @return value of cell
     */
    public int getCell(int c) {
        switch (mask[c]) {
            case SHOWED:
                return cells[c];
            case USER_DEFINED:
                return defined.get(c);
            case HIDDEN:
            default:
                return 0;
        }
    }

    /**
     * Answer on request of type cell.
     * @param i number of cell
     * @return true if cell defined by user
     */
    public boolean isUserDefined(int i) {
        return i < LINE_SIZE_S && i >= 0 && mask[i] == CellMask.USER_DEFINED;
    }

    /**
     * Generate <u>ultra-random</u> Sudoku grid
     * based on nanotechnology and quantum-mechanic. <br>
     * As bonus automatically hide cells.
     */
    public void generateGrid() {
        clearAnswers();
        for (int i = 0; i < LINE_SIZE; i++)
            System.arraycopy(BASE[i], 0, cells, i * LINE_SIZE, LINE_SIZE);
        final Random random = new Random();
        for (int i = random.nextInt(10) + 100; i > 0; i--) {
            int t = random.nextInt(3);
            int x = random.nextInt(3);
            int y; do {y = random.nextInt(3);} while (y == x);
            int z = random.nextInt(3);
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
     * Checks chosen cell.
     * @param i is number of cell
     * @return true if wrong, else correct
     */
    public boolean checkCell(int i) {
        return defined.get(i, cells[i]) != cells[i];
    }

    /**
     * Just clear all user-defined cells.
     */
    public void clearAnswers() {
        while (defined.size() > 0) {
            int key = defined.keyAt(0);
            defined.removeAt(0);
            mask[key] = CellMask.HIDDEN;
        }
        stillOpened = CLOSED_CELLS;
    }

    public int getStillOpened() {
        return stillOpened;
    }

    public int[] getCells() {
        return cells.clone();
    }

    public CellMask[] getMask() {
        return mask.clone();
    }

    public SparseIntArray getDefined() {
        return defined;
    }

    /**
     * Just generate array of hided cells.
     */
    private void generateMask() {

        for (int i = 0; i < LINE_SIZE_S; i++)
            mask[i] = CellMask.SHOWED;

        Vector<Integer> a = new Vector<>(LINE_SIZE_S);
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
     * Transposing matrix cells.
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

    /**
     * Swap first and second chosen rows in matrix.
     * @param r1 first row
     * @param r2 second row
     */
    private void swapRows(int r1, int r2) {
        for (int i = 0; i < LINE_SIZE; i++) {
            int l = r1*LINE_SIZE + i;
            int k = r2*LINE_SIZE + i;
            int t = cells[l];
            cells[l] = cells[k];
            cells[k] = t;
        }
    }

    /**
     * Swap first and second chosen columns in matrix.
     * @param c1 first column
     * @param c2 second column
     */
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
     * Checks all cells.
     * @return if has error(s) true, else false
     */
    public boolean checkCells() {
        for (int i = 0; i < LINE_SIZE_S; i++)
            if (checkCell(i))
                return true;
        return false;
    }
}
