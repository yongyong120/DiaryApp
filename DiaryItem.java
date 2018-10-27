package com.example.com.diaryapp;

public class DiaryItem {
    public String write_date;
    public String write_time;
    public String contents;
    public String userid;

    public DiaryItem(String write_date, String contents, String write_time,String userid) {
        this.write_date = write_date;
        this.contents = contents;
        this.write_time = write_time;
        this.userid = userid;
    }

    public String getWrite_date() {

        return write_date;
    }

    public String getWrite_time()
    {
        return write_time;
    }
    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getContents() {
        return contents;
    }
    public String getUserid() {
        return userid;
    }

    public void setWrite_time(String time){this.write_time =time;}
    public void setContents(String contents) {
        this.contents = contents;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
