package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {

    private Button btnSeleccionarCalle, btnSeleccionarFecha, btnMostrarDatosPL1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar componentes
        btnSeleccionarCalle = findViewById(R.id.btnSeleccionarCalle);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnMostrarDatosPL1 = findViewById(R.id.btnMostrarDatosPL1);

        handler = new Handler();

        // Configurar listeners
        configurarListeners();
    }

    private void configurarListeners() {
        // Botón: Seleccionar Calle (espera 3 segundos)
        btnSeleccionarCalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deshabilitar botón durante la espera
                btnSeleccionarCalle.setEnabled(false);
                btnSeleccionarCalle.setText("Esperando 3 segundos...");

                // Mostrar mensaje de espera
                Toast.makeText(MainMenuActivity.this,
                        "Esperando 3 segundos...", Toast.LENGTH_SHORT).show();

                // Handler para la espera de 3 segundos
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Restaurar botón
                        btnSeleccionarCalle.setEnabled(true);
                        btnSeleccionarCalle.setText("Seleccionar Calle");

                        // Ir a StreetSelection
                        Intent intent = new Intent(MainMenuActivity.this, StreetSelection.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000); // 3000 milisegundos = 3 segundos
            }
        });

        // Botón: Seleccionar por Fecha
        btnSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, DateSelection.class);
                startActivity(intent);
                finish();
            }
        });

        // Botón: Mostrar datos PL1 (no hace nada)
        btnMostrarDatosPL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // No hace nada, solo muestra un mensaje opcional
//                Toast.makeText(MainMenuActivity.this,
//                        "Función no implementada", Toast.LENGTH_SHORT).show();
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<SuccessResponse> call = apiService.triggerTL();

                call.enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                        Log.d("TriggerTL", "Respuesta recibida. Código: " + response.code());

                        if (response.isSuccessful()) {
                            mostrarToast("Semáforo activado");
                        } else {
                            mostrarToast("Fallo en la activación");
                            Log.e("TriggerTL", "Error HTTP: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {
                        Log.e("TriggerTL", "Error de conexión: " + t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar handlers para evitar memory leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void mostrarToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}