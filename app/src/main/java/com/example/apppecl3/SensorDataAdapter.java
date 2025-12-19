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

        holder.tvFechaHora.setText(data.getTime());
        holder.tvEstado.setText(data.getCurrentState());
        holder.tvPeatones.setText(data.isPedestrianWaiting() ? "Sí" : "No");

        // Cambiar color según el estado
        switch (data.getCurrentState().toLowerCase()) {
            case "green":
                holder.tvEstado.setTextColor(Color.GREEN);
                break;
            case "red":
                holder.tvEstado.setTextColor(Color.RED);
                break;
            case "amber":
                holder.tvEstado.setTextColor(Color.parseColor("#FFA500")); // Naranja
                break;
            default:
                holder.tvEstado.setTextColor(Color.BLACK);
        }

        // Color para peatones
        if (data.isPedestrianWaiting()) {
            holder.tvPeatones.setTextColor(Color.RED);
        } else {
            holder.tvPeatones.setTextColor(Color.GREEN);
        }
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
        TextView tvFechaHora, tvEstado, tvPeatones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvPeatones = itemView.findViewById(R.id.tvPeatones);
        }
    }
}
