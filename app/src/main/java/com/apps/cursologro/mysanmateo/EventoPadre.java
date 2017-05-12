package com.apps.cursologro.mysanmateo;

/**
 * Created by Edu on 12/05/2017.
 */

public class EventoPadre {
    String titulo;
    String descripcion;
    Integer cantidad;

    public EventoPadre() {

    }

    public EventoPadre(String titulo, String descripcion, Integer cantidad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
