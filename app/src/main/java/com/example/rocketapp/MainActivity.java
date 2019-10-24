package com.example.rocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView showRocketName;
    private Button btnLeft;
    private Button btnRight;
    private Button calculate;
    private EditText inputMass;
    private EditText inputArea;
    private EditText inputImpulse;
    private EditText inputThrust;
    private String[] displayRocket = new String[4];
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idAssign();
        showRocketName.setText(displayRocket[0]);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a--;
                if (a < 0) {
                    a = 3;
                    showRocketName.setText(displayRocket[a]);
                }
                if (a >= 0) {
                    showRocketName.setText(displayRocket[a]);
                }
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a++;
                if (a > 3) {
                    a = 0;
                    showRocketName.setText(displayRocket[a]);
                } else {
                    showRocketName.setText(displayRocket[a]);
                }
            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
    }

    protected void idAssign() {
        displayRocket[0] = "Custom";
        displayRocket[1] = "Test 1";
        displayRocket[2] = "Test 2";
        displayRocket[3] = "Test 3";
        showRocketName = findViewById(R.id.rocketName);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        inputMass = findViewById(R.id.inputMass);
        inputArea = findViewById(R.id.inputArea);
        inputImpulse = findViewById(R.id.inputImpulse);
        inputThrust = findViewById(R.id.inputThrust);
        calculate = findViewById(R.id.btnCalculate);
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
            double sumTime = t;
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("altitude", altitude);
            intent.putExtra("velocity", v);
            intent.putExtra("time", sumTime);
            Bundle bundle = new Bundle();
            bundle.putDouble("altitude", altitude);
            bundle.putDouble("velocity", v);
            bundle.putDouble("t", sumTime);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


}
