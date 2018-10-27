package com.example.com.diaryapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String idid,pwpw;
    String a,b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a="";
        b="";
        Intent intent = getIntent();
        idid = intent.getStringExtra("id");
        pwpw = intent.getStringExtra("pw");
        Log.i("id는", idid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //네비게이션 뷰 설정
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //네비게이션 헤더 사용자 아이디로 설정
        View nav_header = navigationView.getHeaderView(0);
        TextView txt = nav_header.findViewById(R.id.user_idid);
        txt.setText(idid);
        BasicInfo.userID = idid;
        //처음 화면을 일기장으로 설정
        Fragment fragment =  new DiaryFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout,fragment);
        ft.commit();
    }

    @Override //drawer layout 설정
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override//네비게이션 메뉴 아이템 선택했을 때
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //fragment
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        if (id == R.id.nav_diary) {
            fragment = new DiaryFragment();
            title ="Diary";
        } else if (id == R.id.nav_memo) { //메모 프래그먼트 생성
            fragment = new MemoFragment();
            title="Memo";
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            logoutDB lDB = new logoutDB();
            lDB.execute(a,b);
            // 세션 파괴 하기

            startActivity(intent);
            finish();
        }
        if(fragment!=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout,fragment);
            ft.commit();
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class logoutDB extends AsyncTask<String, Void, String> {

        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                String link = "http://ec2-54-180-86-219.ap-northeast-2.compute.amazonaws.com/logout.php";
                String str;

                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if(conn.getResponseCode() == conn.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = br.readLine();
                    Log.i("통신 결과", line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
