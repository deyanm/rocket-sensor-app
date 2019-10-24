package com.example.rocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private Button back;
    private TextView altitude;
    private TextView velocity;
    private TextView time;
    private double holder1;
    private double holder2;
    private double holder3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceound);
        back = findViewById(R.id.btnBack);
        altitude = findViewById(R.id.altitude);
        velocity = findViewById(R.id.velocity);
        time = findViewById(R.id.time);

        Bundle bundle = getIntent().getExtras();

        holder1 = bundle.getDouble("altitude");
        holder2 = bundle.getDouble("velocity");
        holder3 = bundle.getDouble("t");

        altitude.setText(String.valueOf(Math.round(holder1)));
        velocity.setText(String.valueOf(Math.round(holder2)));
        time.setText(String.valueOf((holder3)));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //TODO MAKE PASS THE RESULTS
    }
}
