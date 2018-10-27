package com.example.com.diaryapp;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MemoActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private long mMemoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        mTitleEditText = findViewById(R.id.title_edit);
        mContentsEditText = findViewById(R.id.contents_edit);

        Intent intent = getIntent();

        // 수정 일어나는 경우
        if(intent != null){
            mMemoId = intent.getLongExtra("id", -1);
            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");

            mTitleEditText.setText(title);
            mContentsEditText.setText(contents);
        }
    }

    // 뒤로 갈 때 저장
    public void onBackPressed() {
        // 저장할 데이터
        String title = mTitleEditText.getText().toString();
        String contents = mContentsEditText.getText().toString();

        // 키와, 데이터를 ContentValues 에 넣어 db에 넣는다.
        ContentValues contentValues = new ContentValues();
        contentValues.put(memoContract.MemoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(memoContract.MemoEntry.COLUMN_NAME_CONTENTS, contents);

        // db 객체
        SQLiteDatabase db = MemoDbHelper.getsInstance(this).getWritableDatabase();

        // 메모 저장, 삽입
        if(mMemoId == -1)
        {
            long newRowId = db.insert(memoContract.MemoEntry.TABLE_NAME,
                    null,
                    contentValues);
            if(newRowId == -1){
                Toast.makeText(this, "저장에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }
        else // 메모 수정
        {
            int count = db.update(memoContract.MemoEntry.TABLE_NAME, contentValues,
                    memoContract.MemoEntry._ID + " = " + mMemoId, null);
            // 수정 되지 않은 경우
            if(count == 0){
                Toast.makeText(this, "수정에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                // 수정 된 경우.
                Toast.makeText(this, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }
        super.onBackPressed();
    }

    // 메세지 보내는 함수
    public void sendMessage(View view) {
        mTitleEditText = findViewById(R.id.title_edit);
        mContentsEditText = findViewById(R.id.contents_edit);
        String sendMessage = "Title : " + mTitleEditText.getText().toString() + "\n"
                + "Message : " + mContentsEditText.getText().toString() + "\n"
                + "...from the Simple Memo App";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendMessage);

        // 처리할 수 있는 액티비티가 있으면 그 액티비티를 실행하라
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }

    }

    // 메뉴 재정의
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메뉴 XML 붙이기
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.memu_main, menu);
        return true;
    }

    // 메뉴 클릭시 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
