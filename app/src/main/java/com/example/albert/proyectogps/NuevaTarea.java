package com.example.albert.proyectogps;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class NuevaTarea extends AppCompatActivity {
    EditText nuevafecha;
    EditText nuevahora;
    EditText tit;
    EditText pri;
    EditText desc;
    EditText direc;
    EditText cat;
    int year,month,day,hora,min;
    Button btn;
    Calendar calendar= Calendar.getInstance();

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        nuevafecha=(EditText)findViewById(R.id.etfecha);
        nuevahora=(EditText)findViewById(R.id.ethora);
        tit=(EditText) findViewById(R.id.ettitulo);
        pri=(EditText) findViewById(R.id.etprioridad);
        desc=(EditText) findViewById(R.id.etdescripcion);
        direc=(EditText) findViewById(R.id.etdireccion);
        cat=(EditText) findViewById(R.id.etcategoria);

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hora=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);

        abrirBD();
        btn= (Button) findViewById(R.id.botonguardar);

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
            public void onClick(View view) {
                insertarDatos(tit.getText().toString(),pri.getText().toString(),cat.getText().toString(),
                        desc.getText().toString(),direc.getText().toString(),nuevafecha.getText().toString(),nuevahora.getText().toString());
            }
        });
    }

    public void abrirBD(){
        db=openOrCreateDatabase("gps_ubicaciones",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS ubicaciones(titulo VARCHAR,prioridad VARCHAR,categoria VARCHAR, " +
                "descripcion VARCHAR, direccion VARCHAR, fecha DATE, hora VARCHAR);");

    }

    public void insertarDatos(String titulo,String prioridad,String categoria,String descripcion,String direccion, String fecha,String hora){
        db.execSQL("INSERT INTO ubicaciones VALUES('"+titulo+","+prioridad+","+categoria+","+descripcion+","+direccion+","+fecha+","+hora+"');");

    }
    
    public void cargarDatos(){
        Cursor c=db.rawQuery("SELECT * FROM ubicaciones",null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            //añadir codigo para que aparezca en la lista
            c.moveToNext();
        }
    }

}


