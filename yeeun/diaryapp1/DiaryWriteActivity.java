package com.example.com.diaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class DiaryWriteActivity extends AppCompatActivity {

    TextView writedate;
    EditText writecontent;
    String dMode; //쓰기 모드 담는 변수 (새 일기 쓰기 혹은 기존일기 수정)
    String writetime;
    Date curtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarywrite);
        curtime= new Date();
        Log.d("Cur time!!", BasicInfo.timeFormat.format(curtime));
        //넘어온 값 받기
        writedate = (TextView)findViewById(R.id.d_writeDate);
        writecontent =(EditText)findViewById(R.id.d_writeContents);
        Intent intent = getIntent();
        writedate.setText(intent.getStringExtra("curdate")); //intent putExtra에서 준 식별 태그와 같은 이름이여야 한다.
        writecontent.setText(intent.getStringExtra("curcontents"));
        writetime = intent.getStringExtra("writetime");

        dMode = intent.getStringExtra(BasicInfo.KEY_WRITE_MODE);
        Button button = (Button)findViewById(R.id.cancel_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button button2 = (Button)findViewById(R.id.save_button);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String date = writedate.getText().toString();
                String contents = writecontent.getText().toString();
                DiaryInsert task = new DiaryInsert();
                if(dMode.equals(BasicInfo.MODE_INSERT)) //새 일기 쓰기 모드일때
                    task.execute("http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/dtest2.php",date,contents,BasicInfo.timeFormat.format(curtime));
                else //기존에 있던 일기 수정 모드 일 때
                    task.execute("http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/dmodify.php",date,contents,writetime);
                writedate.setText("");
                writecontent.setText("");
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
