package com.example.rocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rocketapp.database.AppDatabase;
import com.example.rocketapp.database.AppExecutors;
import com.example.rocketapp.database.Rocket;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MaterialButton calculate, saveRocket, deleteRocket;
    private EditText inputName, inputMass, inputArea, inputThrust, inputImpulse;
    private AppDatabase mDb;
    private Spinner mSpinner;
    private List<Rocket> rockets;
    private List<String> spinnerList;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mSpinner = findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Rocket rocket;
                        if (rockets != null && rockets.get(position) != null) {
                            rocket = rockets.get(position);
                        } else {
                            rocket = mDb.personDao().findByName(parent.getItemAtPosition(position).toString());
                        }
                        if (rocket != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    deleteRocket.setVisibility(View.VISIBLE);
                                    inputName.setText(rocket.getName());
                                    inputArea.setText(rocket.getArea());
                                    inputImpulse.setText(rocket.getImpulse());
                                    inputThrust.setText(rocket.getThrust());
                                    inputMass.setText(rocket.getMass());
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rockets = new ArrayList<>();
        spinnerList = new ArrayList<>();
        dataAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
        retrieveRockets();
        inputName = findViewById(R.id.name_edit_text);
        inputMass = findViewById(R.id.inputMass);
        inputArea = findViewById(R.id.inputArea);
        inputImpulse = findViewById(R.id.inputImpulse);
        inputThrust = findViewById(R.id.inputThrust);
        saveRocket = findViewById(R.id.save);
        saveRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (inputMass.getText().toString().equals("") || inputName.getText().toString().equals("") || inputThrust.getText().toString().equals("") || inputImpulse.getText().toString().equals("") || inputArea.getText().toString().equals("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Please fill all the blanks", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        final Rocket rocket = new Rocket();
                        rocket.setName(inputName.getText().toString());
                        rocket.setArea(inputArea.getText().toString());
                        rocket.setImpulse(inputImpulse.getText().toString());
                        rocket.setMass(inputMass.getText().toString());
                        rocket.setThrust(inputThrust.getText().toString());
                        mDb.personDao().insertRocket(rocket);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Successfully added rocket", Toast.LENGTH_SHORT).show();
                                rockets.add(rocket);
                                spinnerList.add(rocket.getName());
                                dataAdapter.notifyDataSetChanged();
                                mSpinner.setSelection(spinnerList.size() - 1);
                            }
                        });
                    }
                });
            }
        });
        deleteRocket = findViewById(R.id.delete);
        deleteRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final Rocket rocket = rockets.get(mSpinner.getSelectedItemPosition());
                        mDb.personDao().deleteRocket(rocket);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Successfully deleted rocket", Toast.LENGTH_SHORT).show();
                                rockets.remove(rocket);
                                spinnerList.remove(rocket.getName());
                                dataAdapter.notifyDataSetChanged();
                                if (dataAdapter.getCount() > 0) {
                                    mSpinner.setSelection(spinnerList.size() - 1);
                                } else {
                                    deleteRocket.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }
        });
        calculate = findViewById(R.id.button);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
    }

    private void calculate() {
        if (inputMass.getText().toString().equals("") || inputName.getText().toString().equals("") || inputThrust.getText().toString().equals("") || inputImpulse.getText().toString().equals("") || inputArea.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill all the blanks", Toast.LENGTH_SHORT).show();
            return;
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
            double ta = Math.atan(v / qa) / qb;
            double allTime = t + ta;
            double speed = altitude / allTime;
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("altitude", altitude);
            intent.putExtra("velocity", v);
            intent.putExtra("speed", speed);
            intent.putExtra("time", allTime);
            startActivity(intent);
        }
    }

    private void retrieveRockets() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                rockets = mDb.personDao().getAllRockets();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Rocket rocket : rockets) {
                            spinnerList.add(rocket.getName());
                        }

                        dataAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

}
