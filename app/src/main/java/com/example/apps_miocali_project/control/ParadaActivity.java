package com.example.apps_miocali_project.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.apps_miocali_project.R;

import java.util.ArrayList;

public class ParadaActivity extends AppCompatActivity {


    private String paradas;
    private ArrayList<String> rutasParada;
    private String idParada;
    private Button regreso;
    private DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parada);
        db=new DataBase(getApplicationContext());
        idParada = getIntent().getStringExtra("parada");
        rutasParada=new ArrayList<>();
        rutasParada=db.cargarRutasParada(idParada);

    }




    public Activity getActivity() {
        return this;
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, HomeActivity.class);
        this.finish();
        startActivity(intent);

    }

}
