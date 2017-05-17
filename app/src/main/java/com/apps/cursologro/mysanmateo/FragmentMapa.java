package com.apps.cursologro.mysanmateo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edu on 09/05/2017.
 */

public class FragmentMapa extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    public ArrayList<Evento> eventosBD = new ArrayList<>();

    public FragmentMapa() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static FragmentMapa newInstance(int sectionNumber) {
        System.out.println("Estamos en newInstance de FragmentMapa: " +sectionNumber);
        FragmentMapa fragment = new FragmentMapa();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Estamos en onCreateView FragmentMapa");
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng coords;
                LatLng center = new LatLng(42.466676, -2.439315);

                //Aplicamos el estilo personalizado de mapa
                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.formato_mapa));

                eventosBD = Listado.listado.getObjeto();
                if (eventosBD.size()!=0){
                System.out.println("Estamos cargando el mapa " +eventosBD.get(0).getTitle());

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                for (Evento evento : eventosBD){
                    coords = new LatLng(evento.getLat(), evento.getLng());
                    mMap.addMarker(new MarkerOptions().position(coords).title(evento.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_red)));
                    mMap.setInfoWindowAdapter(new CustomInfoWindow(getActivity().getLayoutInflater(), evento, getContext()));

                }}
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 13.0f));

            }
        });

        return rootView;
    }

    public void recargar(){
        googleMap.clear();
        GoogleMap mMap = googleMap;
        LatLng coords = new LatLng(42.466676, -2.439315);
        System.out.println("Ejecutamos FragmentMapa recargar");

        //Aplicamos el estilo personalizado de mapa
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.formato_mapa));

        eventosBD = Listado.listado.getObjeto();
        System.out.println("Estamos recargando el mapa con " +eventosBD.size());

        for (Evento evento : eventosBD){
            coords = new LatLng(evento.getLat(), evento.getLng());
            mMap.addMarker(new MarkerOptions().position(coords).title(evento.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_red)));

        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 13.0f));

    }

    public void mostrarEtiqueta(Integer id_evento){
        System.out.println("Intentamos mostrar la etiqueta del evento " +id_evento);
        GoogleMap mMap = googleMap;
        eventosBD = Listado.listado.getObjeto();
        LatLng coords = new LatLng(42.466676, -2.439315);

        for (Evento evento : eventosBD){
            coords = new LatLng(evento.getLat(), evento.getLng());
            //System.out.println("Intentamos comparar " +evento.getId() +" con " +id_evento);
            if (evento.getId().equals(id_evento)){
                //System.out.println("Intentamos comparar " +evento.getId() +" con " +id_evento +" OK");
                mMap.addMarker(new MarkerOptions().position(coords).title(evento.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_red))).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 16.0f));
            } else {
                mMap.addMarker(new MarkerOptions().position(coords).title(evento.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_red)));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        System.out.println("Estamos en FragmentMapa onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        System.out.println("Estamos en FragmentMapa onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        System.out.println("Estamos en FragmentMapa onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        System.out.println("Estamos en FragmentMapa onLowMemory");
    }

}

