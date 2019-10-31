package com.example.rocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.material.button.MaterialButton;

public class SecondActivity extends AppCompatActivity {
    private Button back;
    private TextView altitude;
    private TextView velocity;
    private TextView time;
    private TextView speedv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        back = findViewById(R.id.btnBack);
        altitude = findViewById(R.id.altitude);
        velocity = findViewById(R.id.velocity);
        time = findViewById(R.id.time);
        speedv = findViewById(R.id.speed);
        double alt = 0, vel = 0, t = 0,speed = 0;

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            alt = bundle.getDouble("altitude");
            vel = bundle.getDouble("velocity");
            t = bundle.getDouble("time");
            speed = bundle.getDouble("speed");

        }
        String roundDouble = String.format("%.2f",t);

        altitude.setText(String.valueOf(Math.round(alt))+" Meters");
        velocity.setText(String.valueOf(Math.round(vel))+" M/s on vector");
        time.setText(String.valueOf((roundDouble))+" Seconds");
        speedv.setText(String.valueOf(Math.round(speed)+" M/s"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });
    }
}
