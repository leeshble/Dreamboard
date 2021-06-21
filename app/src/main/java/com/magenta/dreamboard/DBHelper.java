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
        //기본 버튼 구성 모음
        sqLiteDatabase.execSQL("CREATE TABLE list(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, purpose TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO list(title, purpose) VALUES('Windows', 'Windows shortcut')");

        //기본 버튼 구성
        sqLiteDatabase.execSQL("CREATE TABLE buttonList(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, button_action TEXT NOT NULL, list_id INTEGER NOT NULL)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Windows', 'win', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Copy', 'Ctrl c', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Paste', 'Ctrl v', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Capture', 'printscreen', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('BrowserBack', 'browserback', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('BrowserFoward', 'browserforward', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('VolumeUp', 'volumeup', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('VolumeDown', 'volumedown', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Mute', 'volumemute', 1)");
        sqLiteDatabase.execSQL("INSERT INTO buttonList(title, button_action, list_id) VALUES('Server End', 'STOP', 1)");

        //설정 테이블
        sqLiteDatabase.execSQL("CREATE TABLE setting(id INTEGER PRIMARY KEY, host TEXT NOT NULL, port INTEGER NOT NULL)");
        sqLiteDatabase.execSQL("INSERT INTO setting(host, port) VALUES('0', 0)");
    }

    //DB_VERSION이 올라갈때마다 호출
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS list");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS buttonList");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS setting");
        onCreate(sqLiteDatabase);
    }
}
