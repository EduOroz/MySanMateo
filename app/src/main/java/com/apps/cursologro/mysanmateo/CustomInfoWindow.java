package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Eduardo on 17/05/2017.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater=null;
    ArrayList<Evento> eventosBD = new ArrayList<>();
    Context context;

    CustomInfoWindow(LayoutInflater inflater, Context context) {
        this.inflater=inflater;
        this.eventosBD=Listado.listado.getObjeto();
        this.context=context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View popup=inflater.inflate(R.layout.info_window_layout, null);

        Evento eventoToWindow = new Evento();

        System.out.println("Estamos en getInfoContents titulo " +marker.getTitle());

        //Buscamos el evento señalado en el marker en nuestra lista de eventos
        for (Evento mEvento : eventosBD ) {
            if (mEvento.getTitle().equals(marker.getTitle())){
                eventoToWindow = mEvento;
            } else {
                System.out.println("No hemos encontrado el evento en getInfoWindow");
            }
        }

        TextView tvMarker_title=(TextView)popup.findViewById(R.id.tvMarker_title);
        TextView tvMarker_thematic=(TextView)popup.findViewById(R.id.tvMarker_thematic);
        TextView tvMarker_place=(TextView)popup.findViewById(R.id.tvMarker_place);
        TextView tvMarker_address=(TextView)popup.findViewById(R.id.tvMarker_address);
        TextView tv_Marker_day=(TextView)popup.findViewById(R.id.tvMarker_day);
        TextView tv_Marker_time=(TextView)popup.findViewById(R.id.tvMarker_hour);

        //Tipos de letra
        Typeface faceLBold= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Bold.ttf");
        Typeface faceLLight= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Light.ttf");
        Typeface faceLMedium= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Medium.ttf");

        tvMarker_title.setText(eventoToWindow.getTitle());
        tvMarker_title.setTypeface(faceLBold);
        tvMarker_thematic.setText(eventoToWindow.getTitulo_categoria());
        tvMarker_thematic.setAllCaps(true);
        tvMarker_thematic.setTypeface(faceLMedium);
        tvMarker_place.setText(eventoToWindow.getPlace());
        tvMarker_place.setTypeface(faceLBold);
        tvMarker_address.setText(eventoToWindow.getAddress());
        tvMarker_address.setTypeface(faceLMedium);

        tv_Marker_day.setText("DIA " +eventoToWindow.getStart_date().substring(8, 10));
        tv_Marker_day.setTypeface(faceLBold);

        //Para poner la hora revisamos si el comienzo y el fin es el mismo o no
        String start_time = eventoToWindow.getStart_time();
        String finish_time = eventoToWindow.getFinish_time();
        if (start_time.equals(finish_time)){
            tv_Marker_time.setText(start_time +" h");
        }
        else {
            tv_Marker_time.setText(start_time +" h - " +finish_time +" h");
        }
        tv_Marker_time.setTypeface(faceLMedium);

        return(popup);
    }
}
