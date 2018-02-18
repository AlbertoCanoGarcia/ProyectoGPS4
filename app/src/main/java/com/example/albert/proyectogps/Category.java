package com.example.albert.proyectogps;

import android.graphics.drawable.Drawable;

/**
 * Created by pilar on 18/02/2018.
 */

public class Category {

    private String title;
    private String categoryId;
    private String description;
    private String date;
    private Drawable imagen;

    public Category() {
        super();
    }

    public Category(String categoryId, String title, String description, String date, Drawable imagen) {
        super();
        this.title = title;
        this.description = description;
        this.date = date;
        this.imagen = imagen;
        this.categoryId = categoryId;
    }


    public String getTitle() {
        return title;
    }

    public void setTittle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Drawable getImage() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public String getCategoryId(){return categoryId;}

    public void setCategoryId(String categoryId){this.categoryId = categoryId;}

}
