package com.magenta.dreamboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "data.db";
    final static int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //DB가 없을 시 동작
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE list(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, purpose TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE buttonList(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, button_action TEXT NOT NULL, list_id INTEGER, FOREIGN KEY(list_id) REFERENCES list(id))");
    }

    //DB_VERSION이 올라갈때마다 호출
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS list");
        onCreate(sqLiteDatabase);
    }
}
