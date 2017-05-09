package com.apps.cursologro.mysanmateo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Edu on 08/05/2017.
 */

public class ListadoCursorAdapter extends CursorAdapter {
    public ListadoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.elemento, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvPlace = (TextView) view.findViewById(R.id.place);
        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String place = cursor.getString(cursor.getColumnIndexOrThrow("place"));
        // Populate fields with extracted properties
        tvTitle.setText(title);
        tvPlace.setText(place);
    }
}
