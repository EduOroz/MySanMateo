package com.apps.cursologro.mysanmateo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btPrueba = (Button) findViewById(R.id.btPrueba);

        Typeface face= Typeface.createFromAsset(getAssets(),"fonts/Lato-BoldItalic.ttf");
        Typeface face2= Typeface.createFromAsset(getAssets(),"fonts/Roboto-Italic.ttf");
        TextView tvSaludo = (TextView) findViewById(R.id.tvSaludo);
        tvSaludo.setTypeface(face);
        btPrueba.setTypeface(face2);

        btPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Listado.class);
                startActivity(intent);
            }
        });

    }
}
