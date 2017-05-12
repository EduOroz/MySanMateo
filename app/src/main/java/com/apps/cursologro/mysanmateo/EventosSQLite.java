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
    private String SENTENCIA_SQL_CREAR_TABLA_EVENTOS = "CREATE TABLE if not exists Eventos (_id INTEGER PRIMARY KEY autoincrement, " + "id_evento INTEGER, title TEXT, place TEXT, thematic_id INTEGER, text TEXT, publication_date TEXT, link TEXT, address TEXT, lat REAL, lng REAL, start_date TEXT, finish_date TEXT, start_time TEXT, finish_time TEXT)";
    private String SENTENCIA_SQL_CREAR_TABLA_CATEGORIAS = "CREATE TABLE if not exists Categorias (_idcat INTEGER PRIMARY KEY autoincrement, " + "id_categoria INTEGER, title_categoria TEXT)";
    private String SENTENCIA_SQL_CREAR_TABLA_EVENTOS_ESPECIALES = "CREATE TABLE if not exists EventosEspeciales (_id INTEGER PRIMARY KEY autoincrement, " + "titulo TEXT, subtitulo TEXT, cantidad INTEGER)";

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
        // Se ejecuta la sentencia SQL de creación de las tablas
        System.out.println("onCreate de la Base de Datos");
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_EVENTOS);
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_CATEGORIAS);
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_EVENTOS_ESPECIALES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de las tablas
        db.execSQL("DROP TABLE IF EXISTS Eventos");
        db.execSQL("DROP TABLE IF EXISTS Categorias");
        db.execSQL("DROP TABLE IF EXISTS EventosEspeciales");

        // Se crea la nueva versión de las tablas
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_EVENTOS);
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_CATEGORIAS);
        db.execSQL(SENTENCIA_SQL_CREAR_TABLA_EVENTOS_ESPECIALES);
    }
    /**
     * Metodo publico para insertar una nuevo evento.
     */
    public void insertarEvento(Evento evento){
        ContentValues valores = new ContentValues();
        valores.put("id_evento", evento.getId());
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
        System.out.println("Se inserta registro id " +valores.get("id_evento")  +"title " +valores.get("title") +"lat " +valores.get("lat") +"lng " +valores.get("lng"));
    }

    /**
     * Metodo publico para insertar una nueva categoria.
     */
    public void insertarCategoria(Integer id_categoria, String titulo){
        ContentValues valores = new ContentValues();
        valores.put("id_categoria", id_categoria);
        valores.put("title_categoria", titulo);
        this.getWritableDatabase().insert("Categorias", null, valores);
        System.out.println("Se inserta registro id " +id_categoria  +" title " +titulo);
    }

    /**
     * Metodo publico para insertar eventos especiales
     */
    public void insertarEventosEspeciales(String titulo, String subtitulo, Integer cantidad){
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("subtitulo", subtitulo);
        valores.put("cantidad", cantidad);
        this.getWritableDatabase().insert("EventosEspeciales", null, valores);
        System.out.println("Se inserta evento especial " +titulo  +" cantidad " +cantidad);
    }

    /**
     * Metodo publico que cierra la base de datos.
     */
    public void cerrar(){
        this.close();
    }

    /**
     * Metodo publico que devuelve todas los eventos haciendo un join con categoria
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventos(){
        String[] columnas = new String[]{"_id", "id_evento", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time", "title_categoria"};
        Cursor cursor = this.getReadableDatabase().query("Eventos, Categorias", columnas, "thematic_id=id_categoria and (thematic_id=133 or thematic_id=134)", null, null, null,columnas[11]+" ASC");

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
        String[] columnas = new String[]{"_id", "id_evento", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time", "title_categoria"};
        String[] condiciones = new String[]{"%"+nombre+"%", "%"+nombre+"%"};
        Cursor cursor = this.getReadableDatabase().query("Eventos, Categorias", columnas, "thematic_id=id_categoria and (thematic_id=133 or thematic_id=134) and (title like ? or place like ?) ", condiciones, null, null,columnas[11]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Metodo publico que devuelve todas los eventos de San Mateo Infantil haciendo un join con categoria
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventosInfantiles(){
        String[] columnas = new String[]{"_id", "id_evento", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time", "title_categoria"};
        Cursor cursor = this.getReadableDatabase().query("Eventos, Categorias", columnas, "thematic_id=id_categoria and thematic_id=134", null, null, null,columnas[11]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Metodo publico que devuelve todas los eventos de San Mateo Infantil por Nombre
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventosPorNombreInfantil(String nombre){
        String[] columnas = new String[]{"_id", "id_evento", "title", "place", "thematic_id", "text", "publication_date", "link", "address", "lat", "lng", "start_date", "finish_date", "start_time", "finish_time", "title_categoria"};
        String[] condiciones = new String[]{"%"+nombre+"%", "%"+nombre+"%"};
        Cursor cursor = this.getReadableDatabase().query("Eventos, Categorias", columnas, "thematic_id=id_categoria and thematic_id=134 and (title like ? or place like ?) ", condiciones, null, null,columnas[11]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Metodo publico que devuelve todas los eventos especiales
     * @return un cursor con todos los eventos
     */
    public Cursor obtenerEventosEspeciales(){
        System.out.println("Recuperando los Eventos Especiales");
        String[] columnas = new String[]{"_id", "titulo", "subtitulo", "cantidad"};
        Cursor cursor = this.getReadableDatabase().query("EventosEspeciales", columnas, null, null, null, null,columnas[0]+" ASC");

        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}
