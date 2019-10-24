package com.example.rocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private MaterialButton calculate;
    private TextInputEditText inputName;
    private EditText inputMass, inputArea, inputThrust, inputImpulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idAssign();
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
    }

    protected void idAssign() {
        inputName = findViewById(R.id.name_edit_text);
        inputMass = findViewById(R.id.inputMass);
        inputArea = findViewById(R.id.inputArea);
        inputImpulse = findViewById(R.id.inputImpulse);
        inputThrust = findViewById(R.id.inputThrust);
        calculate = findViewById(R.id.button);
    }

    private void calculate() {
        if (inputMass.getText().toString().equals("") || inputThrust.getText().toString().equals("") || inputImpulse.getText().toString().equals("") || inputArea.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill all the blanks", Toast.LENGTH_SHORT).show();
        } else {
            double mass = Double.parseDouble(inputMass.getText().toString());
            double area = Double.parseDouble(inputArea.getText().toString());
            double k = 0.5 * 1.2 * 0.75 * area;
            double impulse = Double.parseDouble(inputImpulse.getText().toString());
            double thrust = Double.parseDouble(inputThrust.getText().toString());
            double t = impulse / thrust;
            double q = Math.sqrt((thrust - (mass * 9.8)) / k);
            double x = 2 * k * (q / mass);
            double v = q * (1 - Math.exp(-x * t)) / (1 + Math.exp(-x * t));
            double yb = (-mass / (2 * k)) * Math.log((thrust - mass * 9.8 - k * v * v) / (thrust - mass * 9.8));
            double yc = (mass / (2 * k)) * Math.log((mass * 9.8 + k * v * v) / (mass * 9.8));
            double altitude = (yb + yc);
            double qa = Math.sqrt(mass * 9.8 / k);
            double qb = Math.sqrt(9.8 * k / mass);
            //double ta = Math.atan(v/qa)/qb;
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("altitude", altitude);
            intent.putExtra("velocity", v);
            intent.putExtra("time", t);
            startActivity(intent);
        }
    }


}
