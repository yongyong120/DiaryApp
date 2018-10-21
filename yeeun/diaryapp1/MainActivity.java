package org.androidtown.diaryapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    ListView diarylist;
    DiaryAdapter dadapter;
    ArrayList<DiaryItem> diaryitemList;
    String myJSON;
    JSONArray infos = null;
    //php 내의 요소의 이름과 일치할 것
    private static final String TAG_RESULTS="result";
    private static final String TAG_DATE = "date";
    private static final String TAG_CONTENTS = "contents"; // name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //다이어리 리스트뷰를 가져옴
        diarylist = (ListView) findViewById(R.id.diarylist);
        //리스트 넣을 다이어리 아이템 리스트 생성
        diaryitemList = new ArrayList<DiaryItem>();
        getData("http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/dtest.php");
     //   diaryitemList.add(new DiaryItem("2018년 10월 10일", "어제는 팀플을 했다. 팀플의 장소는....."));
     //   diaryitemList.add(new DiaryItem("2018년 09월 10일", "어제는 팀플을 했다. 팀플의 장소는....."));

        //다이어리 어댑터에 CONTEXT로 메인화면과(다이어리뷰) 다이어리 아이템 리스트 인자로 넣어줌
     //   dadapter = new DiaryAdapter(MainActivity.this, diaryitemList);

        //다이어리 리스트뷰에 다이어리 어댑터를 넣어줌
       // diarylist.setAdapter(dadapter);

        diarylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DiaryActivity.class);
                /*intent에 data 넘기기. putExtra에는 식별 태그, 다음 화면에 넘길 값이 들어감 */
                intent.putExtra("date",diaryitemList.get(position).getWrite_date());
                intent.putExtra("contents",diaryitemList.get(position).getContents());
                startActivity(intent);
            }
        });
    }
    //서버에서 읽은 정보를 토대로 리스트를 만든다.
    public void showList()
    {
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            infos = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i<infos.length(); i++)
            {
                JSONObject c = infos.getJSONObject(i);
                String date = c.getString(TAG_DATE);
                String contents = c.getString(TAG_CONTENTS);

                diaryitemList.add(new DiaryItem(date,contents));
            }
            //다이어리 어댑터에 CONTEXT로 메인화면과(다이어리뷰) 다이어리 아이템 리스트 인자로 넣어줌
            dadapter = new DiaryAdapter(MainActivity.this, diaryitemList);
            //다이어리 리스트뷰에 다이어리 어댑터를 넣어줌
            diarylist.setAdapter(dadapter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //서버에 올라간 data를 php파일 이용해 가져온다.
    public void getData(String url)
    {
        class GetDataJSON extends AsyncTask<String, Void, String>{
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
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    public void AddButtonClicked(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
        startActivity(intent);
    }
}
