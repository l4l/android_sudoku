package org.sudoku.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by kitsu.
 * This file is part of AwesomeSudoku in package org.sudoku.io.
 */
public class FileReader extends FileManager {

    private final FileInputStream in;
    private final ObjectInputStream input;

    public FileReader(File file, String name) throws IOException {
        this(new File(file, name));
    }

    public FileReader(File file) throws IOException {
        super(file);
        if (!file.canRead()) {
            throw new IOException();
        }
        in = new FileInputStream(file);
        input = new ObjectInputStream(in);
    }

    public int[] getIntArray(int size) throws IOException {
        int[] array = new int[size];

        for (int i = 0; i < array.length && in.available() != 0; ++i)
            array[i] = in.read();

        return array;
    }

    public <T> void getObject(T t) throws IOException {
        try {
            t = (T)input.readObject();
        } catch (ClassNotFoundException e) {
            t = null;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        in.close();
    }
}
