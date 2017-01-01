package com.example.administrator.dictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class NoteActivity extends Activity implements View.OnClickListener {

    private SqliteOperate sqliteOperate;
    private ArrayList<WordInfo> arrayList,arrayList2;

    private ArrayAdapter<WordInfo> arrayAdapter;

    private  ListView listView;
    private Button button_time,button_click;

    private int prepostion=0;
    private long pretime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        button_click=(Button)findViewById(R.id.bt_click);
        button_time=(Button)findViewById(R.id.bt_time);
        button_click.setOnClickListener(this);
        button_time.setOnClickListener(this);

        Log.i("Main","start");
//        arrayList=new ArrayList<>();
//        arrayList2=new ArrayList<>();
//
//        sqliteOperate=new SqliteOperate(NoteActivity.this,"note.db",null,1);
//        SQLiteDatabase db=sqliteOperate.getReadableDatabase();
//     //   Cursor cursor = db.query("note_table", null, null, null, null, "flag DESC", null);
//        Cursor cursor=db.rawQuery("SELECT * FROM note_table ORDER BY flag DESC",null);
//        while(cursor.moveToNext()){
//            String word=cursor.getString(cursor.getColumnIndex("word"));
//            String explains=cursor.getString(cursor.getColumnIndex("explains"));
//            String phonogram=cursor.getString(cursor.getColumnIndex("phonogram"));
//            int flag=cursor.getInt(cursor.getColumnIndex("flag"));
//            WordInfo wordInfo=new WordInfo(word,explains,phonogram,flag);
//            arrayList.add(wordInfo);
//        }
//        db.close();
//
//        for(int i=arrayList.size();i>0;i--){
//            arrayList2.add(arrayList.get(i-1));
//        }
//
//        arrayAdapter=new WordAdapter(NoteActivity.this,R.layout.simple_item,arrayList2);
//        listView=(ListView)findViewById(R.id.listview);
//        listView.setAdapter(arrayAdapter);


        setAdapter(1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordInfo wi=arrayList2.get(position);
                long time=System.currentTimeMillis();
                if(position==prepostion&&Math.abs(time-pretime)<500){
                    //双击事件
                    SQLiteDatabase db=MainActivity.dbHelper.getWritableDatabase();
                    String sql="DELETE FROM note_table WHERE word=?";
                    db.execSQL(sql,new Object[]{wi.getWord()});
                    db.close();

                    arrayList2.remove(position);
                    arrayAdapter.notifyDataSetChanged();
//                    listView.setAdapter(arrayAdapter);
//                    Toast.makeText(NoteActivity.this,"双击事件"+position,Toast.LENGTH_SHORT).show();
                }else{
                    //单击事件
                    SQLiteDatabase db=MainActivity.dbHelper.getWritableDatabase();
                    String sql="UPDATE note_table SET flag=? WHERE word=?";
                    int flag=wi.getFlag()+1;
                    wi.setFlag(flag);
                    String word=wi.getWord();
                    db.execSQL(sql, new Object[]{flag, word});
                    db.close();
//                    Toast.makeText(NoteActivity.this,"单击事件"+position,Toast.LENGTH_SHORT).show();
                }

                pretime=time;
                prepostion=position;
            }
        });
    }

    private void setAdapter(int b){
        //
        arrayList=new ArrayList<>();
        arrayList2=new ArrayList<>();

        SQLiteDatabase db=MainActivity.dbHelper.getReadableDatabase();
        //   Cursor cursor = db.query("note_table", null, null, null, null, "flag DESC", null);
        Cursor cursor;
        if(b==1) {
            cursor = db.rawQuery("SELECT * FROM note_table ORDER BY flag DESC", null);
        }else{
            cursor = db.rawQuery("SELECT * FROM note_table", null);
        }
        while(cursor.moveToNext()){
            String word=cursor.getString(cursor.getColumnIndex("word"));
            String explains=cursor.getString(cursor.getColumnIndex("explains"));
            String phonogram=cursor.getString(cursor.getColumnIndex("phonogram"));
            int flag=cursor.getInt(cursor.getColumnIndex("flag"));
            WordInfo wordInfo=new WordInfo(word,explains,phonogram,flag);
            arrayList.add(wordInfo);
        }
        db.close();

        for(int i=arrayList.size();i>0;i--){
            arrayList2.add(arrayList.get(i-1));
        }

        arrayAdapter=new WordAdapter(NoteActivity.this,R.layout.simple_item,arrayList2);
        listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_click:
                setAdapter(1);
                Log.i("Main","btclick");
                break;
            case R.id.bt_time:
                setAdapter(0);
                Log.i("Main","bttime");
        }
    }
}
