package com.example.apppecl3;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorDetailActivity extends AppCompatActivity {

    private static final String TAG = "SensorDetailActivity";
    private TextView tvInfoSensor;
    private RecyclerView recyclerView;
    private SensorDataAdapter adapter;
    private List<SensorData> listaDatosSensor = new ArrayList<>();
    private String sensorId;
    private String streetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        // Obtener datos del sensor
        sensorId = getIntent().getStringExtra("sensor_id");
        streetName = getIntent().getStringExtra("street_name");
        String streetId = getIntent().getStringExtra("street_id");

        Log.i(TAG, "Sensor: " + sensorId + ", Calle: " + streetName);

        // Inicializar componentes
        tvInfoSensor = findViewById(R.id.tvInfoSensor);
        recyclerView = findViewById(R.id.recyclerViewSensorData);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SensorDataAdapter(listaDatosSensor);
        recyclerView.setAdapter(adapter);

        // Mostrar información básica
        tvInfoSensor.setText("Sensor: " + sensorId + "\nCalle: " + streetName);

        // Obtener datos del sensor desde el servidor
        obtenerDatosSensor(sensorId);
    }

    private void obtenerDatosSensor(String sensorId) {
        Log.i(TAG, "Obteniendo datos para sensor: " + sensorId);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<SensorData>> call = apiService.getSensorData(sensorId);

        call.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (response.isSuccessful()) {
                    List<SensorData> datos = response.body();

                    if (datos != null && !datos.isEmpty()) {
                        Log.i(TAG, "Datos recibidos: " + datos.size() + " registros");

                        // Actualizar lista y adapter
                        listaDatosSensor.clear();
                        listaDatosSensor.addAll(datos);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(SensorDetailActivity.this,
                                datos.size() + " registros cargados",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Log.w(TAG, "No hay datos para este sensor");
                        Toast.makeText(SensorDetailActivity.this,
                                "No hay datos disponibles",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "Error del servidor: " + response.code());
                    Toast.makeText(SensorDetailActivity.this,
                            "Error del servidor",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(SensorDetailActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}