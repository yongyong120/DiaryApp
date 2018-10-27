package com.example.com.diaryapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.support.constraint.Constraints.TAG;

/*http://webnautes.tistory.com/828
http://hmkcode.com/android-send-json-data-to-server/ 참고
다이어리 추가, 수정, 삭제위한 서버 연결 작업을 한다.
*/
public class DiaryInsert extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {
        //1. php파일을 실행시킬수 있는 주소와 전송할 데이터 준비 (post방식)
        String serverURL = (String)params[0];
        String date = (String)params[1];
        String contents = (String)params[2];
        String time = (String)params[3];
        String json ="";
        /*1. HttpURLConnection 생성*/
        try{
            URL url = new URL(serverURL); //예외 처리 안해주면 빨간줄 생김
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            /*응답,연결시간초과 예외 처리*/
            conn.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
            conn.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
            conn.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
            //json파일에 대한정보 설정 (꼭 해줘야 되는지는 모르겠음)
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            /* build JSON object*/
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.accumulate("date",date);
                jsonObject.accumulate("contents",contents);
                jsonObject.accumulate("time",time);
                Log.d("Diary",date);
                Log.d("Diary",contents);
            }catch(JSONException e)
            {
                return "json error1";
            }
            Log.d("Diary","======End Json Setting!!!!");
            //데이터 전송
            OutputStream os = conn.getOutputStream(); //outputstream을 얻어온다.
            Log.d("DiaryjSon",jsonObject.toString());
            os.write(jsonObject.toString().getBytes());
            os.flush();
            os.close();
            Log.d("Diary","======End Connecting!!!!");
            return conn.getResponseMessage()+"";
        }catch(Exception e)
        {
            Log.d(TAG, "InsertData: Error ", e);
            return new String("Error: " + e.getMessage());
        }
    }
}
