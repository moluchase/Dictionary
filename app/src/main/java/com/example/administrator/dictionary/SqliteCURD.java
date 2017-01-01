package com.example.administrator.dictionary;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/1/1.
 */
public class SqliteCURD {
    private SqliteOperate dbHelper;

    public void InsertSqlite(String word,String explains){
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("word",word);
        contentValues.put("explains",explains);
        db.insert("note_table",null,contentValues);

        db.close();
    }
}
