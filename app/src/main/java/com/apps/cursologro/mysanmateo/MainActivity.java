package com.apps.cursologro.mysanmateo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        private ListView list;
        private String[] programa = {"PROGRAMA DE SAN MATEOS COMPLETO", "PROGRAMA INFANTIL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getSupportActionBar().hide();

        list = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, programa);
        list.setAdapter(adaptador);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();

            }

        });

        /* Button btPrueba = (Button) findViewById(R.id.btPrueba);

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
        }); */

    }
}
