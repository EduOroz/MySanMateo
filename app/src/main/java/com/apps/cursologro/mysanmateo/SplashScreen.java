package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private GifImageView gifImageView;
    private ProgressBar progressBar;

    //Elementos para guardar los eventos que recogemos en el WS
    static Evento evento;
    static ArrayList<Evento> eventos = new ArrayList<>();

    //Variables para la conexión a BD
    public EventosSQLite baseDatos;

    //static Context myContext;
    public static SplashScreen splashScreen;

    public Integer espera;

    /**
     * Permite comprobar si existe la base de datos
     * @param dbName Nombre de la base de datos
     * @return si existe la base de datos
     */
    private boolean existeBD(String dbName) {
        File dbFile = getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreen = this;

        //Instanciamos la BD
        baseDatos = new EventosSQLite(this);

        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);

        // Establece el recurso GIFImageView
        try{
            InputStream inputStream = getAssets().open("splashscreen.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException ex)
        {

        }

        //Revisamos Si existe el archivo SQLite, si no, lo creamos
        if (existeBD("DBEventos")) {
            //
            espera = 3000;
            System.out.println("La Base de Datos existe");
        } else {
            System.out.println("La Base de Datos no existe");
            espera = 6000;
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

                @Override
                protected void onFinish() {
                    super.onFinish();
                    //Creamos los 2 eventos especiales San Mateo y San Mateo Infantil
                    baseDatos.insertarEventosEspeciales("PROGRAMA DE FIESTAS COMPLETO", "San Mateo 2017", 0);
                    baseDatos.insertarEventosEspeciales("PROGRAMA INFANTIL ", "San Mateo 2017", 134);
                }
            };

            MyVolley.getInstance(splashScreen).addToRequestQueue(stringRequest);

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

            MyVolley.getInstance(splashScreen).addToRequestQueue(stringRequest);

        }

        // Espera por 6 segundos y arranca Activity Main
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this,MainActivity.class));
                SplashScreen.this.finish();
            }
        },espera); // 6000=6segundos si no hay BD 3 seg si la hay
    }

    public EventosSQLite getDB (){
        return baseDatos;
    }

}
