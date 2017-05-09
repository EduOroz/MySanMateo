package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Listado extends AppCompatActivity {

    //Declarado para jugar con el icono de búsqueda
    TextView textView;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private Listado.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //Variables para la conexión a BD
    static EventosSQLite baseDatos;

    //Elementos para manejar la Lista de Eventos
    static ListView listaEventos;
    static Context myContext;

    //Elementos para guardar los eventos que recogemos en el WS
    static Evento evento;
    static ArrayList<Evento> eventos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        textView = (TextView) findViewById(R.id.textView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mSectionsPagerAdapter = new Listado.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Creamos un listener para controlar cuando cambiemos de ventana en la actividad
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("onPageScrolled estamos en position: " +position);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        System.out.println("OnPageSelected estamos en sección 0");
                        break;
                    case 1:
                        System.out.println("OnPageSelected estamos en sección 1");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("onPageScrollStateChanged estamos en state: " +state);
            }
        });

        //Cargamos el elemento para mostrar la sección en la que estamos
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        listaEventos = (ListView) findViewById(R.id.lvListado);
        myContext = this;

        //Instanciamos la BD
        baseDatos = new EventosSQLite(this);

        //Revisamos Si existe el archivo SQLite, si no lo creamos
        if(existeBD("DBEventos")){
            //
            System.out.println("La Base de Datos existe");
        }
        else {
            System.out.println("La Base de Datos no existe");

            //Realizamos una consulta al web service para traernos la información de los eventos a mostrar
            StringRequest stringRequest = new StringRequest(Request.Method.GET, " https://testgestorlogrono.get-app.es/api/events",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("URL_CHECK_ENABLE_FORM", "Response:"+response);
                            System.out.println("Respuesta: " +response);
                            //ArrayList<Evento> p = new ArrayList<>();
                            try{
                                //creamos un array JSON a partir de la cadena recibida
                                JSONObject jobj = new JSONObject(response);
                                if (jobj!=null){
                                /*p = new Gson().fromJson(jarray.toString(), new TypeToken<List<Evento>>(){}.getType());*/
                                    JSONArray jarray=(JSONArray) jobj.get("modified");
                                    for (int i=0;i<jarray.length();i++){
                                        JSONObject jobject = jarray.getJSONObject(i);
                                        System.out.println("objeto " +i +" id es: " +jobject.get("id"));
                                        JSONArray jarray_horario=(JSONArray) jobject.get("schedules");
                                        evento = new Evento (jobject.getString("place"), jobject.getInt("thematic_id"), jobject.getString("title"), jobject.getString("text"), jobject.getString("publication_date"), jobject.getString("link"), jobject.getString("address"), jobject.getDouble("lat"), jobject.getDouble("lng"), jarray_horario.getJSONObject(0).getString("start_date"), jarray_horario.getJSONObject(0).getString("finish_date"), jarray_horario.getJSONObject(0).getString("start_time"), jarray_horario.getJSONObject(0).getString("finish_time"));
                                        System.out.println("evento place: " +evento.getPlace());
                                        eventos.add(evento);

                                        //insertamos en baseDatos
                                        baseDatos.insertarEvento(evento);
                                    }
                                    System.out.println("eventos 0 place: " +eventos.get(0).getPlace());

                                }
                            }
                            catch(JSONException ex){
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
        }
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

    /**
     * Metodo privado que recupera todos los eventos existentes de la base de datos.
     */
    static void recuperarEventos() {
        try {
            // Devuelve todos los eventos en el objeto Cursor.
            Cursor cursor = baseDatos.obtenerEventos();
            System.out.println("numero columnas en el cursor " +cursor.getColumnCount());
            System.out.println("cursor string 1 " +cursor.getString(1));

            // Find ListView to populate -> Variable global listaEventos

            // Setup cursor adapter using cursor from last step
            ListadoCursorAdapter todoAdapter = new ListadoCursorAdapter(myContext, cursor);
            // Attach cursor adapter to the ListView
            listaEventos.setAdapter(todoAdapter);

        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "El mensaje de error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("El mensaje de error es: " + e.getMessage());
        } finally {
            // Se cierra la base de datos.
            baseDatos.cerrar();
        }
    }

    /**
     * Metodo privado que recupera los eventos de nombre like '%%' existentes de la base de datos.
     */
    static void recuperarEventosPorNombre(String nombre) {
        try {
            // Devuelve todos los eventos en el objeto Cursor.
            Cursor cursor = baseDatos.obtenerEventosPorNombre(nombre);
            System.out.println("numero columnas en el cursor " +cursor.getColumnCount());
            System.out.println("cursor string 1 " +cursor.getString(1));

            // Find ListView to populate -> Variable global listaEventos

            // Setup cursor adapter using cursor from last step
            ListadoCursorAdapter todoAdapter = new ListadoCursorAdapter(myContext, cursor);
            // Attach cursor adapter to the ListView
            listaEventos.setAdapter(todoAdapter);

        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "El mensaje de error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("El mensaje de error es: " + e.getMessage());
        } finally {
            // Se cierra la base de datos.
            baseDatos.cerrar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners...

        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Listado.this, R.string.submitted, Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                //System.out.println("query: " +query);
                recuperarEventosPorNombre(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //textView.setText(newText);
                recuperarEventosPorNombre(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private GoogleMap mMap;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static Listado.PlaceholderFragment newInstance(int sectionNumber) {
            System.out.println("Estamos en newInstance de sectionNumber: " +sectionNumber);
            Listado.PlaceholderFragment fragment = new Listado.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //Chequeamos en que sección estamos y cargamos el fragmento de listado o el de mapa
            if ((getArguments().getInt(ARG_SECTION_NUMBER))==1) {
                System.out.println("Estamos en sección : " +getArguments().getInt(ARG_SECTION_NUMBER));
                View rootView = inflater.inflate(R.layout.fragment_listado, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                listaEventos = (ListView) rootView.findViewById(R.id.lvListado);
                recuperarEventos();

                return rootView;
            } else {
                System.out.println("Estamos en sección : " +getArguments().getInt(ARG_SECTION_NUMBER));
                View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
                System.out.println("id mapa: " +R.id.map);
                /*SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);*/
                return rootView;
            }
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Añade un marcador en Ayuntamiento y mueve la camara
            LatLng logro = new LatLng(42.466676, -2.439315);
            mMap.addMarker(new MarkerOptions().position(logro).title("Marcador Ayuntamiento de Logroño"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(logro, 13.0f));

            // Añade un marcador en C.C. Berceo y mueve la camara
            LatLng berceo = new LatLng(42.462826, -2.420469);
            mMap.addMarker(new MarkerOptions().position(berceo).title("Marcador C.C. Berceo"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(berceo, 13.0f));

            // Añade un marcador en Escuelas Trevijano y mueve la camara
            LatLng escuelas = new LatLng(42.466640, -2.450672);
            mMap.addMarker(new MarkerOptions().position(escuelas).title("Marcador Escuelas Trevijano"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(escuelas, 13.0f));
        }

    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return Listado.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LISTADO";
                case 1:
                    return "MAPA";
            }
            return null;
        }
    }
}