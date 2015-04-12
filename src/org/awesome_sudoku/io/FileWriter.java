package org.awesome_sudoku.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by kitsu.
 * This file is part of AwesomeSudoku in package org.sudoku.io.
 */
public class FileWriter extends FileManager {

    private final ObjectOutputStream out;

    public FileWriter(File file, String name) throws IOException {
        this(new File(file, name));
    }

    public FileWriter(File file) throws IOException {
        super(file);
        if (!file.canWrite()) {
            throw new IOException();
        }
        out = new ObjectOutputStream(new FileOutputStream(file));
    }

    public <T extends Serializable> void write(T data) throws IOException {
        out.writeObject(data);
    }

    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }
}
