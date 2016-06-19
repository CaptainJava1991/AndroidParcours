package com.androidparcours.example.carthy.androidparcours;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main2Activity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    IntentFilter mIntentFilter =new IntentFilter("SERVICE_LOCALISATION");
    private MarkerOptions marker = new MarkerOptions();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        //private MarkerOptions marker = new MarkerOptions();

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("SERVICE_LOCALISATION")){
                Bundle bundle = intent.getExtras();
                double latitude = bundle.getDouble("latitude");
                double longitude =  bundle.getDouble("longitude");
                LatLng newLoc = new LatLng(latitude,longitude);
                marker.position(newLoc);

                map.addMarker(new MarkerOptions().position(newLoc));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        registerReceiver(mReceiver,mIntentFilter);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.oval));
    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.map = map;
        startService(new Intent(this, ServiceLocalisation.class));Log.i("onMapReady","OK");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ServiceLocalisation.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }
}
