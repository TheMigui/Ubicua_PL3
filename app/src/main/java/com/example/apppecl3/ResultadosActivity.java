package com.example.apppecl3;

import android.os.AsyncTask;
import android.os.Bundle;
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
            new ObtenerDatosTask().execute(url);
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

    private class ObtenerDatosTask extends AsyncTask<String, Void, List<DatoSensor>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<DatoSensor> doInBackground(String... urls) {
            List<DatoSensor> resultado = new ArrayList<>();
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000); // 10 segundos
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parsear JSON
                    JSONArray jsonArray = new JSONArray(response.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String sensorId = jsonObject.getString("sensor_id");
                        String currentState = jsonObject.getString("current_state");
                        String time = jsonObject.getString("time");

                        resultado.add(new DatoSensor(sensorId, currentState, time));
                    }
                } else {
                    // Error en la respuesta del servidor
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(List<DatoSensor> datos) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (datos != null && !datos.isEmpty()) {
                listaSensores.clear();
                listaSensores.addAll(datos);
                adapter.actualizarLista(listaSensores);

                // Mostrar mensaje con cantidad de registros
                Toast.makeText(ResultadosActivity.this,
                        "Encontrados: " + datos.size() + " sensores",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResultadosActivity.this,
                        "No se encontraron datos para esta fecha",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
