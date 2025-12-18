package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
    private List<String> listaIdsSensores;  // CAMBIADO: Ahora es List<String>
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

        Log.i(TAG, "Calle recibida: " + streetName + " (ID: " + streetId + ")");

        // Inicializar componentes
        spinnerSensores = findViewById(R.id.spinnerSensores);
        btnSeleccionar = findViewById(R.id.btnSeleccionar);
        tvCalleInfo = findViewById(R.id.tvCalleInfo);

        // Mostrar información de la calle
        tvCalleInfo.setText("Calle: " + streetName + "\nID: " + streetId);

        // Obtener sensores del servidor
        obtenerSensoresDelServidor(streetId);
    }

    private void obtenerSensoresDelServidor(String streetId) {
        Log.i(TAG, "Obteniendo sensores para calle ID: " + streetId);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<String>> call = apiService.getSensorsByStreet(streetId);

        call.enqueue(new Callback<List<String>>() {  // CAMBIADO: Callback<List<String>>
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, "Respuesta recibida. Código: " + response.code());

                if (response.isSuccessful()) {
                    listaIdsSensores = response.body();

                    if (listaIdsSensores != null) {
                        Log.i(TAG, "Sensores recibidos: " + listaIdsSensores.size());

                        // Mostrar en log todos los sensores recibidos
                        for (String sensorId : listaIdsSensores) {
                            Log.d(TAG, "Sensor: " + sensorId);
                        }

                        if (!listaIdsSensores.isEmpty()) {
                            mostrarSensoresEnSpinner();
                        } else {
                            Log.w(TAG, "No hay sensores para esta calle");
                            mostrarMensajeSinSensores();
                        }
                    } else {
                        Log.e(TAG, "Lista de sensores es null");
                        mostrarMensajeSinSensores();
                    }
                } else {
                    Log.e(TAG, "Error del servidor: " + response.code());

                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Sin detalles";
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mostrarMensajeError();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
                mostrarMensajeError();
            }
        });
    }

    private void mostrarSensoresEnSpinner() {
        Log.i(TAG, "Mostrando " + listaIdsSensores.size() + " sensores en spinner");

        // Crear adapter con los IDs de sensores (que son strings)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaIdsSensores);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSensores.setAdapter(adapter);

        // Configurar botón seleccionar
        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinnerSensores.getSelectedItemPosition();
                if (selectedPosition >= 0 && selectedPosition < listaIdsSensores.size()) {
                    String sensorSeleccionado = listaIdsSensores.get(selectedPosition);

                    Log.i(TAG, "=== SENSOR SELECCIONADO ===");
                    Log.i(TAG, "Sensor ID: " + sensorSeleccionado);
                    Log.i(TAG, "Calle: " + streetName + " (ID: " + streetId + ")");

                    // Mostrar mensaje de confirmación
                    Toast.makeText(SensorSelection.this,
                            "Seleccionado: " + sensorSeleccionado,
                            Toast.LENGTH_SHORT).show();

                    // Ir a la siguiente actividad
                    Intent intent = new Intent(SensorSelection.this, SensorDetailActivity.class);
                    intent.putExtra("sensor_id", sensorSeleccionado);
                    intent.putExtra("street_id", streetId);
                    intent.putExtra("street_name", streetName);
                    startActivity(intent);

                } else {
                    Log.e(TAG, "Posición seleccionada inválida: " + selectedPosition);
                    Toast.makeText(SensorSelection.this,
                            "Error: Selecciona un sensor válido",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Habilitar botón
        btnSeleccionar.setEnabled(true);

        // Seleccionar primer elemento
        if (listaIdsSensores.size() > 0) {
            spinnerSensores.setSelection(0);
            Log.d(TAG, "Sensor seleccionado por defecto: " + listaIdsSensores.get(0));
        }
    }

    private void mostrarMensajeSinSensores() {
        String[] mensaje = {"⚠️ No hay sensores disponibles"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mensaje);
        spinnerSensores.setAdapter(adapter);
        spinnerSensores.setEnabled(false);
        btnSeleccionar.setEnabled(false);

        Toast.makeText(this, "Esta calle no tiene sensores", Toast.LENGTH_LONG).show();
    }

    private void mostrarMensajeError() {
        String[] mensaje = {"❌ Error al cargar sensores"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mensaje);
        spinnerSensores.setAdapter(adapter);
        spinnerSensores.setEnabled(false);
        btnSeleccionar.setEnabled(false);

        Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
    }
}