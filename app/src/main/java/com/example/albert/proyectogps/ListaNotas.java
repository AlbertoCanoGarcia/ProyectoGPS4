package com.example.albert.proyectogps;

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
import android.widget.Toast;

import java.util.ArrayList;

public class ListaNotas extends AppCompatActivity {

    SQLiteDatabase db;

    ListView listv;
    ArrayList<String> lista;
    ArrayAdapter adaptador;

    Category cat = null;



    int i = 0; int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);


        final ArrayList<Category> category = new ArrayList<Category>();
         //instanciamos el arrayList que va a ir el el AdapterCategory

        db=openOrCreateDatabase("gps_ubicaciones",MODE_PRIVATE,null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS ubicaciones(titulo VARCHAR,prioridad VARCHAR,categoria VARCHAR, " +
           //     "descripcion VARCHAR, direccion VARCHAR, fecha DATE, hora VARCHAR);");

        String sqlLeerTodo = "SELECT * FROM ubicaciones;";

        try{

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

            listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
              int tareaselec = position ;
              Intent ver = new Intent(ListaNotas.this, NuevaTarea.class);
              ver.putExtra("Id",position);
              startActivityForResult(ver, 0);

            }
            });


        }
        catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
