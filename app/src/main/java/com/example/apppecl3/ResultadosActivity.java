package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class ResultadosActivity extends AppCompatActivity {

    private static final String TAG = "ResultadosActivity";
    private TextView tvFecha;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btnVolver;
    private SensorAdapter adapter;
    private List<DatoSensor> listaSensores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        Log.d(TAG, "onCreate iniciado");

        // Inicializar componentes
        tvFecha = findViewById(R.id.tvFecha);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnVolver = findViewById(R.id.btnVolver);

        // Mostrar progress bar inicialmente
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SensorAdapter(listaSensores);
        recyclerView.setAdapter(adapter);

        // Obtener la fecha del Intent
        String fechaFormateada = getIntent().getStringExtra("fecha_formateada");

        if (fechaFormateada != null && !fechaFormateada.isEmpty()) {
            // Mostrar la fecha en el título
            tvFecha.setText("Datos del: " + fechaFormateada);
            Log.d(TAG, "Fecha recibida: " + fechaFormateada);

            // Llamar al servidor
            obtenerDatos(fechaFormateada);
        } else {
            Toast.makeText(this, "Error: No se recibió la fecha", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultadosActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();            }
        });
    }

    void obtenerDatos(String fecha){
        Log.d(TAG, "obtenerDatos llamada con fecha: " + fecha);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<DatoSensor>> call = apiService.getItemsByDate(fecha);

        call.enqueue(new Callback<List<DatoSensor>>() {
            @Override
            public void onResponse(Call<List<DatoSensor>> call, Response<List<DatoSensor>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    List<DatoSensor> nuevosDatos = response.body();
                    Log.d(TAG, "Respuesta exitosa. Datos: " +
                            (nuevosDatos != null ? nuevosDatos.size() : 0));

                    if (nuevosDatos != null && !nuevosDatos.isEmpty()) {
                        // 1. Limpiar lista
                        listaSensores.clear();

                        // 2. Añadir nuevos datos
                        listaSensores.addAll(nuevosDatos);

                        // 3. ACTUALIZAR ADAPTER - IMPORTANTE
                        adapter.actualizarLista(listaSensores);

                        // 4. Mostrar mensaje
                        Toast.makeText(ResultadosActivity.this,
                                "✓ " + nuevosDatos.size() + " sensores encontrados",
                                Toast.LENGTH_SHORT).show();

                        // 5. Loggear para debug
                        for (DatoSensor sensor : nuevosDatos) {
                            Log.d(TAG, "Sensor: " + sensor.getSensorId());
                        }
                    } else {
                        Toast.makeText(ResultadosActivity.this,
                                "No hay datos para esta fecha",
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Lista vacía");
                    }
                } else {
                    Log.e(TAG, "Error servidor: " + response.code());
                    Toast.makeText(ResultadosActivity.this,
                            "Error del servidor",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DatoSensor>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                Log.e(TAG, "Error conexión: " + t.getMessage());
                Toast.makeText(ResultadosActivity.this,
                        "Error de conexión al servidor",
                        Toast.LENGTH_LONG).show();
                // SIN DATOS DE EJEMPLO
            }
        });
    }
}