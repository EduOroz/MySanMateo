package com.apps.cursologro.mysanmateo;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by Edu on 12/05/2017.
 */

public class PortadaCursorAdapter extends CursorAdapter {

    public PortadaCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        System.out.println("Constructor de PortadaCursorAdapter");
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        System.out.println("newView de PortadaCursorAdapter");
        return LayoutInflater.from(context).inflate(R.layout.elemento_portada, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        System.out.println("Estamos en bindView de PortadaCursorAdapter");
        // Find fields to populate in inflated template
        final TextView tvTituloPortada = (TextView) view.findViewById(R.id.tvTituloPortada);
        final TextView tvSubtituloPortada = (TextView) view.findViewById(R.id.tvSubtituloPortada);
        final TextView tvNumero = (TextView) view.findViewById(R.id.tvNumero);
        final ImageButton ibAListado = (ImageButton) view.findViewById(R.id.ibAListado);


        // Configuramos los estilos de los text fields
        Typeface faceLBold= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Bold.ttf");
        Typeface faceLRegular= Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");
        tvTituloPortada.setTypeface(faceLBold);
        tvSubtituloPortada.setTypeface(faceLRegular);
        tvNumero.setTypeface(faceLBold);

        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
        String subtitle = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"));
        Integer num = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));

        System.out.println("Los datos del cursor son " +title +" " +subtitle +" " +num);
        // Populate fields with extracted properties
        tvTituloPortada.setText(title);
        tvSubtituloPortada.setText(subtitle);
        tvNumero.setText(" "+num +" ");

        //Creamos un listener en el ibAListado
        ibAListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTituloPortada.getText().equals("San Mateo")){
                    MainActivity.main.moveToListado(false);}
                else {
                    MainActivity.main.moveToListado(true);
                }
            }
        });

        tvNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTituloPortada.getText().equals("San Mateo")){
                    MainActivity.main.moveToListado(false);}
                else {
                    MainActivity.main.moveToListado(true);
                }
            }
        });

       view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTituloPortada.getText().equals("San Mateo")){
                    MainActivity.main.moveToListado(false);}
                else {
                    MainActivity.main.moveToListado(true);
                }
            }
        });

    }
}

