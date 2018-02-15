package com.example.albert.proyectogps;


public class Anotacion {
        private String  Titulo;
        private String Descripción;
        private String Fecha ;
        private String hora ;
        private String Dirección;
        private String Prioridad;
    private String aaaaaaa;
    public Anotacion(String titulo, String descripción, String fecha, String hora, String dirección, String prioridad) {
        Titulo = titulo;
        Descripción = descripción;
        Fecha = fecha;
        this.hora = hora;
        Dirección = dirección;
        Prioridad = prioridad;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripción() {
        return Descripción;
    }

    public void setDescripción(String descripción) {
        Descripción = descripción;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDirección() {
        return Dirección;
    }

    public void setDirección(String dirección) {
        Dirección = dirección;
    }

    public String getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(String prioridad) {
        Prioridad = prioridad;
    }
}
