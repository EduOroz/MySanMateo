package com.apps.cursologro.mysanmateo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Edu on 09/05/2017.
 */

public class FragmentMapa extends Fragment implements OnMapReadyCallback {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap mMap;

    public FragmentMapa() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static FragmentMapa newInstance(int sectionNumber) {
        System.out.println("Estamos en newInstance de sectionNumber: " +sectionNumber);
        FragmentMapa fragment = new FragmentMapa();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         System.out.println("Estamos en sección : " +getArguments().getInt(ARG_SECTION_NUMBER));
         View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
         System.out.println("id mapa: " +R.id.map);
         SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
         return rootView;
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

