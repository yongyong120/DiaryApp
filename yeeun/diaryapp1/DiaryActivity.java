package org.androidtown.diaryapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //전 화면에서 넘어온 intent를 받는다.
        Intent intent = getIntent();
        TextView date = (TextView)findViewById(R.id.diary_date);
        TextView contents = (TextView)findViewById(R.id.diary_contents);

        date.setText(intent.getStringExtra("date"));
        contents.setText(intent.getStringExtra("contents"));


        Button back_button = (Button)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
