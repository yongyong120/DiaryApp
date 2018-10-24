package org.androidtown.diaryapp1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class DiaryAdapter extends BaseAdapter {
    Context context;
    ArrayList<DiaryItem> diaryitemList;

    public DiaryAdapter(Context context, ArrayList<DiaryItem> diaryitemList) {
        this.context = context; //context 초기화
        this.diaryitemList = diaryitemList;
    }

    @Override
    public int getCount() {
        return this.diaryitemList.size();
    }

    @Override
    public Object getItem(int position) {
        return diaryitemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //리스트뷰에서 아이템과 xml을 연결하여 화면에 표시해주는 가장 중요한  부분
    //반복문이 여기서 실행된다고 이해하면 편함. 순차적으로 한칸씩 화면을 구성
    public View getView(int position, View convertview, ViewGroup parent) {
        DiaryItemView itemview;
        if(convertview==null) //convertview에 item view를 불러옴
            itemview = new DiaryItemView(context);
        else
            itemview = (DiaryItemView)convertview;
        itemview.setD_itemDate(diaryitemList.get(position).getWrite_date());
        itemview.setD_itemText(diaryitemList.get(position).getContents());
        return itemview;
    }
}


