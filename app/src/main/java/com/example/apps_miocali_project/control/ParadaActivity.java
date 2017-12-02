package com.example.apps_miocali_project.control;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apps_miocali_project.R;

import java.util.ArrayList;

public class ParadaActivity extends AppCompatActivity {

    private final static String NOMBRE_RUTA="http://190.216.202.35:82/WebInformador/rxp/mobil/?id=";
    private ArrayList<String> rutasParada;
    private String idParada;
    private Toolbar mToolbar;
    private TextView txtParada;
    private DataBase db;
    private WebView myWebView;
    private String nombreParada;
    RecyclerView MyRecyclerView;
    private Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ALEJOTAG", "HASTA AQUI FUNCIONA PARADA");
        setContentView(R.layout.activity_parada);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtParada= (TextView) findViewById(R.id.textview);
        db=new DataBase(getApplicationContext());
        idParada = (String)getIntent().getStringExtra("parada");
        nombreParada=(String)getIntent().getStringExtra("nombreParada");
        Log.d("ID_PARADA",idParada);
        rutasParada=new ArrayList<String>();
        rutasParada=db.cargarRutasParada(idParada);
        txtParada.setText(nombreParada);
         myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        Log.d("PARADA", idParada);

        MyRecyclerView = (RecyclerView) findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (rutasParada.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(rutasParada));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);


        aSwitch= (Switch) findViewById(R.id.toggleButton1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myWebView.loadUrl(NOMBRE_RUTA+idParada);
                    myWebView.setVisibility(View.VISIBLE);
                }else{
                    myWebView.stopLoading();
                    myWebView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Activity getActivity() {
        return this;
    }

    public void onClick(View view) {

       // Intent intent = new Intent(this, HomeActivity.class);
        this.finish();
       // startActivity(intent);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public Button btnVerRuta;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            btnVerRuta = (Button) v.findViewById(R.id.btnViewRoute);
            btnVerRuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "RUTA SOLIDA",Toast.LENGTH_LONG );
                }
            });

        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<String> list;
        public MyAdapter(ArrayList<String> Data) {
            list = Data;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.route_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            String nombreRuta= list.get(position);
            char tipo=nombreRuta.charAt(0);
            holder.titleTextView.setText(list.get(position));
            switch (tipo){
                case 'A':
                    holder.titleTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
                case 'E':
                    holder.titleTextView.setBackgroundColor(Color.rgb(218,214,0));
                    break;
                case 'T':
                    holder.titleTextView.setBackgroundColor(Color.rgb(216,35,42));
                    break;
                case 'P':
                    holder.titleTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;

            }
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}
