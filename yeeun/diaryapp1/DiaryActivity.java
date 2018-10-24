package org.androidtown.diaryapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DiaryActivity extends AppCompatActivity {
    TextView date;
    TextView contents;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //전 화면에서 넘어온 intent를 받는다.
        Intent intent = getIntent();
        date = (TextView)findViewById(R.id.diary_date);
        contents = (TextView)findViewById(R.id.diary_contents);

        //intent에서 넘어온 값을 전달받는다.
        date.setText(intent.getStringExtra("date")); //intent putExtra에서 준 식별 태그와 같은 이름이여야 한다.
        contents.setText(intent.getStringExtra("contents"));
        time = intent.getStringExtra("time");
        //back button 누를 시
        Button back_button = (Button)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //수정 버튼 누를시
        Button modify_button = (Button)findViewById(R.id.modify_button);
        modify_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DiaryWriteActivity.class);
                Log.d("========Date==", ""+date.getText());
                intent.putExtra("curdate",date.getText().toString());
                intent.putExtra("curcontents",contents.getText().toString());
                intent.putExtra("writetime",time);
                intent.putExtra(BasicInfo.KEY_WRITE_MODE, BasicInfo.MODE_MODIFY);
                startActivity(intent);
                date.setText("");
                contents.setText("");
                finish();
            }
        });
        //삭제 버튼 누를시
        Button delete_button = (Button)findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DiaryInsert task = new DiaryInsert();
                task.execute("http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/ddelete.php",date.getText().toString(),contents.getText().toString(),time);
                finish();
            }
        });



    }
}
