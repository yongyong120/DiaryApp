package org.androidtown.diaryapp1;

public class DiaryItem {
    public String write_date;
    public String contents;

    public DiaryItem(String write_date, String contents) {
        this.write_date = write_date;
        this.contents = contents;
    }

    public String getWrite_date() {

        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
