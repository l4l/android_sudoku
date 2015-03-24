package org.sudoku.model;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.
 */
public enum CellMask {
    SHOWED,
    HIDDEN,
    USER_DEFINED;
    public static CellMask getByChar(char c) {
        switch (c) {
            case 'h':
            case 'H':
                return CellMask.HIDDEN;
            case 'u':
            case 'U':
                return CellMask.USER_DEFINED;
            default:
                return CellMask.SHOWED;
        }
    }
}
