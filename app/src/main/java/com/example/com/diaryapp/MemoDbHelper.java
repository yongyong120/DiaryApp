package com.example.com.diaryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 데이터베이스와 테이블을 생성하고 조작하는 동작을 쉽게 할 수 있도록 도움을 주는 헬퍼 클래스
// 처음 DB 조작이 발생할 때 테이블을 생성해 주고, DB 버전이 올라갈 때(DB의 구조가 변경될 때)의 처리 정의
public class MemoDbHelper extends SQLiteOpenHelper {
    // 싱글톤 인스턴스
    private static MemoDbHelper sInstance;

    // DB 버전으로 1부터 시작하고 스키마가 변경될 때 숫자가 올라간다.
    private static final int DB_VERSION = 1;

    // DB 파일명
    private static final String DB_NAME = "Memo.db";

    // 테이블 생성 SQL 문
    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT , %s TEXT)",
                    memoContract.MemoEntry.TABLE_NAME,
                    memoContract.MemoEntry._ID,
                    memoContract.MemoEntry.COLUMN_NAME_TITLE,
                    memoContract.MemoEntry.COLUMN_NAME_CONTENTS);

    // 테이블 삭제 SQL 문
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + memoContract.MemoEntry.TABLE_NAME;

    // 싱글톤 객체 생성해서 반환
    // 멀티 스레드 환경에서 db 에 접근해도 스레드에 안전하도록 synchronized  키워드를 팩토리 메서드에 추가
    public static synchronized MemoDbHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new MemoDbHelper(context);
        }
        return sInstance;
    }

    // 외부에서 생성 못하도록 private
    // MemoDbHelper 의 인스턴스 생성은 팩토리 메서드를 사용하도록 강제된다.
    private MemoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 최초의 db 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // DB 가 업데이트 될 때
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db 스키마가 변경될 때 여기서 데이터를 백업하고
        // 테이블을 삭제 후 재생성 및 데이터 복원 등을 한다.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
