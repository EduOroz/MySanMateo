package com.apps.cursologro.mysanmateo;

import android.app.Fragment;
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
       /**
         * The fragment argument representing the section number for this fragment.
         */
       private static final String ARG_SECTION_NUMBER = "section_number";

       public FragmentLista() {
       }

       /**
       * Returns a new instance of this fragment for the given section number.
       */
       public static FragmentLista newInstance(int sectionNumber) {
           System.out.println("Estamos en newInstance de sectionNumber: " +sectionNumber);
           FragmentLista fragment = new FragmentLista();
           Bundle args = new Bundle();
           args.putInt(ARG_SECTION_NUMBER, sectionNumber);
           fragment.setArguments(args);
           return fragment;
       }

       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
           System.out.println("Estamos en FragmentLista: " +getArguments().getInt(ARG_SECTION_NUMBER));
           View rootView = inflater.inflate(R.layout.fragment_listado, container, false);
           TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

           recuperarEventos();
           return rootView;
       }

}

