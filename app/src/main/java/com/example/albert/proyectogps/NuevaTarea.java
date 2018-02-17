package com.example.albert.proyectogps;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class NuevaTarea extends AppCompatActivity {

    EditText nuevafecha;
    EditText nuevahora;
    int year,month,day,hora,min;
    Button btn;
    EditText tit, pri, cat, desc, direc;

    Calendar calendar= Calendar.getInstance();

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        abrirBD();
        btn=(Button)findViewById(R.id.bd);
        tit=(EditText)findViewById(R.id.ettitulo);
        pri=(EditText)findViewById(R.id.etprioridad);
        cat=(EditText)findViewById(R.id.etprioridad);
        desc=(EditText)findViewById(R.id.etcategoria);
        direc=(EditText)findViewById(R.id.etdireccion);

        nuevafecha=(EditText)findViewById(R.id.etfecha);
        nuevahora=(EditText)findViewById(R.id.ethora);
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hora=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);
        nuevafecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(NuevaTarea.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        nuevafecha.setText(day + "/" + (month+1)+"/" + year);
                    }
                },day,(year+2018),month);
                datePickerDialog.show();

            }
        });
        nuevahora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(NuevaTarea.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        nuevahora.setText(hora + ":" + min);
                    }
                },hora,min,true);
                timePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatos(tit.getText().toString(),desc.getText().toString(),cat.getText().toString(),pri.getText().toString(),
                        direc.getText().toString(),nuevafecha.getText().toString(),nuevahora.getText().toString());
            }
        });
    }

    public void abrirBD(){
        db= openOrCreateDatabase("gps_ubicaciones",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS ubicaciones (titulo VARCHAR, descripcion VARCHAR," +
                "categoria VARCHAR, prioridad VARCHAR, direccion VARCHAR, fecha DATE, hora VARCHAR)");
    }
    public void insertarDatos(String titulo,String descripcion,String categoria,
                              String prioridad,String direccion, String fecha,String hora){
        db.execSQL("INSERT INTO ubicaciones VALUES('"+titulo+","+descripcion+","+categoria+","+prioridad+","+direccion+","+fecha+","+hora+"');");
    }
    public void cargarDatos(){
        Cursor c=db.rawQuery("SELECT * FROM ubicaciones",null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            //añadir los datos a la lista
            //falta poner la instruccion para ello
            c.moveToNext();
        }
    }

}
