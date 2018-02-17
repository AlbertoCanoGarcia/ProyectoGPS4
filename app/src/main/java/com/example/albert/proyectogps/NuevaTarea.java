package com.example.albert.proyectogps;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
    EditText txttitulo;
    EditText txtprioridad;
    EditText txtdescripcion;
    EditText txtdireccion;
    int year,month,day,hora,min;
    Button guardar;
    Calendar calendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        nuevafecha=(EditText)findViewById(R.id.etfecha);
        nuevahora=(EditText)findViewById(R.id.ethora);
        txttitulo=(EditText) findViewById(R.id.ettitulo);
        txtprioridad=(EditText) findViewById(R.id.etprioridad);
        txtdescripcion=(EditText) findViewById(R.id.etdescripcion);
        txtdireccion=(EditText) findViewById(R.id.etdireccion);

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hora=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);

        guardar= (Button) findViewById(R.id.botonguardar);

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

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = txttitulo.getText().toString();
                String descripcion= txtdescripcion.getText().toString();
                String prioridad = txtprioridad.getText().toString();
                String direccion= txtdireccion.getText().toString();
                String fecha= nuevafecha.getText().toString();
                String hora= nuevahora.getText().toString();
                String latitud=null;
                String longitud=null;
                Anotacion a= new Anotacion(titulo,descripcion,fecha,hora,direccion,prioridad,latitud,longitud);
            }
        });
    }

    public void localizacion(){


    }

    }


