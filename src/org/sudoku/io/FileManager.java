package org.sudoku.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by kitsu.
 * This file is part of AwesomeSudoku in package org.sudoku.io.
 */
public abstract class FileManager {

    protected final File file;

    /**
     * @param file is file for the next operation
     * @throws IOException
     */
    protected FileManager(File file) throws IOException {
        this.file = file;

        if (!file.exists() && !file.createNewFile()) {
            throw new IOException();
        }
    }

    /**
     * @return true if file empty
     */
    public final boolean isEmpty() {
        return file.length() == 0;
    }

    /**
     * Removing file
     * @throws IOException
     */
    protected final void remove() throws IOException {
        if (!file.delete())
            throw new IOException();
    }

    /**
     * Method for stream closing
     * @throws IOException
     */
    public abstract void close() throws IOException;

}
