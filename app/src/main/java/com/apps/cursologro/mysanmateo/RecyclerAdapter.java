package com.apps.cursologro.mysanmateo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Edu on 19/05/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewRecycleHolder> {

    private ArrayList<Evento> eventos;

    @Override
    public RecyclerAdapter.ViewRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento, parent, false);
        return new ViewRecycleHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewRecycleHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }


    public static class ViewRecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        // Find fields to populate in inflated template
        TextView tvTitle;
        TextView tvPlace;
        TextView tvDay;
        TextView tvHour;
        TextView tvAddress;
        TextView tvThematic;
        ImageView ivWeb;
        ImageView ivLocalizacion;

        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public ViewRecycleHolder(View v) {
            super(v);

            tvTitle = (TextView) v.findViewById(R.id.tvTitulo);
            tvPlace = (TextView) v.findViewById(R.id.tvLugar);
            tvDay = (TextView) v.findViewById(R.id.tvDia);
            tvHour = (TextView) v.findViewById(R.id.tvHora);
            tvAddress = (TextView) v.findViewById(R.id.tvDireccion);
            tvThematic = (TextView) v.findViewById(R.id.tvCategoria);
            ivWeb = (ImageView) v.findViewById(R.id.ivWeb);
            ivLocalizacion = (ImageView) v.findViewById(R.id.ivLocalizacion);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            System.out.println("onClick en el recycle view holder");;
        }

        public void bindEvento(Evento evento) {

        }

    }
}