package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorSelection extends AppCompatActivity {

    private static final String TAG = "SensorSelection";
    private Spinner spinnerSensores;
    private Button btnSeleccionar;
    private TextView tvCalleInfo;
    private List<Sensor> listaSensores;
    private String streetId;
    private String streetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);

        // Obtener datos de la calle
        Intent intent = getIntent();
        streetId = intent.getStringExtra("street_id");
        streetName = intent.getStringExtra("street_name");

        // Inicializar componentes
        spinnerSensores = findViewById(R.id.spinnerSensores);
        btnSeleccionar = findViewById(R.id.btnSeleccionar);
        tvCalleInfo = findViewById(R.id.tvCalleInfo);

        // Mostrar información de la calle
        tvCalleInfo.setText("Calle: " + streetName);

        // Obtener sensores del servidor
        obtenerSensoresDelServidor(streetId);
    }

    private void obtenerSensoresDelServidor(String streetId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Sensor>> call = apiService.getSensorsByStreet(streetId);

        call.enqueue(new Callback<List<Sensor>>() {
            @Override
            public void onResponse(Call<List<Sensor>> call, Response<List<Sensor>> response) {
                if (response.isSuccessful()) {
                    listaSensores = response.body();
                    if (listaSensores != null && !listaSensores.isEmpty()) {
                        mostrarSensoresEnSpinner(listaSensores);
                    } else {
                        Log.e(TAG, "No hay sensores para esta calle");
                        mostrarMensajeSinSensores();
                    }
                } else {
                    Log.e(TAG, "Error del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Sensor>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarSensoresEnSpinner(List<Sensor> sensores) {
        // Crear lista para el spinner
        String[] nombresSensores = new String[sensores.size()];
        for (int i = 0; i < sensores.size(); i++) {
            nombresSensores[i] = "Sensor " + sensores.get(i).getId() +
                    " - " + sensores.get(i).getTipo();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresSensores);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSensores.setAdapter(adapter);

        // Configurar botón seleccionar
        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinnerSensores.getSelectedItemPosition();
                if (selectedPosition >= 0 && selectedPosition < listaSensores.size()) {
                    Sensor sensorSeleccionado = listaSensores.get(selectedPosition);

                    Log.i(TAG, "Sensor seleccionado: " +
                            sensorSeleccionado.getId() + " - " +
                            sensorSeleccionado.getTipo());

                    // Ir a la siguiente actividad (por ahora solo muestra mensaje)
                    Intent intent = new Intent(SensorSelection.this, SensorDetailActivity.class);
                    intent.putExtra("sensor_id", sensorSeleccionado.getId());
                    intent.putExtra("sensor_tipo", sensorSeleccionado.getTipo());
                    intent.putExtra("street_id", streetId);
                    intent.putExtra("street_name", streetName);
                    startActivity(intent);
                }
            }
        });
    }

    private void mostrarMensajeSinSensores() {
        String[] mensaje = {"No hay sensores disponibles"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mensaje);
        spinnerSensores.setAdapter(adapter);
        spinnerSensores.setEnabled(false);
        btnSeleccionar.setEnabled(false);
    }
}
