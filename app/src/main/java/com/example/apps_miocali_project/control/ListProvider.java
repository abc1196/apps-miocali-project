package com.example.apps_miocali_project.control;

/**
 * Created by alejo on 3/12/2017.
 */

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.apps_miocali_project.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Modelo.Tiempo;

import static com.example.apps_miocali_project.R.id.txtError;

/**
 * Created by the-dagger on 24/7/16.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private final static String NOMBRE_RUTA="http://190.216.202.35:82/WebInformador/rxp/mobil/clienteXML.php?id=";

    private Context context;
    private Intent intent;
    ArrayList<Tiempo> tiempos= new ArrayList<Tiempo>();
    private String idParada="";
    //For obtaining the activity's context and intent
    public ListProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        SharedPreferences sharedPref = context.getSharedPreferences(ParadaWidget.NUEVA_PARADA,Context.MODE_PRIVATE);
        idParada=sharedPref.getString(ParadaWidget.NUEVA_PARADA,"NULL");
    }

    private void initCursor(){

        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
        getStopTimes();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        initCursor();
    }

    @Override
    public void onDataSetChanged() {
        /** Listen for data changes and initialize the cursor again **/
        initCursor();
    }

    @Override
    public void onDestroy() {
       tiempos.clear();
    }

    @Override
    public int getCount() {
        return tiempos.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        /** Populate your widget's single list item **/
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_item);
        remoteViews.setTextViewText(R.id.txtBus,tiempos.get(i).getBus());
        remoteViews.setTextViewText(R.id.txtTime,tiempos.get(i).getTiempo()+" min.");
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    public boolean activarBuses=true;

    public void getStopTimes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

         //       while(activarBuses) {

                    try {
                        Document doc = Jsoup.connect(NOMBRE_RUTA + idParada).get();
                        Element table = doc.select("table").get(0);
                        Elements rows = table.select("tr");
                        tiempos.clear();
                        for (int i = 0; i < rows.size(); i++) {
                            Element row = rows.get(i);
                            Elements col = row.select("td");
                            Tiempo info= new Tiempo();
                            if (col.size() == 4) {
                                info.setBus(col.get(0).text());
                                info.setDestino(col.get(1).text());
                                info.setTiempo(Integer.parseInt(col.get(2).text()));
                                info.setMin(col.get(3).text());
                                tiempos.add(info);
                                Log.d("ABCTAG","BUS: "+info.getBus()+" "+info.getTiempo()+" "+info.getDestino());
                            } else if (col.size() == 3 && !col.get(0).text().equals("")) {
                                Log.d("ABCTAG","BUS NOMBRE: "+col.get(0).text()+"...");
                                info.setBus(col.get(0).text());
                                info.setDestino(col.get(1).text());
                                info.setTiempo(0);
                                info.setMin("");
                                info.setProximo(true);
                                tiempos.add(info);
                                Log.d("ABCTAG","PROXIMO: "+info.getBus()+" "+info.getTiempo()+" "+info.getDestino());
                            }

                        }
                        Thread.sleep(10000);
                    } catch (IOException e) {
                        builder.append("Error : ").append(e.getMessage()).append("\n");
                    } catch (InterruptedException e) {

                    }



     //           }
            }
        }).start();

    }


}



