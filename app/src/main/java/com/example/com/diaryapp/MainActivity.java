package com.example.com.diaryapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go Home", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.getHeaderView(0);
        TextView txt = nav_header.findViewById(R.id.user_idid);
        txt.setText(idid);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
            intent.putExtra("id",idid);
            intent.putExtra("pw",pwpw);
            startActivity(intent);
            //Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_memo) {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            intent.putExtra("id",idid);
            intent.putExtra("pw",pwpw);
            //Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

            logoutDB lDB = new logoutDB();
            lDB.execute(a,b);
            // 세션 파괴 하기

            startActivity(intent);
            finish();
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
