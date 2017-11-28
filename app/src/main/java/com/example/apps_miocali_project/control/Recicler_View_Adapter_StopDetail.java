package com.example.apps_miocali_project.control;
import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apps_miocali_project.R;

import java.util.ArrayList;
/**
 * Created by Nicolás on 21-sep-17.
 */

public class Recicler_View_Adapter_StopDetail extends RecyclerView.Adapter<Recicler_View_Adapter_StopDetail.View_Holder> {

    static ArrayList<String> rutasParadas;
    static Context context;
    static String idParada;
    static int mShortAnimationDuration;
    private static Animator mCurrentAnimator;

    public Recicler_View_Adapter_StopDetail(ArrayList<String> rutasParadas, String idParada, Context context) {
        this.rutasParadas = rutasParadas;
        this.idParada=idParada;
        this.context = context;
    }

    @Override
    public Recicler_View_Adapter_StopDetail.View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item_list, parent, false);
        Recicler_View_Adapter_StopDetail.View_Holder holder = new Recicler_View_Adapter_StopDetail.View_Holder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(Recicler_View_Adapter_StopDetail.View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        //holder.title.setText(rutasParadas.get(position).getTitulo());
        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return rutasParadas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, String ruta) {
        rutasParadas.add(position, ruta);
        notifyItemInserted(position);
    }

    public static class View_Holder extends RecyclerView.ViewHolder {

        //PARA EL BOTON DE CARD VIEW
        //Button rutaid=new Button();
        CardView cv;
        View_Holder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            //AQUI VA EL LISTENER DEL BOTON QUE TIENE EL NOMBRE DE RUTA
            //imageView.setOnClickListener(new View.OnClickListener() {
              //  @Override
                //public void onClick(View v) {
                  //  int position = getAdapterPosition();

//                }
  //          });

        }
    }
}
