package com.example.apppecl3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DateSelection extends AppCompatActivity {
    
    private EditText etAnio, etMes, etDia;
    private Button btnBuscar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);
        
        // Inicializar componentes
        etAnio = findViewById(R.id.etAnio);
        etMes = findViewById(R.id.etMes);
        etDia = findViewById(R.id.etDia);
        btnBuscar = findViewById(R.id.btnBuscar);
        
        // Configurar listener del botón
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarDatos();
            }
        });
    }
    
    private void buscarDatos() {
        String anio = etAnio.getText().toString().trim();
        String mes = etMes.getText().toString().trim();
        String dia = etDia.getText().toString().trim();
        
        // Validar que todos los campos tengan datos
        if (anio.isEmpty() || mes.isEmpty() || dia.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validar formato numérico
        if (!esNumeroValido(anio, 1900, 2100) || 
            !esNumeroValido(mes, 1, 12) || 
            !esNumeroValido(dia, 1, 31)) {
            Toast.makeText(this, "Por favor, ingrese fechas válidas", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Formatear la fecha (ej: 2024-03-15)
        String fechaFormateada = String.format("%s-%02d-%02d", 
            anio, 
            Integer.parseInt(mes), 
            Integer.parseInt(dia));
        
        // Mostrar mensaje de éxito
        Toast.makeText(this, "Buscando datos para: " + fechaFormateada, Toast.LENGTH_SHORT).show();
        
        // Ir a ActivityCambiarya con la fecha
        Intent intent = new Intent(DateSelection.this, ActivityCambiarya.class);
        intent.putExtra("fecha_formateada", fechaFormateada);
        intent.putExtra("anio", anio);
        intent.putExtra("mes", mes);
        intent.putExtra("dia", dia);
        startActivity(intent);
        finish();
    }
    
    private boolean esNumeroValido(String texto, int min, int max) {
        try {
            int numero = Integer.parseInt(texto);
            return numero >= min && numero <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
