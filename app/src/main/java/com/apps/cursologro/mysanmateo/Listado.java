package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.hardware.camera2.params.BlackLevelPattern;
import android.support.annotation.MainThread;
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

import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    /**
     * Creamos este objeto para pasar el array al otro fragmento, lo inicializaremos en onCreate
     * y luego crearemos un método público que nos devuelva el objeto getObjecto()
     */
    public static Listado listado;

    public FragmentLista fragmentLista;
    public static FragmentMapa fragmentMapa;

    //Elementos para manejar la Lista de Eventos
    public static ListView mylistaEventos;
    static Context myContext;

    //Variables para la conexión a BD
    static EventosSQLite baseDatos;

    //Elementos para guardar los eventos que recogemos en el WS
    static Evento evento;
    static ArrayList<Evento> eventosBD = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        //Nos traemos la base de Datos abierta en MainActivty
        baseDatos = MainActivity.main.getDB();

        listado = this;

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

        fragmentLista = new FragmentLista();
        fragmentMapa = new FragmentMapa();

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

        //Cambiamos el tipo de letra de los textos en el tab layout
        Typeface faceLMedium= Typeface.createFromAsset(this.getAssets(),"fonts/Lato-Medium.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        System.out.println("Intentamos cambiar el tipo de letra vg.getChildCount " +tabsCount);
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(faceLMedium);
                }
            }
        }

        myContext = this;
    }

    /**
     * Metodo privado que recupera todos los eventos existentes de la base de datos.
     */
    static void recuperarEventos(ListView listaEventos) {
        String title;
        String place;
        eventosBD = new ArrayList<>();
        try {
            // Devuelve todos los eventos en el objeto Cursor.
            Cursor cursor = baseDatos.obtenerEventos();
            System.out.println("RE numero columnas en el cursor " +cursor.getColumnCount());
            System.out.println("RE numero resultados " +cursor.getCount());

            //Recorremos el cursor y guardamos los eventos en evetosBD
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex("title"));
                place = cursor.getString(cursor.getColumnIndex("place"));
                evento = new Evento(cursor.getInt(cursor.getColumnIndex("id_evento")),
                        place,
                        cursor.getInt(cursor.getColumnIndex("thematic_id")),
                        title, cursor.getString(cursor.getColumnIndex("text")),
                        cursor.getString(cursor.getColumnIndex("publication_date")),
                        cursor.getString(cursor.getColumnIndex("link")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getDouble(cursor.getColumnIndex("lat")),
                        cursor.getDouble(cursor.getColumnIndex("lng")),
                        cursor.getString(cursor.getColumnIndex("start_date")),
                        cursor.getString(cursor.getColumnIndex("finish_date")),
                        cursor.getString(cursor.getColumnIndex("start_time")),
                        cursor.getString(cursor.getColumnIndex("finish_time")),
                        cursor.getString(cursor.getColumnIndex("title_categoria")));
                eventosBD.add(evento);
                System.out.println("En cursor recuperarEventos " +evento.getTitle() +"lat " +evento.getLat());
            }

            // Find ListView to populate -> Variable global listaEventos

            // Setup cursor adapter using cursor from last step
            ListadoCursorAdapter todoAdapter = new ListadoCursorAdapter(myContext, cursor);
            // Attach cursor adapter to the ListView
            System.out.println("ID de listaEventos en recuperar: " +R.id.lvListado);
            mylistaEventos = listaEventos;
            listaEventos.setAdapter(todoAdapter);

        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "El mensaje de error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("El mensaje de error es: " + e.getMessage());
        } finally {
            // Se cierra la base de datos.
            //baseDatos.cerrar();
        }
    }

    /**
     * Metodo privado que recupera los eventos de nombre like '%%' existentes de la base de datos.
     */
    static void recuperarEventosPorNombre(String nombre) {
        String title;
        String place;
        eventosBD = new ArrayList<>();
        try {
            // Devuelve todos los eventos en el objeto Cursor.
            Cursor cursor = baseDatos.obtenerEventosPorNombre(nombre);
            System.out.println("REN numero columnas en el cursor " +cursor.getColumnCount());
            System.out.println("REN numero resultados " +cursor.getCount());

            //Recorremos el cursor y guardamos los eventos en evetosBD
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex("title"));
                place = cursor.getString(cursor.getColumnIndex("place"));
                evento = new Evento(cursor.getInt(cursor.getColumnIndex("id_evento")),
                        place, cursor.getInt(cursor.getColumnIndex("thematic_id")),
                        title, cursor.getString(cursor.getColumnIndex("text")),
                        cursor.getString(cursor.getColumnIndex("publication_date")),
                        cursor.getString(cursor.getColumnIndex("link")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getDouble(cursor.getColumnIndex("lat")),
                        cursor.getDouble(cursor.getColumnIndex("lng")),
                        cursor.getString(cursor.getColumnIndex("start_date")),
                        cursor.getString(cursor.getColumnIndex("finish_date")),
                        cursor.getString(cursor.getColumnIndex("start_time")),
                        cursor.getString(cursor.getColumnIndex("finish_time")));
                eventosBD.add(evento);
                System.out.println("En cursor recuperarEventosPorNombre " +evento.getTitle() +"lat " +evento.getLat());
            }
            // Find ListView to populate -> Variable global listaEventos

            // Setup cursor adapter using cursor from last step
            ListadoCursorAdapter todoAdapter = new ListadoCursorAdapter(myContext, cursor);
            // Attach cursor adapter to the ListView
            mylistaEventos.setAdapter(todoAdapter);

        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "El mensaje de error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("El mensaje de error es: " + e.getMessage());
        } finally {
            // Se cierra la base de datos.
            //baseDatos.cerrar();
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
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(20);
        Typeface faceLItalic= Typeface.createFromAsset(myContext.getAssets(),"fonts/Lato-Italic.ttf");
        autoComplete.setTypeface(faceLItalic);

        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint(getText(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(Listado.this, R.string.submitted, Toast.LENGTH_SHORT).show();
                //se oculta el EditText
                if (query.equals("")){
                    searchView.setIconified(true);
                    searchItem.collapseActionView();
                    recuperarEventos(mylistaEventos);
                    fragmentMapa.recargar();
                }
                else {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    searchItem.collapseActionView();
                    //System.out.println("query: " +query);
                    recuperarEventosPorNombre(query);
                    fragmentMapa.recargar();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //textView.setText(newText);
                recuperarEventosPorNombre(newText);
                fragmentMapa.recargar();
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
        System.out.println("Hacemos click en boton con id " +id +" " +R.id.action_search);
        if (id == R.id.action_search){
            recuperarEventos(mylistaEventos);
        }
        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
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
            if (position==0) {
                System.out.println("Devolvemos fragmentLista");
                return fragmentLista;
            } else {
                System.out.println("Devolvemos fragmentMapa");
                return fragmentMapa;}
        }

        /*@Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }*/

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

    public ArrayList<Evento> getObjeto(){
        return eventosBD;
    }
    public void cambiarMapa(){
        mViewPager.setCurrentItem(1);
    }
    public void cambiarListado(){
        mViewPager.setCurrentItem(0);
    }

}
