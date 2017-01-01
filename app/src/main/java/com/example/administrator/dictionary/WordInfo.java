package com.example.administrator.dictionary;

/**
 * Created by Administrator on 2017/1/1.
 */
public class WordInfo {
    private String word;
    private String explains;
    private int flag;
    private String phonogram;

    public WordInfo(String word, String explains,String phonogram,int flag) {
        this.word = word;
        this.explains = explains;
        this.phonogram=phonogram;
        this.flag=flag;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPhonogram() {
        return phonogram;
    }

    public void setPhonogram(String phonogram) {
        this.phonogram = phonogram;
    }
}
