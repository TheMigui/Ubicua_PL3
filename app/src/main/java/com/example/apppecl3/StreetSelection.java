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
    private List<Street> listaCalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_selection);

        Log.i(TAG, "Activity StreetSelection iniciada");

        spinner = findViewById(R.id.spinner);
        btnContinuar = findViewById(R.id.button);

        // Deshabilitar botón hasta que carguen datos
        btnContinuar.setEnabled(false);

        // Obtener calles del servidor
        obtenerCallesDelServidor();
    }

    private void obtenerCallesDelServidor() {
        Log.i(TAG, "Conectando al servidor: http://10.0.2.2:8080/Ubicua/GetStreets");

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Street>> call = apiService.getItems();

        call.enqueue(new Callback<List<Street>>() {
            @Override
            public void onResponse(Call<List<Street>> call, Response<List<Street>> response) {
                Log.d(TAG, "Respuesta del servidor recibida");
                Log.d(TAG, "Código HTTP: " + response.code());
                Log.d(TAG, "¿Éxito?: " + response.isSuccessful());

                if (response.isSuccessful()) {
                    listaCalles = response.body();

                    if (listaCalles != null) {
                        Log.i(TAG, "Calles recibidas: " + listaCalles.size());

                        // Mostrar todas las calles en log
                        for (Street calle : listaCalles) {
                            Log.d(TAG, "📌 Calle: " + calle.getNombre() +
                                    " (ID: " + calle.getId() + ")");
                        }

                        if (!listaCalles.isEmpty()) {
                            mostrarCallesEnSpinner();
                        } else {
                            Log.w(TAG, "El servidor devolvió lista vacía");
                            mostrarError("No hay calles disponibles en el servidor");
                        }
                    } else {
                        Log.e(TAG, "Error: lista de calles es null");
                        mostrarError("Error al procesar datos del servidor");
                    }
                } else {
                    Log.e(TAG, "Error HTTP del servidor: " + response.code());

                    // Intentar leer el error
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Sin detalles";
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error leyendo error body", e);
                    }

                    mostrarError("Error del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Street>> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
                mostrarError("No se pudo conectar al servidor: " + t.getMessage());
            }
        });
    }

    private void mostrarCallesEnSpinner() {
        Log.i(TAG, "Mostrando " + listaCalles.size() + " calles en spinner");

        // Crear lista de nombres para el spinner
        String[] nombresCalles = new String[listaCalles.size()];
        for (int i = 0; i < listaCalles.size(); i++) {
            nombresCalles[i] = listaCalles.get(i).getNombre();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresCalles);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Configurar botón continuar
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinner.getSelectedItemPosition();
                if (selectedPosition >= 0 && selectedPosition < listaCalles.size()) {
                    Street calleSeleccionada = listaCalles.get(selectedPosition);

                    Log.i(TAG, "=== CALLE SELECCIONADA ===");
                    Log.i(TAG, "Nombre: " + calleSeleccionada.getNombre());
                    Log.i(TAG, "ID: " + calleSeleccionada.getId());

                    // Mostrar mensaje de confirmación
                    Toast.makeText(StreetSelection.this,
                            "Calle seleccionada: " + calleSeleccionada.getNombre(),
                            Toast.LENGTH_SHORT).show();

                    // Ir a la siguiente actividad
                    Intent intent = new Intent(StreetSelection.this, SensorSelection.class);
                    intent.putExtra("street_id", calleSeleccionada.getId());
                    intent.putExtra("street_name", calleSeleccionada.getNombre());
                    startActivity(intent);

                } else {
                    Log.e(TAG, "Posición seleccionada inválida: " + selectedPosition);
                    Toast.makeText(StreetSelection.this,
                            "Error: Selecciona una calle válida",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Habilitar botón
        btnContinuar.setEnabled(true);

        // Seleccionar primer elemento por defecto
        if (listaCalles.size() > 0) {
            spinner.setSelection(0);
            Log.d(TAG, "Calle seleccionada por defecto: " + listaCalles.get(0).getNombre());
        }

        Toast.makeText(this,
                "✅ " + listaCalles.size() + " calles cargadas",
                Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String mensaje) {
        Log.e(TAG, "Mostrando error: " + mensaje);

        // Mostrar mensaje en spinner
        String[] mensajeError = {mensaje};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                mensajeError);
        spinner.setAdapter(adapter);
        spinner.setEnabled(false);

        // Deshabilitar botón
        btnContinuar.setEnabled(false);

        // Mostrar Toast
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}