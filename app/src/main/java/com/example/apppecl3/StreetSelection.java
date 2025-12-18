package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreetSelection extends AppCompatActivity {
    private static final String TAG = "StreetSelection";
    private Spinner spinner;
    private Button btnContinuar;
    private List<String> listaStreetIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_selection);

        Log.i(TAG, "Activity iniciada");

        spinner = findViewById(R.id.spinner);
        btnContinuar = findViewById(R.id.button);
        btnContinuar.setEnabled(false);

        obtenerIdsCallesDelServidor();
    }

    private void obtenerIdsCallesDelServidor() {
        Log.i(TAG, "Conectando al servidor para obtener calles");

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<String>> call = apiService.getStreetIds();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, "Respuesta recibida. Código: " + response.code());

                if (response.isSuccessful()) {
                    listaStreetIds = response.body();

                    if (listaStreetIds != null) {
                        Log.i(TAG, "IDs recibidos: " + listaStreetIds.size());

                        if (!listaStreetIds.isEmpty()) {
                            mostrarCallesEnSpinner();
                        } else {
                            Log.w(TAG, "Lista vacía");
                            mostrarError("No hay calles disponibles");
                        }
                    } else {
                        Log.e(TAG, "Lista de IDs es null");
                        mostrarError("Error al procesar datos");
                    }
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    mostrarError("Error del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarCallesEnSpinner() {
        Log.i(TAG, "Mostrando " + listaStreetIds.size() + " calles");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaStreetIds);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinner.getSelectedItemPosition();
                if (selectedPosition >= 0 && selectedPosition < listaStreetIds.size()) {
                    String streetIdSeleccionada = listaStreetIds.get(selectedPosition);

                    Intent intent = new Intent(StreetSelection.this, SensorSelection.class);
                    intent.putExtra("street_id", streetIdSeleccionada);
                    intent.putExtra("street_name", streetIdSeleccionada);
                    startActivity(intent);

                } else {
                    Toast.makeText(StreetSelection.this,
                            "Error: Selecciona una calle válida",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnContinuar.setEnabled(true);

        if (listaStreetIds.size() > 0) {
            spinner.setSelection(0);
        }

        Toast.makeText(this,
                listaStreetIds.size() + " calles cargadas",
                Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String mensaje) {
        Log.e(TAG, "Error: " + mensaje);

        String[] mensajeError = {mensaje};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mensajeError);
        spinner.setAdapter(adapter);
        spinner.setEnabled(false);
        btnContinuar.setEnabled(false);

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}