package com.example.administrator.dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/31.
 */
public class SqliteOperate extends SQLiteOpenHelper {


    public SqliteOperate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE note_table(word VARCHAR(20) PRIMARY KEY NOT NULL,explains VARCHAR(250) ,flag int NOT NULL,phonogram VARCHAR(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql="ALTER TABLE note_table ADD COLUMN flag int";
//        db.execSQL(sql);
    }
}
