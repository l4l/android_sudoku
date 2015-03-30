package org.sudoku.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by kitsu.
 * This file is part of SudokuLab in package org.sudoku.model.
 */
public final class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public static final String DATABASE_NAME = "SAG.db";
    private final TableEntry tableEntry;

    public DatabaseHelper(Context context, TableEntry tableEntry) {
        super(context, DATABASE_NAME, null, VERSION);
        this.tableEntry = tableEntry;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(tableEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: fix maybe
    }

    public static class TableEntry implements BaseColumns {
        public final String TABLE_NAME;
        public final String[] TITLES;
        public  final String SQL_CREATE_TABLE;

        public TableEntry(String name, TableColumn[] types) {
            TABLE_NAME = name;
            TITLES = new String[types.length];
            StringBuilder tempQuery = new StringBuilder("CREATE TABLE ")
                    .append(TABLE_NAME)
                    .append(" (");
            int i = 0;
            for (TableColumn type: types) {
                if (i != 0)
                    tempQuery.append(", \n");

                tempQuery.append(type.getTitle())
                         .append(" ")
                         .append(type.getType());
                TITLES[i++] = type.getTitle();
            }
            tempQuery.append(");");
            SQL_CREATE_TABLE = tempQuery.toString();
        }

    }

    public static class TableColumn {

        private final String title;
        private final String type;

        public TableColumn(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }
    }
}
