package com.example.apps_miocali_project.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apps_miocali_project.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public Activity getActivity(){
        return this;
    }
}
