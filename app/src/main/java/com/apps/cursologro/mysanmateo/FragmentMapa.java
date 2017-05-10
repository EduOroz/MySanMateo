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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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

                eventosBD = Listado.listado.getObjeto();
                System.out.println("Estamos cargando el mapa " +eventosBD.get(0).getTitle());

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

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
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}

