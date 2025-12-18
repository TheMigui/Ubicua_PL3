package com.example.apppecl3;

public class DatoSensor {
    // Los nombres DEBEN coincidir EXACTAMENTE con el JSON
    // O usar @SerializedName si los nombres son diferentes

    private String sensor_id;      // ← IGUAL que en JSON
    private String current_state;  // ← IGUAL que en JSON
    private String time;           // ← IGUAL que en JSON

    // Constructor vacío OBLIGATORIO para Retrofit
    public DatoSensor() {}

    // Constructor completo
    public DatoSensor(String sensor_id, String current_state, String time) {
        this.sensor_id = sensor_id;
        this.current_state = current_state;
        this.time = time;
    }

    // Getters y Setters - los nombres pueden ser distintos aquí
    public String getSensorId() {
        return sensor_id;
    }

    public void setSensorId(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    public String getCurrentState() {
        return current_state;
    }

    public void setCurrentState(String current_state) {
        this.current_state = current_state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Para debugging
    @Override
    public String toString() {
        return "DatoSensor{" +
                "sensor_id='" + sensor_id + '\'' +
                ", current_state='" + current_state + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}