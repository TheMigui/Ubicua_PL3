package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreetSelection extends AppCompatActivity {
    Spinner spinner;
    Button btnContinuar;
    List<Street> listaCalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_selection);

        spinner = findViewById(R.id.spinner);
        btnContinuar = findViewById(R.id.button);

        // Obtener calles del servidor
        obtenerCallesDelServidor();
    }

    private void obtenerCallesDelServidor() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Street>> call = apiService.getItems();

        call.enqueue(new Callback<List<Street>>() {
            @Override
            public void onResponse(Call<List<Street>> call, Response<List<Street>> response) {
                if (response.isSuccessful()) {
                    listaCalles = response.body();
                    if (listaCalles != null && !listaCalles.isEmpty()) {
                        mostrarCallesEnSpinner(listaCalles);
                    } else {
                        Log.e("StreetSelection", "Lista de calles vacía");
                    }
                } else {
                    Log.e("StreetSelection", "Error del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Street>> call, Throwable t) {
                Log.e("StreetSelection", "Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarCallesEnSpinner(List<Street> lista) {
        // Crear lista de nombres para el spinner
        String[] nombresCalles = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            nombresCalles[i] = lista.get(i).getNombre();
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

                    Log.i("StreetSelection", "Calle seleccionada: " +
                            calleSeleccionada.getNombre() + " (ID: " +
                            calleSeleccionada.getId() + ")");

                    // Ir a la siguiente actividad
                    Intent intent = new Intent(StreetSelection.this, SensorSelection.class);
                    intent.putExtra("street_id", calleSeleccionada.getId());
                    intent.putExtra("street_name", calleSeleccionada.getNombre());
                    startActivity(intent);
                }
            }
        });
    }
}