package com.example.albert.proyectogps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.light.Position;
import com.mapbox.services.Constants;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMapa extends AppCompatActivity  {
    double lat = 0.0;
    double lon = 0.0;
    Location ultimaLocalizacion;
    private MapView mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapboxMap mapa;
    private FloatingActionButton btUbicacion;
    private Context contexto;
    private final static  int MY_PERMISSION_FINE_LOCATION=101;
    Double latitud;
    Double longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Mapbox.getInstance(this, "pk.eyJ1IjoiYWxiZXJ0b2Nhbm9nYXJjaWEiLCJhIjoiY2pkcmFxNTk4MjAzODMzdDdlczEwb2RvbyJ9.ivhOGtFgvddeXkcuRqFWVw");
        mMap = (MapView) findViewById(R.id.mapView);
        mMap.onCreate(savedInstanceState);


        // Obtenemos el servicio para saber todo lo referido con nuestra localizacion

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mapa.setMyLocationEnabled(true);


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

        // Cuando se carga el mapa se realiza las siguientes instrucciones
        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                marcarUbicacionActual(mapboxMap);
                mapa = mapboxMap;
                ubicarUsuario();
                mapa.setMyLocationEnabled(true);
                Location localizaciondestino= new Location(ultimaLocalizacion);
                localizaciondestino.setLatitude(39.467494);
                localizaciondestino.setLongitude(-3.52829);
                obtenerRuta(localizaciondestino,ultimaLocalizacion);
                Intent i = getIntent();
                Bundle b = i.getExtras();
                // Si se recive una nueva nota creada desde el activity NuevaTarea se añadira un marcador en el centro del mapa
                // con los datos de la nueva tarea
                if (b != null) {
                    String titulo = (String) b.get("titulo");
                    String descripcion= (String) b.get("descripcion");
                    latitud=(Double) b.get("latitud");
                    longitud=(Double) b.get("longitud");

                    // ponemos el marcador con las coordenadas del centro del mapa en el momento que pulsamos el boton de
                    // nueva tarea
                    mapa.addMarker(new MarkerOptions()
                            .position(new LatLng(latitud,longitud))
                            .title(titulo)
                            .snippet(descripcion));


                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu mimenu) {
        getMenuInflater().inflate(R.menu.menu,mimenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();

        if(id==R.id.nuevatarea){
            // Almacenamos la longitud y latitud de la anotacion para luego guardarla en la base de datos y al iniciar
            // de nuevo la aplicacion se vea esa anotacion en estas coordenadas
            Double latitud=mapa.getCameraPosition().target.getLatitude();
            Double longitud= mapa.getCameraPosition().target.getLongitude();

            // y se lo pasamos al activity de la nueva tarea para luego poder poner un marcador en esas coordenadas
            Intent i = new Intent(this,NuevaTarea.class).putExtra("latitud",latitud).putExtra("longitud",longitud);
            startActivity(i);

        }
        if(id==R.id.vertareas){
            Intent intent = new Intent(this, ListaNotas.class);
            intent.putExtra("lista",0);
            startActivity(intent);
        }

        return true;
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
    private void ubicarUsuario() {
        btUbicacion = (FloatingActionButton) findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapa != null) {
                    Location lastLocation = ultimaLocalizacion;
                    if (lastLocation != null)
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

                    // Resalta la posición del usuario en el mapa
                    mapa.setMyLocationEnabled(true);
                }
            }
        });
    }


    public void marcarUbicacionActual(MapboxMap mapboxMap) {
        LatLng posicion = mostrarDatosLocalizacion(ultimaLocalizacion); // posicion actual
        LatLng posicion2 = new LatLng(39.467494, -3.52829); // posicion destino
        mapboxMap.addMarker(new MarkerOptions()
                .position(posicion2)
                .title("aaaa")
                .snippet("muajajaja"));

        CameraPosition position = new CameraPosition.Builder()
                .target(posicion) // Fija la posición
                .zoom(17) // Fija el nivel de zoom
                .tilt(30) // Fija la inclinación de la cámara
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }

    // Calcula la ruta entre el marker y la posición del usuario
    private void obtenerRuta(Location markerLocation, Location userLocation) {

        com.mapbox.services.commons.models.Position posicionMarker =
                com.mapbox.services.commons.models.Position.fromCoordinates
                        (markerLocation.getLongitude(), markerLocation.getLatitude());
        com.mapbox.services.commons.models.Position posicionUsuario =
                com.mapbox.services.commons.models.Position.fromCoordinates
                        (userLocation.getLongitude(), userLocation.getLatitude());

        // Obtiene la dirección entre los dos puntos
        MapboxDirections direccion = new MapboxDirections.Builder()
                .setOrigin(posicionMarker)
                .setDestination(posicionUsuario)
                .setProfile(DirectionsCriteria.PROFILE_CYCLING)
                .setAccessToken( Mapbox.getInstance(this, "pk.eyJ1IjoiYWxiZXJ0b2Nhbm9nYXJjaWEiLCJhIjoiY2pkcmFxNTk4MjAzODMzdDdlczEwb2RvbyJ9.ivhOGtFgvddeXkcuRqFWVw").getAccessToken())
                .build();

        direccion.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                DirectionsRoute ruta = response.body().getRoutes().get(0);
                Toast.makeText(ActivityMapa.this, "Distancia: " + ruta.getDistance() + " metros", Toast.LENGTH_SHORT).show();

                pintarRuta(ruta);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                // Qué hacer en caso de que falle el cálculo de la ruta
            }
        });
    }

    // Pinta la ruta sobre el mapa
    private void pintarRuta(DirectionsRoute ruta) {

        // Recoge los puntos de la ruta
        LineString lineString = LineString.fromPolyline(ruta.getGeometry(), Constants.PRECISION_6);
        List<com.mapbox.services.commons.models.Position> coordenadas = lineString.getCoordinates();
        LatLng[] puntos = new LatLng[coordenadas.size()];
        for (int i = 0; i < coordenadas.size(); i++) {
            puntos[i] = new LatLng(coordenadas.get(i).getLatitude(), coordenadas.get(i).getLongitude());
        }

        // Pinta los puntos en el mapa
        mapa.addPolyline(new PolylineOptions()
                .add(puntos)
                .color(Color.parseColor("#009688"))
                .width(5));

        // Resalta la posición del usuario si no lo estaba ya
        if (!mapa.isMyLocationEnabled())
            mapa.setMyLocationEnabled(true);
    }

}
