package org.awesome_sudoku.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.sql.
 */
public final class RecordTable {

    private static final String TABLE_NAME = "Records";
    private static final String[] TITLES = {"NAME", "TIME"};

    private static RecordTable instance = null;

    private final SQLiteDatabase database;

    private RecordTable(Context context) {

        final DatabaseHelper.TableColumn[] recordsColumns = {
                new DatabaseHelper.TableColumn(getTitle(0), "TEXT"),
                new DatabaseHelper.TableColumn(getTitle(1), "INTEGER")
        };
        database = new DatabaseHelper(context,
                new DatabaseHelper.TableEntry(TABLE_NAME, recordsColumns)
        ).getWritableDatabase();
    }

    public void insertPair(String name, long time) {
        if (name == null || time == -1)
            return;
        ContentValues values = new ContentValues();
        values.put(getTitle(0), name);
        values.put(getTitle(1), time);
        database.insert(TABLE_NAME, null, values);
    }

    public Pair<String, Long>[] getTop(int len) {
        String query = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY -" + getTitle(1)
                + " DESC LIMIT " + len + ";";
        Cursor cursor;
        try {
            cursor = database.rawQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null; // It'll be better to replace it with Runtime Exception
        }
        if (cursor.getCount() == 0) {
            return null;
        }
        Pair<String, Long>[] pairs = new Pair[cursor.getCount()];
        cursor.moveToFirst();
        String s; long l;
        for (int i = 0; !cursor.isAfterLast(); cursor.moveToNext(), ++i) {
            s = cursor.getString(0);
            l = cursor.getLong(1);
            pairs[i] = Pair.create(s, l);
        }
        cursor.close();

        return pairs;
    }

    public static RecordTable getInstance(Context context) {

        if (instance != null) {
            return instance;
        } else if (context == null) {
            return null;
        }

        instance = new RecordTable(context);
        return instance;
    }

    public static String getTitle(int i) {
        return i >= 0 && i < TITLES.length ? TITLES[i] : null;
    }
}
