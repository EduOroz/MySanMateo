package com.apps.cursologro.mysanmateo;

/**
 * Created by Edu on 05/05/2017.
 */

public class Evento {
    private Integer id;
    private String place;
    private Integer thematic_id;
    private String title;
    private String text;
    private String publication_date;
    private String link;
    private String address;
    private Double lat;
    private Double lng;
    private String start_date;
    private String finish_date;
    private String start_time;
    private String finish_time;

    //Constructores
    public Evento(){}

    public Evento(Integer id, String place, Integer thematic_id, String title, String text, String publication_date, String link, String address, Double lat, Double lng, String start_date, String finish_date, String start_time, String finish_time) {
        this.id = id;
        this.place = place;
        this.thematic_id = thematic_id;
        this.title = title;
        this.text = text;
        this.publication_date = publication_date;
        this.link = link;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.start_date = start_date;
        this.finish_date = finish_date;
        this.start_time = start_time;
        this.finish_time = finish_time;
    }

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setPlace(Integer id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getThematic_id() {
        return thematic_id;
    }

    public void setThematic_id(Integer thematic_id) {
        this.thematic_id = thematic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }
}
