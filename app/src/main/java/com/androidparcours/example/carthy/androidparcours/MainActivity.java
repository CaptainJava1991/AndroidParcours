package com.androidparcours.example.carthy.androidparcours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private double[][] tabLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newRun(View view){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
