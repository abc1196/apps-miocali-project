package com.example.apps_miocali_project.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apps_miocali_project.R;

public class HomeActivity extends AppCompatActivity {

    public DataBase db;
    public ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        db = new DataBase(this);
        tareaAsyncCargarDatos tareaCargar = new tareaAsyncCargarDatos();
        tareaCargar.execute();
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

    public class tareaAsyncCargarDatos extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = ProgressDialog.show(getApplicationContext(),"Sync", "Cargando datos primera vez",false, false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.inicarDatos(getApplicationContext());
            return null;
        }

        protected  void onPostExecute(Void voids){
            super.onPostExecute(voids);
            pg.dismiss();
        }
    }
}

