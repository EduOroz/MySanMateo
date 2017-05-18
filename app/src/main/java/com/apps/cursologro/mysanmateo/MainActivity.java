package com.apps.cursologro.mysanmateo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static MainActivity main;
    public static ProgressDialog progressDialog;

    public boolean infantil;

    //Variables para la conexión a BD
    public EventosSQLite baseDatos;

    //Elementos para guardar los eventos que recogemos en el WS y los eventos padre
    static EventoPadre eventoPadre;
    static ArrayList<EventoPadre> eventosPadreBD = new ArrayList<>();

    //Elementos para manejar la Lista de Eventos
    public static ListView mylistaEventosPadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asignamos tipo de letra al título de la app
        TextView tvPortada = (TextView) findViewById(R.id.tvPortada);
        Typeface faceLHairline= Typeface.createFromAsset(this.getAssets(),"fonts/Lato-Light.ttf");
        tvPortada.setTypeface(faceLHairline);

        //Declaramos esta variable para poder acceder a sus métodos desde otras clases
        main = this;

        mylistaEventosPadre = (ListView) findViewById(R.id.lvPortada);
        mylistaEventosPadre.setDividerHeight(40);

        //Recogemos la BD creada en la ventana de carga
        baseDatos = SplashScreen.splashScreen.getDB();

    }

    @Override
    protected void onResume() {
        recuperarEventosEspeciales();
        super.onResume();
    }

    /**
     * Función para recuperar los eventos Padre que se van a mostrar en la ventana principal
     */
    private void recuperarEventosEspeciales (){
        //Vamos a recuperar de la Base de Datos los tipos de eventos especiales que tenemos
        eventosPadreBD = new ArrayList<>();
        String titulo;
        String descripcion;
        Integer cantidad;
        try {
            // Devuelve todos los eventos en el objeto Cursor.
            Cursor cursor = baseDatos.obtenerEventosEspeciales();
            System.out.println("ES numero columnas en el cursor " +cursor.getColumnCount());
            System.out.println("ES numero resultados " +cursor.getCount());

            //Recorremos el cursor y guardamos los eventos en evetosPadreBD
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                //System.out.println("Elemento del cursor titulo " +cursor.getString(cursor.getColumnIndex("titulo")));
                descripcion = cursor.getString(cursor.getColumnIndex("subtitulo"));
                cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                eventoPadre = new EventoPadre(titulo, descripcion, cantidad);
                eventosPadreBD.add(eventoPadre);
                //System.out.println("En cursor recuperarEventosPadre " +eventoPadre.getTitulo());
            }

            System.out.println("Tras crear eventosPadre cargamos el adaptador");
            // Setup cursor adapter using cursor from last step
            PortadaCursorAdapter portadaAdapter = new PortadaCursorAdapter(this, cursor);
            // Attach cursor adapter to the ListView
            mylistaEventosPadre.setAdapter(portadaAdapter);

        } catch (Exception e) {
            System.out.println("El mensaje de error es: " + e.getMessage());
        } finally {
            // Se cierra la base de datos.
            //baseDatos.cerrar();
        }
    }

    /**
     * Función para cambiar a la actividad de Listado le añadimos un progressDialog para que
     * mientras se carguen los datos el usuario sepa que la aplicación no se ha colgado
     */
    public void moveToListado (Boolean infantil){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progressdialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.infantil = infantil;
        Intent intent = new Intent(getBaseContext(), Listado.class);
        startActivity(intent);
    }

    public boolean isInfantil() {
        return this.infantil;
    }

}
