package com.example.administrator.dictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int SHOW_RESPONSE=0;

    private TextView textView_result;
    private Button button_transf,button_note;
    private EditText editText_content;

    public static SqliteOperate dbHelper;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_RESPONSE:
                    CharSequence response=Html.fromHtml(msg.obj.toString());
                    textView_result.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_result=(TextView)findViewById(R.id.tv_result);
        button_transf=(Button)findViewById(R.id.bt_transf);
        editText_content=(EditText)findViewById(R.id.edt_content);
        button_note=(Button)findViewById(R.id.bt_note);
        button_transf.setOnClickListener(this);
        button_note.setOnClickListener(this);

        dbHelper=new SqliteOperate(MainActivity.this,"note.db",null,1);
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        String sql="ALTER TABLE note_table ADD COLUMN flag int";
//        db.execSQL(sql);
//        db.close();

    }

    public void connectDict(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {

                    String content=editText_content.getText().toString();
                    URL url=new URL("http://fanyi.youdao.com/openapi.do?keyfrom=dictionaryTestqq&key=416582248&type=data&doctype=json&version=1.1&q="+content);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in=connection.getInputStream();//获取输入字节流
                    BufferedReader buf=new BufferedReader(new InputStreamReader(in));//包装为字符流
                    StringBuilder response=new StringBuilder();
                    String line;
                    //读取
                    while((line=buf.readLine())!=null){
                           response.append(line);
                    }



                    Message message=new Message();
                    message.what=SHOW_RESPONSE;
                    message.obj=getJsonResult(response);
                    handler.sendMessage(message);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(connection!=null){
                    connection.disconnect();
                }
            }

            private String getJsonResult(StringBuilder response) {
                String trans="";
                String word,explains,phonogram;
                explains="";
                phonogram="";
                word=editText_content.getText().toString();
                try {
                    JSONObject jsonObject=new JSONObject(String.valueOf(response));
                    trans+="translation:"+jsonObject.getString("translation")+"<br>";
                    JSONObject basicObject=jsonObject.getJSONObject("basic");
                    trans+="美式:"+basicObject.getString("us-phonetic")+"<br>";
                    trans+="标准:"+basicObject.getString("phonetic")+"<br>";
                    phonogram=basicObject.getString("phonetic");
                    trans+="英式:"+basicObject.getString("uk-phonetic")+"<br>";
                    trans+="<br>explains:<br>";
                    JSONArray expObject=basicObject.getJSONArray("explains");
                    for (int i=0;i<expObject.length();i++){
                        explains+=expObject.get(i)+"<br>";
                    }
                    trans+=explains;
                    trans+= "<br><h1><font color='red'>web:</font></h1>";
                    JSONArray webObject=jsonObject.getJSONArray("web");
                    for (int i=0;i<webObject.length();i++){
                        JSONObject oj=(JSONObject)webObject.get(i);
                        trans+="key: "+oj.getString("key")+"<br>";
                        trans+=oj.getString("value")+"<br>";
                    }

                    SQLiteDatabase db=dbHelper.getWritableDatabase();

                    ContentValues contentValues=new ContentValues();
                    contentValues.put("word",word);
                    contentValues.put("explains",explains);
                    contentValues.put("phonogram",phonogram);
                    contentValues.put("flag",0);
                    db.insert("note_table",null,contentValues);

                    db.close();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return trans;
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_transf:
                connectDict();
                break;
            case R.id.bt_note:
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(intent);
                break;
        }

    }
}
