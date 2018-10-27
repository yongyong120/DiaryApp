package com.example.com.diaryapp;

import android.provider.BaseColumns;

// 계약 클래스는 테이블 생성에 대한 sql 문이나 칼럼구조, 테이블명 등을 정의합니다.
// 스키마 정한 것을 정의
public final class memoContract {
    // 인스턴스화 금지
    private memoContract(){}

    // 테이블 정보를 내부 클래스로 정의
    public static class MemoEntry implements BaseColumns{
        public static final String TABLE_NAME = "memo";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
    }
}
