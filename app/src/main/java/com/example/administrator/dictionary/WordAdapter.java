package com.example.administrator.dictionary;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/1.
 */
//自定义arrayAdapter
public class WordAdapter extends ArrayAdapter<WordInfo> {

    private int resourceId;
    //传入文本，子布局，List列表
    public WordAdapter(Context context, int resource, List<WordInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WordInfo wordInfo=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView textView_word=(TextView)view.findViewById(R.id.text_word);
        TextView textView_explains=(TextView)view.findViewById(R.id.text_explains);
        textView_word.setText(Html.fromHtml(wordInfo.getWord()+"    <font color='#000000'>["+wordInfo.getPhonogram()+"]   "+"("+wordInfo.getFlag()+")</font>"));
        textView_explains.setText(Html.fromHtml(wordInfo.getExplains()));
        return view;
    }
}
