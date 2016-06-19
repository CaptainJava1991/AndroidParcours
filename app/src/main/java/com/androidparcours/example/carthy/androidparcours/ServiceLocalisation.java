package com.androidparcours.example.carthy.androidparcours;

/**
 * Created by Carthy on 18/06/2016.
 */
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class ServiceLocalisation extends Service implements LocationListener {
    double latitude;
    double longitude;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreateService","OK");
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            abonnementGPS();
            Log.i("locationManager","OK");
        }else{
            Log.i("locationManagerPB","OK");
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(locationGPS != null){
                onLocationChanged(locationGPS);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        desabonnementGPS();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("onLocationChanged","OK");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Intent intent = new Intent();
        intent.setAction("SERVICE_LOCALISATION");
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        sendBroadcast(intent);
    }

    /**
     * Méthode permettant de s'abonner à la localisation par GPS.
     */
    public void abonnementGPS() {
        //On s'abonne
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.i("permissonPB","OK");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }

    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_FINE_LOCATION )
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission
                        ( this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.i("permissonPB","OK");
            return  ;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if("gps".equals(provider)) {
            desabonnementGPS();
        }
    }

    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if("gps".equals(provider)) {
            abonnementGPS();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) { }

}
