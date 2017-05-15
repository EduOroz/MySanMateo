package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static MainActivity main;

    //Variables para la conexión a BD
    public EventosSQLite baseDatos;

    //Elementos para guardar los eventos que recogemos en el WS y los eventos padre
    static EventoPadre eventoPadre;
    static ArrayList<EventoPadre> eventosPadre = new ArrayList<>();
    static ArrayList<EventoPadre> eventosPadreBD = new ArrayList<>();

    static Evento evento;
    static ArrayList<Evento> eventos = new ArrayList<>();
    static ArrayList<Evento> eventosBD = new ArrayList<>();

    //Elementos para manejar la Lista de Eventos
    public static ListView mylistaEventosPadre;
    static Context myContext;

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

        //Instanciamos la BD
        baseDatos = new EventosSQLite(this);

        //Revisamos Si existe el archivo SQLite, si no lo creamos
        if (existeBD("DBEventos")) {
            //
            System.out.println("La Base de Datos existe");
        } else {
            System.out.println("La Base de Datos no existe");

            //Realizamos una consulta al web service para traernos la información de los eventos a mostrar
            StringRequest stringRequest = new StringRequest(Request.Method.GET, " https://testgestorlogrono.get-app.es/api/events",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("URL_CHECK_ENABLE_FORM", "Response:"+response);
                            System.out.println("Respuesta: " + response);
                            //ArrayList<Evento> p = new ArrayList<>();
                            try {
                                //creamos un array JSON a partir de la cadena recibida
                                JSONObject jobj = new JSONObject(response);
                                if (jobj != null) {
                                /*p = new Gson().fromJson(jarray.toString(), new TypeToken<List<Evento>>(){}.getType());*/
                                    JSONArray jarray = (JSONArray) jobj.get("modified");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobject = jarray.getJSONObject(i);
                                        System.out.println("objeto " + i + " id es: " + jobject.get("id"));
                                        JSONArray jarray_horario = (JSONArray) jobject.get("schedules");
                                        evento = new Evento(jobject.getInt("id"), jobject.getString("place"), jobject.getInt("thematic_id"), jobject.getString("title"), jobject.getString("text"), jobject.getString("publication_date"), jobject.getString("link"), jobject.getString("address"), jobject.getDouble("lat"), jobject.getDouble("lng"), jarray_horario.getJSONObject(0).getString("start_date"), jarray_horario.getJSONObject(0).getString("finish_date"), jarray_horario.getJSONObject(0).getString("start_time"), jarray_horario.getJSONObject(0).getString("finish_time"));
                                        System.out.println("evento place: " + evento.getPlace());
                                        eventos.add(evento);

                                        //insertamos en baseDatos
                                        baseDatos.insertarEvento(evento);
                                    }
                                    System.out.println("eventos 0 place: " + eventos.get(0).getPlace());

                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                /*@Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Accept", "application/json");
                    return params;
                }*/
                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };

            MyVolley.getInstance(this).addToRequestQueue(stringRequest);

            //Realizamos una consulta al web service para traernos la información de las categorías
            stringRequest = new StringRequest(Request.Method.GET, "https://testgestorlogrono.get-app.es/api/thematics",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("URL_CHECK_ENABLE_FORM", "Response:"+response);
                            System.out.println("Respuesta categoría: " + response);
                            //ArrayList<Evento> p = new ArrayList<>();
                            try {
                                //creamos un array JSON a partir de la cadena recibida
                                JSONObject jobj = new JSONObject(response);
                                if (jobj != null) {
                                /*p = new Gson().fromJson(jarray.toString(), new TypeToken<List<Evento>>(){}.getType());*/
                                    JSONArray jarray = (JSONArray) jobj.get("modified");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobject = jarray.getJSONObject(i);
                                        System.out.println("objeto categoria " + i + " id es: " + jobject.get("id"));
                                        //insertamos en baseDatos
                                        baseDatos.insertarCategoria(jobject.getInt("id"), jobject.getString("title"));
                                    }
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                /*@Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Accept", "application/json");
                    return params;
                }*/
                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };

            MyVolley.getInstance(this).addToRequestQueue(stringRequest);

            //Creamos los 2 eventos especiales San Mateo y San Mateo Infantil
            baseDatos.insertarEventosEspeciales("PROGRAMA DE FIESTAS COMPLETO", "San Mateo 2017", 22);
            baseDatos.insertarEventosEspeciales("PROGRAMA INFANTIL ", "San Mateo 2017", 12);
        }
    }

    @Override
    protected void onResume() {
        recuperarEventosEspeciales();
        //moveToListado();
        super.onResume();
    }

    /**
    * Permite comprobar si existe la base de datos
    * @param dbName Nombre de la base de datos
    * @return si existe la base de datos
    */
    private boolean existeBD(String dbName) {
        File dbFile = getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }

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
                System.out.println("Elemento del cursor titulo " +cursor.getString(cursor.getColumnIndex("titulo")));
                descripcion = cursor.getString(cursor.getColumnIndex("subtitulo"));
                cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
                eventoPadre = new EventoPadre(titulo, descripcion, cantidad);
                eventosPadreBD.add(eventoPadre);
                System.out.println("En cursor recuperarEventosPadre " +eventoPadre.getTitulo());
            }

            System.out.println("Tras crear eventosPadre cargamos el adaptador");
            // Find ListView to populate -> Variable global listaEventos

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

    public void moveToListado (){
        Intent intent = new Intent(getBaseContext(), Listado.class);
        startActivity(intent);
    }

    public EventosSQLite getDB (){
        return baseDatos;
    }

}
