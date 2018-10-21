package org.androidtown.diaryapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiaryItemView extends LinearLayout {
    TextView d_itemDate;
    TextView d_itemText;

    public DiaryItemView(Context context) {
        super(context);
       // LayoutInflater.from(context).inflate(R.layout.diary_item,null);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.diary_item,this,true);

        d_itemDate = (TextView)findViewById(R.id.d_itemDate);
        d_itemText = (TextView)findViewById(R.id.d_itemText);
    }

    public void setD_itemDate(String date)
    {
        d_itemDate.setText(date);
    }
    public void setD_itemText(String text) {
        d_itemText.setText(text);
    }
}
