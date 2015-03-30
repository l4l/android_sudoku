package org.sudoku.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.sql.
 */
public class RecordTable {

    private static final String TABLE_NAME = "Records";
    public static final String[] Titles = {"NAME", "TIME"};

    private static RecordTable INSTANCE = null;

    private final SQLiteDatabase database;
    private final Context context;

    private RecordTable(Context context) {

        this.context = context;

        final DatabaseHelper.TableColumn[] recordsColumns = {
                new DatabaseHelper.TableColumn(Titles[0], "TEXT"),
                new DatabaseHelper.TableColumn(Titles[1], "INTEGER")
        };
        database = new DatabaseHelper(context,
                new DatabaseHelper.TableEntry(TABLE_NAME, recordsColumns)
        ).getWritableDatabase();
        database.query(TABLE_NAME, Titles, null, null, null, null, null);

    }

    public void insertPair(String name, long time) {
        if (name == null || time == -1)
            return;
        ContentValues values = new ContentValues();
        values.put(Titles[0], name);
        values.put(Titles[1], time);
        database.insert(TABLE_NAME, null, values);
    }

    public Pair<String, Long>[] getTop(int len) {
        final Cursor cursor =
                database.rawQuery("SELECT MIN(" + Titles[1] +
                                  ") FROM (SELECT " + Titles[1] +
                                  " FROM " + TABLE_NAME +
                                  " ORDER BY " + Titles[1] +
                                  " DESC LIMIT " + len + ")", null);
        Pair<String, Long>[] pairs = new Pair[cursor.getCount()];
        cursor.moveToFirst();
        String s; long l;
        for (int i = 0; cursor.isAfterLast(); cursor.moveToNext(), ++i) {
            s = cursor.getString(0);
            l = cursor.getLong(1);
            pairs[i] = Pair.create(s, l);
        }

        return pairs;
    }

    public static RecordTable getInstance(Context context) {

        if (INSTANCE != null) {
            return INSTANCE;
        } else if (context == null) {
            return null;
        }

        INSTANCE = new RecordTable(context);
        return INSTANCE;
    }
}
