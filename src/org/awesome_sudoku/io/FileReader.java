package org.awesome_sudoku.io;

import java.io.*;

/**
 * Created by kitsu.
 * This file is part of AwesomeSudoku in package org.sudoku.io.
 */
public class FileReader extends FileManager {

    private final FileInputStream in;
    private final ObjectInputStream input;

    /**
     * @param file is directory
     * @param name is filename
     * @throws IOException
     */
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

    public <T extends Serializable> T getObject() throws IOException {
        try {
            return (T)input.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        remove();
    }
}
