package com.example.apppecl3;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private List<DatoSensor> listaSensores;

    public SensorAdapter(List<DatoSensor> listaSensores) {
        this.listaSensores = listaSensores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sensor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatoSensor sensor = listaSensores.get(position);

        holder.tvSensorId.setText(sensor.getSensorId());
        holder.tvEstado.setText(sensor.getCurrentState());
        holder.tvHora.setText(sensor.getTime());

        // Cambiar color según el estado
        if ("green".equalsIgnoreCase(sensor.getCurrentState())) {
            holder.tvEstado.setTextColor(Color.parseColor("#4CAF50")); // Verde
            holder.tvEstado.setText("VERDE");
        } else if ("red".equalsIgnoreCase(sensor.getCurrentState())) {
            holder.tvEstado.setTextColor(Color.parseColor("#F44336")); // Rojo
            holder.tvEstado.setText("ROJO");
        } else {
            holder.tvEstado.setTextColor(Color.parseColor("#FF9800")); // Naranja
        }
    }

    @Override
    public int getItemCount() {
        return listaSensores != null ? listaSensores.size() : 0;
    }

    public void actualizarLista(List<DatoSensor> nuevaLista) {
        this.listaSensores = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSensorId, tvEstado, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSensorId = itemView.findViewById(R.id.tvSensorId);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }
}
