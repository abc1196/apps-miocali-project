package com.example.apps_miocali_project.control;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;

import Modelo.Tiempo;

public class ParadaActivity extends AppCompatActivity {

    private final static String NOMBRE_RUTA="http://190.216.202.35:82/WebInformador/rxp/mobil/clienteXML.php?id=";
    private ArrayList<String> rutasParada;
    private String idParada;
    private Toolbar mToolbar;
    private TextView txtParada;
    private DataBase db;
    private WebView myWebView;
    private TextView txtWeb;
    private TextView txtError;
    private String nombreParada;
    RecyclerView MyRecyclerView;
    RecyclerView recyclerViewTime;
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
        txtParada= (TextView) findViewById(R.id.txtParada);
        txtWeb=(TextView) findViewById(R.id.txtWeb);
        txtError=(TextView)findViewById(R.id.txtError);
        db=new DataBase(getApplicationContext());
        idParada = (String)getIntent().getStringExtra("parada");
        nombreParada=(String)getIntent().getStringExtra("nombreParada");
        Log.d("IDPARADA",idParada);
        rutasParada=new ArrayList<String>();
        rutasParada=db.cargarRutasParada(idParada);
        txtParada.setText(nombreParada);
  //      myWebView = (WebView) findViewById(R.id.webView);
   //     WebSettings webSettings = myWebView.getSettings();
    //    webSettings.setJavaScriptEnabled(true);
   //     myWebView.setWebViewClient(new WebViewClient());
        Log.d("PARADA", idParada);

        MyRecyclerView = (RecyclerView) findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (rutasParada.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(rutasParada));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        recyclerViewTime=(RecyclerView)findViewById(R.id.card_view_time);
        recyclerViewTime.setHasFixedSize(true);
        aSwitch= (Switch) findViewById(R.id.toggleButton1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    activarBuses=true;
                    txtWeb.setText("");
                    getStopTimes();
                }else{
                    txtWeb.setText(getResources().getString(R.string.info_buses));
                    activarBuses=false;
                    infoTiempos.clear();
                    txtError.setText("");
                    recyclerViewTime.setVisibility(View.GONE);
                }
            }
        });

    }
    boolean activarBuses=false;
    String htmldata="";
    ArrayList<Tiempo> infoTiempos= new ArrayList<Tiempo>();
    public void getStopTimes(){
        txtError.setVisibility( View.VISIBLE);
        txtError.setText(getResources().getString(R.string.cargando_buses));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                while(activarBuses) {

                    try {
                        Document doc = Jsoup.connect(NOMBRE_RUTA + idParada).get();
                        Element table = doc.select("table").get(0);
                        Elements rows = table.select("tr");
                        infoTiempos.clear();
                        for (int i = 0; i < rows.size(); i++) {
                            Element row = rows.get(i);
                            Elements col = row.select("td");
                                 Tiempo info= new Tiempo();
                                if (col.size() == 4) {
                                    info.setBus(col.get(0).text());
                                    info.setDestino(col.get(1).text());
                                    info.setTiempo(Integer.parseInt(col.get(2).text()));
                                    info.setMin(col.get(3).text());
                                    infoTiempos.add(info);
                                    Log.d("ABCTAG","BUS: "+info.getBus()+" "+info.getTiempo()+" "+info.getDestino());
                                } else if (col.size() == 3 && !col.get(0).text().equals("")) {
                                    Log.d("ABCTAG","BUS NOMBRE: "+col.get(0).text()+"...");
                                    info.setBus(col.get(0).text());
                                    info.setDestino(col.get(1).text());
                                    info.setTiempo(0);
                                    info.setMin("");
                                    info.setProximo(true);
                                    infoTiempos.add(info);
                                    Log.d("ABCTAG","PROXIMO: "+info.getBus()+" "+info.getTiempo()+" "+info.getDestino());
                                }

                        }

                        htmldata = doc.outerHtml();
                        Log.d("ALEJOTAG", htmldata);
                        // myWebView.loadDataWithBaseURL("file:///android_asset/.", htmldata, "text/html", "UTF-8", null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
                                MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                if (infoTiempos.size() > 0 & recyclerViewTime != null) {
                                    recyclerViewTime.setAdapter(new MyAdapterTime(infoTiempos));
                                    recyclerViewTime.setLayoutManager(MyLayoutManager);
                                    recyclerViewTime.setVisibility(View.VISIBLE);
                                    txtError.setText("");
                                    txtError.setVisibility(View.GONE);
                                }else{
                                    txtError.setVisibility( View.VISIBLE);
                                    txtError.setText(getResources().getString(R.string.error_buses));
                                }




                            }
                        });
                        Thread.sleep(10000);
                    } catch (IOException e) {
                        builder.append("Error : ").append(e.getMessage()).append("\n");
                    } catch (InterruptedException e) {

                    }



                }
                infoTiempos.clear();
                recyclerViewTime.setVisibility(View.GONE);
            }
        }).start();
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



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public Button btnVerRuta;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);

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


    public class MyViewHolderTime extends RecyclerView.ViewHolder {

        public TextView txtBus;
        public TextView txtProximo;
        public TextView txtTime;

        public MyViewHolderTime(View v) {
            super(v);
            txtBus = (TextView) v.findViewById(R.id.txtBus);
            txtProximo = (TextView) v.findViewById(R.id.txtProximo);
            txtTime = (TextView) v.findViewById(R.id.txtTime);


        }
    }

    public class MyAdapterTime extends RecyclerView.Adapter<MyViewHolderTime> {
        private ArrayList<Tiempo> list;
        public MyAdapterTime(ArrayList<Tiempo> Data) {
            list = Data;
        }
        @Override
        public MyViewHolderTime onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.time_item, parent, false);
            MyViewHolderTime holder = new MyViewHolderTime(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolderTime holder, int position) {
            String nombreRuta= list.get(position).getBus();
            holder.txtBus.setText(nombreRuta);
            if(list.get(position).isProximo()){
                holder.txtTime.setText("Llegando...");
                ObjectAnimator blink = ObjectAnimator.ofInt(holder.txtTime, "textColor", Color.BLACK, Color.TRANSPARENT);
                blink.setDuration(1000);
                blink.setEvaluator(new ArgbEvaluator());
                blink.setRepeatCount(ValueAnimator.INFINITE);
                blink.setRepeatMode(ValueAnimator.REVERSE);
                blink.start();
            }else{
                holder.txtTime.setText(list.get(position).getTiempo()+ " min.");
            }

        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}