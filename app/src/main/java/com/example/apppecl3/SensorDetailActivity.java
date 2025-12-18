package com.example.apppecl3;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        // Obtener datos
        String sensorId = getIntent().getStringExtra("sensor_id");
        String streetName = getIntent().getStringExtra("street_name");
        String streetId = getIntent().getStringExtra("street_id");

        // Mostrar información
        TextView tvDetalle = findViewById(R.id.tvDetalle);
        String detalle = "✅ FLUJO COMPLETADO\n\n" +
                "📌 Calle: " + streetName + "\n" +
                "   ID: " + streetId + "\n\n" +
                "🎯 Sensor seleccionado:\n" +
                "   " + sensorId + "\n\n" +
                "🔧 Esta pantalla se implementará\n" +
                "   para mostrar datos del sensor";

        tvDetalle.setText(detalle);

        // Mostrar Toast
        Toast.makeText(this,
                "Sensor " + sensorId + " seleccionado",
                Toast.LENGTH_LONG).show();
    }
}