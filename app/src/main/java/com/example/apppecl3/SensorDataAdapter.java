package com.example.apppecl3;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SensorDataAdapter extends RecyclerView.Adapter<SensorDataAdapter.ViewHolder> {

    private List<SensorData> listaDatos;

    public SensorDataAdapter(List<SensorData> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sensor_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SensorData data = listaDatos.get(position);

        // 1. Fecha y hora
        holder.tvFechaHora.setText(data.getTime());

        // 2. Interpretar estado (Gr, Yr, Rr1, Rr2, Rg)
        String[] estados = interpretarEstado(data.getCurrentState());

        // 3. Mostrar estados separados
        holder.tvEstadoCoches.setText("🚗 " + estados[0]);     // Ej: "🚗 VERDE"
        holder.tvEstadoPeatones.setText("🚶 " + estados[1]);   // Ej: "🚶 ROJO"

        // 4. Aplicar colores
        aplicarColorEstado(holder.tvEstadoCoches, estados[0]);
        aplicarColorEstado(holder.tvEstadoPeatones, estados[1]);

        // 5. Mostrar si hay peatón esperando
        if (data.isPedestrianWaiting()) {
            holder.tvPeatonEsperando.setText("🟢 CON PEATÓN ESPERANDO");
            holder.tvPeatonEsperando.setTextColor(Color.GREEN);
            holder.tvPeatonEsperando.setBackgroundColor(Color.parseColor("#E8F5E8"));
        } else {
            holder.tvPeatonEsperando.setText("⚫ SIN PEATONES ESPERANDO");
            holder.tvPeatonEsperando.setTextColor(Color.GRAY);
            holder.tvPeatonEsperando.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }

    private String[] interpretarEstado(String codigoEstado) {
        String estadoCoches = "";
        String estadoPeatones = "";

        switch (codigoEstado) {
            case "Gr":  // Green for cars, Red for pedestrians
                estadoCoches = "VERDE";
                estadoPeatones = "ROJO";
                break;
            case "Yr":  // Yellow for cars, Red for pedestrians
                estadoCoches = "AMARILLO";
                estadoPeatones = "ROJO";
                break;
            case "Rr1": // Red for cars, Red for pedestrians (Fase 1)
                estadoCoches = "ROJO";
                estadoPeatones = "ROJO";
                break;
            case "Rr2": // Red for cars, Red for pedestrians (Fase 2)
                estadoCoches = "ROJO";
                estadoPeatones = "ROJO";
                break;
            case "Rg":  // Red for cars, Green for pedestrians
                estadoCoches = "ROJO";
                estadoPeatones = "VERDE";
                break;
            default:
                estadoCoches = codigoEstado;
                estadoPeatones = "?";
        }

        return new String[]{estadoCoches, estadoPeatones};
    }

    private void aplicarColorEstado(TextView textView, String estado) {
        switch (estado) {
            case "VERDE":
                textView.setTextColor(Color.GREEN);
                textView.setBackgroundColor(Color.parseColor("#E8F5E8"));
                break;
            case "AMARILLO":
                textView.setTextColor(Color.parseColor("#FFA500"));
                textView.setBackgroundColor(Color.parseColor("#FFF3E0"));
                break;
            case "ROJO":
                textView.setTextColor(Color.RED);
                textView.setBackgroundColor(Color.parseColor("#FFEBEE"));
                break;
            default:
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Padding ya está en el XML, pero lo mantenemos por si acaso
        textView.setPadding(8, 4, 8, 4);
    }

    @Override
    public int getItemCount() {
        return listaDatos != null ? listaDatos.size() : 0;
    }

    public void actualizarLista(List<SensorData> nuevaLista) {
        this.listaDatos = nuevaLista;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFechaHora, tvEstadoCoches, tvEstadoPeatones, tvPeatonEsperando;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvEstadoCoches = itemView.findViewById(R.id.tvEstadoCoches);
            tvEstadoPeatones = itemView.findViewById(R.id.tvEstadoPeatones);
            tvPeatonEsperando = itemView.findViewById(R.id.tvPeatonEsperando);
        }
    }
}