package com.example.apppecl3;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        // Obtener datos del sensor
        String sensorId = getIntent().getStringExtra("sensor_id");
        String sensorTipo = getIntent().getStringExtra("sensor_tipo");
        String streetName = getIntent().getStringExtra("street_name");

        // Mostrar información
        TextView tvDetalle = findViewById(R.id.tvDetalle);
        String detalle = "Calle: " + streetName + "\n\n" +
                "Sensor ID: " + sensorId + "\n" +
                "Tipo: " + sensorTipo + "\n\n" +
                "(Esta actividad se implementará después)";

        tvDetalle.setText(detalle);
    }
}
