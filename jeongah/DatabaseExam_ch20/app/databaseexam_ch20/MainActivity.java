package com.example.gluck.databaseexam_ch20;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1000;
    private MemoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // + 버튼 눌렀을 때 실행.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, MemoActivity.class),
                        REQUEST_CODE_INSERT);
            }
        });

        // 리스트 뷰 가져오기
        ListView listView = findViewById(R.id.memo_list);

        Cursor cursor = getMemoCursor();
        mAdapter = new MemoAdapter(this, cursor);
        listView.setAdapter(mAdapter);

        // 메모 리스트 클릭시 보여주도록.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                // 클릭한 시점의 아이템을 얻음
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                // 커서의 제목과 내용을 얻음
                String title = cursor.getString(cursor.getColumnIndexOrThrow
                        (memoContract.MemoEntry.COLUMN_NAME_TITLE));
                String contents = cursor.getString(cursor.getColumnIndexOrThrow
                        (memoContract.MemoEntry.COLUMN_NAME_CONTENTS));
                // 인텐트에 id 와 함께 저장
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);
                //MemoActivity 시작
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        // 데이터 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long deleteId = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("메모 삭제");
                builder.setMessage("메모를 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = MemoDbHelper.getsInstance(MainActivity.this).getWritableDatabase();
                        int deletedCount = db.delete(memoContract.MemoEntry.TABLE_NAME,
                                memoContract.MemoEntry._ID + " = " + deleteId, null);
                        if(deletedCount == 0){
                            Toast.makeText(MainActivity.this, "삭제에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            // 삭제 되었다면 새로 갱신되 커서를 전달해서 목록을 갱신한다.
                            mAdapter.swapCursor(getMemoCursor());
                            Toast.makeText(MainActivity.this, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });
    }


    private Cursor getMemoCursor() {
        MemoDbHelper dbHelper = MemoDbHelper.getsInstance(this);
        return dbHelper.getReadableDatabase()
                .query(memoContract.MemoEntry.TABLE_NAME,
                null, null, null, null, null, memoContract.MemoEntry._ID
                                + " DESC");
    }


    // 데이터 받는 코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK){
            // 데이터 갱신
            mAdapter.swapCursor(getMemoCursor());
        }
    }


    private static class MemoAdapter extends CursorAdapter{
        public MemoAdapter(Context context, Cursor c) {

            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // 데이터 보여주는 곳
        // 타이틀만 뿌져준다.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleText = view.findViewById(android.R.id.text1);
            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow
                    (memoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }
    }
}