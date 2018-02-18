package com.example.albert.proyectogps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaNotas extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<Anotacion> arrayanotaciones;
    ListView listv;
    ArrayList<String> lista;
    ArrayAdapter<String> adaptador;
    Context con=this;
    Activity activity=this;

    int i = 0; int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        listv = (ListView) findViewById(R.id.listNot);




        arrayanotaciones = new ArrayList<Anotacion>();
         //instanciamos el arrayList que va a ir el el Adapter

        db=openOrCreateDatabase("gps_ubicaciones",MODE_PRIVATE,null);

        CargarDatos();
        //db.execSQL("CREATE TABLE IF NOT EXISTS ubicaciones(titulo VARCHAR,prioridad VARCHAR,categoria VARCHAR, " +
           //     "descripcion VARCHAR, direccion VARCHAR, fecha DATE, hora VARCHAR);");
/*
        String sqlLeerTodo = "SELECT * FROM ubicaciones;";

        Cursor c = db.rawQuery(sqlLeerTodo, null);
        c.moveToFirst();
        while (c != null){
            x++;
            c.moveToNext();
        }

        String titulo [] = new String[x];
        String desc [] = new String[x];
        String fecha [] = new String[x];

        // Obtenemos los índices de las columnas de "nombre" y "edad". Esto nos
        // permitirá acceder más tarde a estas columnas
        int indiceTitulo = c.getColumnIndex("titulo");
        int indiceDescripcion = c.getColumnIndex("descripcion");
        int indiceFecha = c.getColumnIndex("fecha");
        // Movemos el cursor al primer resultado
        c.moveToFirst();

        Drawable respi = getResources().getDrawable(R.drawable.imgnota, getTheme()); //Instanciamos una imagen ubicada en drawable

        // Recorremos el resto de resultados
        while (c != null){
            titulo[i] = c.getString(indiceTitulo);
            desc[i] = c.getString(indiceDescripcion);
            fecha[i] = c.getString(indiceFecha);

            cat=new Category(""+ i, titulo[i],desc[i],fecha[i],respi); //colocamos los valores en el constructor de category

            category.add(cat); //Agregamos la variable cat instanciada mas arriba al arrayList

            i++;
            c.moveToNext();
        }

        listv=(ListView) findViewById(R.id.listNot); //Inicializamos el ListView

        AdapterItem adapter = new AdapterItem(this, category);
        listv.setAdapter(adapter);
        listv.setClickable(true);
*/
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                int tareaselec = position ;
                Intent ver = new Intent(ListaNotas.this, DatosTarea.class);
                ver.putExtra("nota",arrayanotaciones.get(tareaselec)));
                startActivity(ver);
            }
        });
    }

    public void CargarDatos() {
        Cursor c = db.rawQuery("Select * from ubicaciones", null);
// Obtenemos los índices de las columnas. Esto nos
// permitirá acceder más tarde a estas columnas
        int indicetitulo = c.getColumnIndex("titulo");
        int indicecategoria = c.getColumnIndex("categoria");
        int indicedescripcion = c.getColumnIndex("descripcion");
        int indicefecha = c.getColumnIndex("fecha");
        int indicehora = c.getColumnIndex("hora");
        int indicelatitud = c.getColumnIndex("latitud");
        int indicelongitud = c.getColumnIndex("longitud");
        int indicedireccion = c.getColumnIndex("direccion");
        int indiceprioridad = c.getColumnIndex("prioridad");
// Movemos el cursor al primer resultado
        c.moveToFirst();
// Recorremos el resto de resultados

        if(c.getCount()!=0){
            arrayanotaciones.clear();
            int i=0;
            while (i<c.getCount()) {
                Anotacion anotacion=
                        new Anotacion(c.getString(indicetitulo),c.getString(indicedescripcion)
                                ,c.getString(indicefecha),
                                c.getString(indicehora),c.getString(indicedireccion),
                                c.getString(indiceprioridad),c.getString(indicelatitud),c.getString(indicelongitud));
                arrayanotaciones.add(anotacion);
                c.moveToNext() ;
                i++;
                AdapterItem adapter = new AdapterItem(activity, arrayanotaciones);
                listv.setAdapter(adapter);
                listv.setClickable(true);
            }

            //adaptador = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, arrayanotaciones.toString());
            //lista.setAdapter(adaptador);
        }
    }
}
