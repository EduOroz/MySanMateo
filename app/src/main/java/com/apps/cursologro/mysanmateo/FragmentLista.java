package com.apps.cursologro.mysanmateo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import static com.apps.cursologro.mysanmateo.Listado.recuperarEventos;

/**
 * Created by Edu on 09/05/2017.
 */

public class FragmentLista extends Fragment {

    //ListView listaEventos;

    public FragmentLista() {
    }

    /**
    * Returns a new instance of this fragment for the given section number.
    */
    public static FragmentLista newInstance(int sectionNumber) {
        System.out.println("Estamos en newInstance de FragmentLista " +sectionNumber);
        FragmentLista fragment = new FragmentLista();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Estamos en onCreateView FragmentLista");
        View rootView = inflater.inflate(R.layout.fragment_listado, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Hello from FragmentLista");

        ListView listaEventos = (ListView) rootView.findViewById(R.id.lvListado);
        recuperarEventos(listaEventos);
        return rootView;
    }

}

