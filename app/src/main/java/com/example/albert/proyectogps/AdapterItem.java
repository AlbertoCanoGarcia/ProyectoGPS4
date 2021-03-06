package com.example.albert.proyectogps;

/**
 * Created by pilar on 18/02/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterItem extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Anotacion> items;

    public AdapterItem (Activity activity, ArrayList<Anotacion> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Anotacion> category) {
        for (int i =0 ; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_fila, null);
        }

        Anotacion dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.txf_titulo);
        title.setText(dir.getTitulo());

        TextView description = (TextView) v.findViewById(R.id.txf_datos);
        description.setText(dir.getDescripción());

        TextView date = (TextView) v.findViewById(R.id.txf_fecha);
        date.setText(dir.getFecha());



        return v;
    }
}
