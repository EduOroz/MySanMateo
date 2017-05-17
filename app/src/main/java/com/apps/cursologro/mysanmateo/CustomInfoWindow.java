package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Eduardo on 17/05/2017.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater=null;
    Evento evento;
    Context context;

    CustomInfoWindow(LayoutInflater inflater, Evento evento, Context context) {
        this.inflater=inflater;
        this.evento=evento;
        this.context=context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View popup=inflater.inflate(R.layout.info_window_layout, null);

        System.out.println("Estamos en getInfoContents titulo " +marker.getTitle());
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

        tvMarker_title.setText(evento.getTitle());
        tvMarker_title.setTypeface(faceLBold);
        tvMarker_thematic.setText(evento.getTitulo_categoria());
        tvMarker_thematic.setAllCaps(true);
        tvMarker_thematic.setTypeface(faceLMedium);
        tvMarker_place.setText(evento.getPlace());
        tvMarker_place.setTypeface(faceLBold);
        tvMarker_address.setText(evento.getAddress());
        tvMarker_address.setTypeface(faceLMedium);

        tv_Marker_day.setText("DIA " +evento.getStart_date().substring(8, 10));
        tv_Marker_day.setTypeface(faceLBold);

        //Para poner la hora revisamos si el comienzo y el fin es el mismo o no
        String start_time = evento.getStart_time();
        String finish_time = evento.getFinish_time();
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
