package com.apps.cursologro.mysanmateo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Edu on 08/05/2017.
 */

public class ListadoCursorAdapter extends CursorAdapter {
    public ListadoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        System.out.println("Creando ListadoCursorAdapter");
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.elemento, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Cursor cursorCopy = cursor;
        String dia_anterior;

        // Find fields to populate in inflated template
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitulo);
        TextView tvPlace = (TextView) view.findViewById(R.id.tvLugar);
        TextView tvDay = (TextView) view.findViewById(R.id.tvDia);
        TextView tvHour = (TextView) view.findViewById(R.id.tvHora);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvDireccion);
        TextView tvThematic = (TextView) view.findViewById(R.id.tvCategoria);
        ImageView ivWeb = (ImageView) view.findViewById(R.id.ivWeb);
        ImageView ivLocalizacion = (ImageView) view.findViewById(R.id.ivLocalizacion);

        // Configuramos los estilos de los text fields
        Typeface faceLBold= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Bold.ttf");
        tvDay.setTypeface(faceLBold);

        // Extract properties from cursor
        final Integer id_evento = cursor.getInt(cursor.getColumnIndexOrThrow("id_evento"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String place = cursor.getString(cursor.getColumnIndexOrThrow("place"));
        String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        //Integer idThematic = cursor.getInt(cursor.getColumnIndexOrThrow("thematic_id"));
        String titulo_categoria = cursor.getString(cursor.getColumnIndexOrThrow("title_categoria"));
        String start_date = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
        String start_time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
        String finish_time = cursor.getString(cursor.getColumnIndexOrThrow("finish_time"));
        String link = cursor.getString(cursor.getColumnIndexOrThrow("link"));

        if (!cursor.isFirst()) {
            cursorCopy.moveToPrevious();
            dia_anterior = cursorCopy.getString(cursor.getColumnIndexOrThrow("start_date"));
        } else {
            dia_anterior="2999";
        }

        System.out.println("cursor title " +title +" " +start_date);
        System.out.println("cursorCopy title " +cursorCopy.getString(cursorCopy.getColumnIndexOrThrow("title")) +" " +dia_anterior);

        // Populate fields with extracted properties
        tvTitle.setText(title);
        tvPlace.setText(place);
        tvAddress.setText(address);
        //tvThematic.setText(""+idThematic);
        tvThematic.setText(titulo_categoria);

        //El icono de la url sólo se visualizará si hay campo url
        if (link.equals("")){
            ivWeb.setVisibility(View.GONE);
        } else {
            ivWeb.setVisibility(View.VISIBLE);
        }

        //El elemento día sólo se visualizará en el primer evento del día
        if (start_date.equals(dia_anterior)) {
            tvDay.setVisibility(View.GONE);
            System.out.println("Mismo día");
        } else {
            System.out.println("Ponemos el Día " +start_date.substring(8, 10));
            tvDay.setVisibility(View.VISIBLE);
            tvDay.setText("Día " +start_date.substring(8, 10));
        }

        //Para poner la hora revisamos si el comienzo y el fin es el mismo o no
        if (start_time.equals(finish_time)){
            tvHour.setText(start_time);
        }
        else {
            tvHour.setText(start_time +" - " +finish_time);
        }

        //Creamos un listener en el ivLocalización
        ivLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Listado.fragmentMapa.mostrarEtiqueta(id_evento);
               Listado.listado.cambiarMapa();
            }
        });

    }
}
