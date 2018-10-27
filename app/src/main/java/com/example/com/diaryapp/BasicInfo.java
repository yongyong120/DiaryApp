package com.example.com.diaryapp;

import java.text.SimpleDateFormat;

public class BasicInfo {


    //========== 날짜 포맷  ==========//
    public static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy년 MM월 dd일");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    //========== 인텐트 부가정보 전달을 위한 키값 ==========//
    public static final String KEY_WRITE_MODE = "WRITE_MODE";
    //===========인텐트 다이어리 쓰기 모드 상수=====================//
    public static final String MODE_INSERT = "D_INSERT";
    public static final String MODE_MODIFY = "D_MODIFY";
    //========== 액티비티 요청 코드  ==========//
    public static final int REQ_VIEW_ACTIVITY = 1001;
    public static final int REQ_INSERT_ACTIVITY = 1002;


}
