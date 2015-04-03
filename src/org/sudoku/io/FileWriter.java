package org.sudoku.io;

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
        FileOutputStream os = new FileOutputStream(file);
        out = new ObjectOutputStream(os);
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
