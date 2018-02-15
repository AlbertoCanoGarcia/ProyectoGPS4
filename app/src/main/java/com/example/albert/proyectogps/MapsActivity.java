package com.example.albert.proyectogps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    double lat = 0.0;
    double lon = 0.0;
    Location ultimaLocalizacion;
    private Marker marcador;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FloatingActionButton btUbicacion;
    private LocationServices servicioUbicacion;
    private Context contexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        contexto = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Obtengo una instancia del servicio de localización del sistema
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //marcarUbicacionActual();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (Build.VERSION.SDK_INT >= 23) {
            // Comprobamos que tengamos el persmiso para acceder a la ubicación
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // Registro un listener de cambios en el posicionamiento por GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // Obtengo la localización actual del usuario y la muestro en el mapa
        ultimaLocalizacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

       // mMap.setMyLocationEnabled(true);
    }

    // Metodo para saber en que coordenadas se encuentra actualmente el usuario
    public LatLng mostrarDatosLocalizacion(Location localizacion) {
        LatLng posicion = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // Obtenemos la dirección asociada a la localización
            List<Address> direcciones =
                    geocoder.getFromLocation(localizacion.getLatitude(),
                            localizacion.getLongitude(), 1);
            Address direccion = direcciones.get(0);
            posicion = new LatLng(direccion.getLatitude(), direccion.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posicion;
    }

    @Override // Metodo que se ejecuta cuando se cargar el mapa
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marcarUbicacionActual();
        ubicarUsuario();
    }

    public void marcarUbicacionActual() {
        LatLng posicion = mostrarDatosLocalizacion(ultimaLocalizacion); // posicion actual
        LatLng posicion2 = new LatLng(39.467494, -3.52829); // posicion destino
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 17f)); // hacemos zoom a esa posicion
        mMap.addMarker(new MarkerOptions()
                .position(posicion)
                .title("Tu ubicacion")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.posicionactual)));
        mMap.addMarker(new MarkerOptions()
                .position(posicion2));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private void ubicarUsuario() {

        btUbicacion = findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    Location lastLocation = ultimaLocalizacion;
                    if (lastLocation != null)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mostrarDatosLocalizacion(ultimaLocalizacion), 16f));

                    if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    //mMap.setMyLocationEnabled(true);
                }
            }
        });
    }



}
