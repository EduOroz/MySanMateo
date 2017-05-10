package com.apps.cursologro.mysanmateo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Edu on 08/05/2017.
 */

public class EventosSQLite extends SQLiteOpenHelper {

    // Ruta por defecto de las bases de datos en el sistema Android.
    private static String RUTA_BASE_DATOS = "/data/data/com.apps.edu.pruebavolleyedu/databases/";

    // Nombre de la Base de Datos.
    private static String NOMBRE_BASE_DATOS = "DBEventos";

    // Version de la Base de Datos.
    private static final int VERSION_BASE_DATOS = 1;

    // Objeto Base de Datos.
    private SQLiteDatabase base_datos;

    // Objeto Contexto.
    private Context contexto;

    // Constante privada
    private String SENTENCIA_SQL_CREAR_BASE_DATOS_EVENTOS = "CREATE TABLE if not exists Eventos (_id INTEGER PRIMARY KEY autoincrement, " + "title TEXT, place TEXT, thematic_id INTEGER, text TEXT, publication_date TEXT, link TEXT, address TEXT, lat REAL, lng REAL, start_date TEXT, finish_date TEXT, start_time TEXT, finish_time TEXT)";


    /**
     * Constructor
     * Toma referencia hacia el contexto de la aplicación que lo invoca para poder acceder a los 'assets' y
     * 'resources' de la aplicación.
     * Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de la base de datos.
     * @param context
     */
    public EventosSQLite(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
        this.contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta la sentencia SQL de creación de la tabla Eventos.
        System.out.println("onCreate de la Base de Datos");
        db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_EVENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla Eventos.
        db.execSQL("DROP TABLE IF EXISTS Eventos");

        // Se crea la nueva versión de la tabla EVENTOS.
        db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_EVENTOS);
    }
    /**
     * Metodo publico para insertar una nuevo evento.
     */
    public void insertarEvento(Evento evento){
        ContentValues valores = new ContentValues();
        valores.put("title", evento.getTitle());
        valores.put("place", evento.getPlace());
        valores.put("thematic_id", evento.getThematic_id());
        valores.put("text", evento.getText());
        valores.put("publication_date", evento.getPublication_date());
        valores.put("link", evento.getLink());
        valores.put("address", evento.getAddress());
        valores.put("lat", evento.getLat());
        valores.put("lng", evento.getLng());
        valores.put("start_date", evento.getStart_date());
        valores.put("finish_date", evento.getFinish_date());
        valores.put("start_time", evento.getStart_time());
        valores.put("finish_time", evento.getFinish_time());
        this.getWritableDatabase().insert("Eventos", null, valores);
        System.out.println("Se inserta registro " +valores.get("title") +"lat " +valores.get("lat") +"lng " +valores.get("lng"));
    }

    /**
     * Metodo publico que cierra la base de datos.
     */
    public void cerrar(){
        this.close();
    }

    /**
     * Metodo publico que devuelve todas los eventos
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventos(){
        String[] columnas = new String[]{"_id", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time"};
        Cursor cursor = this.getReadableDatabase().query("Eventos", columnas, null, null, null, null,columnas[0]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Metodo publico que devuelve todas los eventos por Nombre
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventosPorNombre(String nombre){
        String[] columnas = new String[]{"_id", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time"};
        String[] condiciones = new String[]{"%"+nombre+"%"};
        Cursor cursor = this.getReadableDatabase().query("Eventos", columnas, "title like ?", condiciones, null, null,columnas[0]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
