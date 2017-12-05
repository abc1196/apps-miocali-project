package com.example.apps_miocali_project.control;

import android.annotation.TargetApi;
import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Switch;
import android.widget.TextView;

import com.example.apps_miocali_project.R;

import java.util.ArrayList;

import Modelo.Tiempo;

/**
 * Implementation of App Widget functionality.
 */
public class ParadaWidget extends AppWidgetProvider {
    public final static String NUEVA_PARADA="NUEVA_PARADA";
    public final static String NOMBRE_PARADA="NOMBRE_PARADA";

    private final static String NOMBRE_RUTA="http://190.216.202.35:82/WebInformador/rxp/mobil/clienteXML.php?id=";
    private final static String TIEMPO_BUSES="TIEMPO_BUSES";
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
    boolean activarBuses=false;
    String htmldata="";
    ArrayList<Tiempo> infoTiempos= new ArrayList<Tiempo>();
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences sharedPref = context.getSharedPreferences(NUEVA_PARADA,Context.MODE_PRIVATE);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.parada_widget);
        Log.d("WIDGETAG",sharedPref.getString(NUEVA_PARADA,"No hay shared"));
        views.setTextViewText(R.id.txtParada, sharedPref.getString(NOMBRE_PARADA,"No hay Parada favorita"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        SharedPreferences sharedPref = context.getSharedPreferences(NUEVA_PARADA,Context.MODE_PRIVATE);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.parada_widget);
        Log.d("WIDGETAG",sharedPref.getString(NUEVA_PARADA,"No hay shared"));
        views.setTextViewText(R.id.txtParada, sharedPref.getString(NOMBRE_PARADA,"No hay Parada favorita"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /** Set the Adapter for out widget **/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.listViewWidget,
                new Intent(context, WidgetService.class));
    }


    /** Deprecated method, don't create this if you are not planning to support devices below 4.0 **/
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.listViewWidget,
                new Intent(context, WidgetService.class));
    }



}

