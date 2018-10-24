package com.example.com.diaryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class DiaryFragment extends Fragment {
    Calendar calendar= Calendar.getInstance();
    FloatingActionButton addButton;
    ListView diarylist;
    DiaryAdapter dadapter;
    ArrayList<DiaryItem> diaryitemList;
    String myJSON;
    JSONArray infos = null;
    //php 내의 요소의 이름과 일치할 것
    private static final String TAG_RESULTS="result";
    private static final String TAG_DATE = "date";
    private static final String TAG_CONTENTS = "contents"; // name
    private static final String TAG_TIME = "time";
    @Nullable
    @Override//프래그먼트와 관련된 뷰 계층을 만들어서 반환한다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_diary_main,container,false);
        //다이어리 리스트뷰를 가져옴
        diarylist = (ListView)rootView.findViewById(R.id.diarylist);
        addButton = (FloatingActionButton) rootView.findViewById(R.id.addButton);
        //리스트 넣을 다이어리 아이템 리스트 생성
        diaryitemList = new ArrayList<DiaryItem>();
        diarylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DiaryActivity.class);
                /*intent에 data 넘기기. putExtra에는 식별 태그, 다음 화면에 넘길 값이 들어감 */
                intent.putExtra("date", diaryitemList.get(position).getWrite_date());
                intent.putExtra("contents",diaryitemList.get(position).getContents());
                intent.putExtra("time",diaryitemList.get(position).getWrite_time());
                startActivity(intent);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiaryWriteActivity.class);
                intent.putExtra("curdate",BasicInfo.dateFormat.format(calendar.getTime()));
                intent.putExtra(BasicInfo.KEY_WRITE_MODE,BasicInfo.MODE_INSERT);
                startActivityForResult(intent,BasicInfo.REQ_INSERT_ACTIVITY);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        //서버에 올라간 데이터를 읽고 리스트 업데이트 한다.
        getData("http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/dtest.php");
        super.onStart();
    }
    @Override /*intent로 띄운 activity가 다시 결과값을 돌려줄 때 그에 따른 처리해주는 함수*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) //intent를 띄울때 보낸 requestCode 에 따라 처리한다.
        {
            case BasicInfo.REQ_INSERT_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {
                    dadapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /*서버에서 읽은 정보를 토대로 리스트를 만든다.*/
    public void showList()
    {
        diaryitemList.clear(); //이거 안해주면 데이터가 중복으로 들어간다.
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            infos = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<infos.length(); i++)
            {
                JSONObject c = infos.getJSONObject(i);
                String date = c.getString(TAG_DATE);
                String contents = c.getString(TAG_CONTENTS);
                String time = c.getString(TAG_TIME);

                diaryitemList.add(new DiaryItem(date,contents,time));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //다이어리 어댑터에 CONTEXT로 메인화면과(다이어리뷰) 다이어리 아이템 리스트 인자로 넣어줌
        dadapter = new DiaryAdapter(getContext(), diaryitemList);
        //다이어리 리스트뷰에 다이어리 어댑터를 넣어줌
        diarylist.setAdapter(dadapter);
    }
    /*서버에 올라간 data를 php파일 이용해 가져온다.*/
    public void getData(String url)
    {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n"); // json으로 array 받아오기
                    }
                    bufferedReader.close();
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }

            @Override /*doinbackground가 끝나면 실행되는 함수*/
            protected void onPostExecute(String result) {
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON(); //위 클래스 객체 만들고
        g.execute(url); //실행하면서 url 전달
    }
}
