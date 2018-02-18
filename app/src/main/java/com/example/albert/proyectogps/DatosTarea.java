package com.example.albert.proyectogps;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by pilar on 18/02/2018.
 */

public class DatosTarea extends AppCompatActivity {
    EditText fec;
    EditText hor;
    EditText tit;
    EditText pri;
    EditText des;
    EditText direc;
    EditText cat;
    EditText lat;
    EditText lon;

    ArrayList<Anotacion> arrayanotaciones;

    SQLiteDatabase db;
    int x = 0, i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_nota);
        fec = (EditText) findViewById(R.id.etfecha);
        hor = (EditText) findViewById(R.id.ethora);
        tit = (EditText) findViewById(R.id.ettitulo);
        pri = (EditText) findViewById(R.id.etprioridad);
        des = (EditText) findViewById(R.id.etdescripcion);
        direc = (EditText) findViewById(R.id.etdireccion);
        cat = (EditText) findViewById(R.id.etcategoria);
        lat = (EditText) findViewById(R.id.etlatitud);
        lon = (EditText) findViewById(R.id.etlongitud);

        arrayanotaciones = new ArrayList<Anotacion>();

        db=openOrCreateDatabase("gps_ubicaciones",MODE_PRIVATE,null);

        String sqlLeerTodo = "SELECT * FROM ubicaciones;";

        Cursor c = db.rawQuery(sqlLeerTodo, null);

        /*c.moveToFirst();
        while (c != null){
            x++;
            c.moveToNext();
        }

        String titulo [] = new String[x];
        String desc [] = new String[x];
        String fecha [] = new String[x];
        String hora [] = new String[x];
        String prio [] = new String[x];
        String cate [] = new String[x];
        String dir [] = new String[x];
        String lati [] = new String[x];
        String longi [] = new String[x];
*/

        int indiceTitulo = c.getColumnIndex("titulo");
        int indiceDescripcion = c.getColumnIndex("descripcion");
        int indiceFecha = c.getColumnIndex("fecha");
        int indiceHora = c.getColumnIndex("hora");
        int indicePrio = c.getColumnIndex("prioridad");
        int indiceCate = c.getColumnIndex("categoria");
        int indiceDir = c.getColumnIndex("direccion");
        int indiceLati = c.getColumnIndex("latitud");
        int indiceLongi = c.getColumnIndex("longitud");

        // Movemos el cursor al primer resultado
        c.moveToFirst();

        // Recorremos el resto de resultados
        if(c.getCount()!=0) {
            arrayanotaciones.clear();
            int i = 0;
            while (i < c.getCount()) {
                Anotacion anotacion =
                        new Anotacion(c.getString(indiceTitulo), c.getString(indiceDescripcion)
                                , c.getString(indiceFecha),
                                c.getString(indiceHora), c.getString(indiceDir),
                                c.getString(indicePrio), c.getString(indiceLati), c.getString(indiceLongi));
                arrayanotaciones.add(anotacion);
                c.moveToNext();
                i++;
            }
        }

        Bundle bundle = getIntent().getExtras();
        int pos=Integer.parseInt(bundle.getString("nota"));

        tit.setText(arrayanotaciones.get(pos).getTitulo());
        des.setText(arrayanotaciones.get(pos).getDescripción());
        hor.setText(arrayanotaciones.get(pos).getHora());
        fec.setText(arrayanotaciones.get(pos).getFecha());
        direc.setText(arrayanotaciones.get(pos).getDirección());
        lat.setText(arrayanotaciones.get(pos).getLatitud());
        lon.setText(arrayanotaciones.get(pos).getLongitud());

        Button btn2 = (Button) findViewById(R.id.volver);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (v.getContext(), ListaNotas.class);
                startActivityForResult(intent2, 0);
            }
        });

    }

}
