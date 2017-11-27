package com.example.apps_miocali_project.control;

import android.Manifest;
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

    private ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pg = ProgressDialog.show(this,"Por favor espera...", "Cargando datos iniciales",false, false);
        db = new DataBase(this);
        tareaAsyncCargarDatos tareaCargar = new tareaAsyncCargarDatos(pg);
        tareaCargar.execute();
    }
    public Activity getActivity(){
        return this;
    }

    public class tareaAsyncCargarDatos extends AsyncTask<Void, Void, Void>{

        private ProgressDialog pg;

        public tareaAsyncCargarDatos(ProgressDialog pg) {
            this.pg=pg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                db.createDataBase(getApplicationContext());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute(Void voids){
            super.onPostExecute(voids);
            Intent intent = new Intent(getActivity(),MapActivity.class);
            startActivity(intent);
            pg.dismiss();
            finish();
        }
    }
}

