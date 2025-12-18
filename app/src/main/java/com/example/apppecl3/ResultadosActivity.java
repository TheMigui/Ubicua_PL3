package com.example.apppecl3;

import android.os.AsyncTask;
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultadosActivity extends AppCompatActivity {

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

        // Inicializar componentes
        tvFecha = findViewById(R.id.tvFecha);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnVolver = findViewById(R.id.btnVolver);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SensorAdapter(listaSensores);
        recyclerView.setAdapter(adapter);

        // Obtener la fecha del Intent
        String fechaFormateada = getIntent().getStringExtra("fecha_formateada");

        if (fechaFormateada != null && !fechaFormateada.isEmpty()) {
            // Mostrar la fecha en el título
            tvFecha.setText("Datos del: " + fechaFormateada);

            // Construir URL con la fecha
            String url = "http://localhost:8080/GetDataByDate?date=" + fechaFormateada;

            // IMPORTANTE: Para Android, localhost es 10.0.2.2 (Emulador) o IP de tu máquina
            // Si estás en emulador, cambia la URL a:
            // String url = "http://10.0.2.2:8080/GetDataByDate?date=" + fechaFormateada;

            // Llamar al servidor
            obtenerDatos();
        } else {
            Toast.makeText(this, "Error: No se recibió la fecha", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresa a la actividad anterior
            }
        });
    }

    void obtenerDatos(){
        Bundle extras = getIntent().getExtras();
        String fecha = "2024-06-01";
        if (extras != null){
            fecha = extras.getString("fecha_formateada");
        }
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<DatoSensor>> call = apiService.getItemsByDate(fecha);
        call.enqueue(new Callback<List<DatoSensor>>() {
            @Override
            public void onResponse(Call<List<DatoSensor>> call, Response<List<DatoSensor>> response) {
                if (response.isSuccessful()) {
                    listaSensores = response.body();
                } else {
                    Log.e("ubicua","Error del servidor");
                }
            }

            @Override
            public void onFailure(Call<List<DatoSensor>> call, Throwable t) {
                Log.e("ubicua","Error: " + t.getMessage());
            }
        });
    }
}
