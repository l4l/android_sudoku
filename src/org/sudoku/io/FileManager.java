package org.sudoku.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by kitsu.
 * This file is part of AwesomeSudoku in package org.sudoku.io.
 */
public abstract class FileManager {

    protected final File file;

    protected FileManager(File file) throws IOException {
        this.file = file;

        if (!file.exists() && !file.createNewFile()) {
            throw new IOException();
        }
    }

    public void close() throws IOException{
        if (!file.delete())
            throw new IOException();
    }

}
