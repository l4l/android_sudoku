package org.awesome_sudoku.model;

import java.io.Serializable;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public enum CellMask implements Serializable {
    HIDDEN(0),
    USER_DEFINED(1),
    SHOWED(2);

    public final byte num;

    CellMask(int i) {
        num = (byte) i;
    }

    public static CellMask getByNum(int i) {
        switch (i) {
            case 0:
                return HIDDEN;
            case 1:
                return USER_DEFINED;
            case 2:
                return SHOWED;
            default:
                return null;
        }
    }
}
