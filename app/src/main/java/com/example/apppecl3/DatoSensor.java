package com.example.apppecl3;

import com.google.gson.annotations.SerializedName;

public class DatoSensor {

    @SerializedName("sensor_id")
    private String sensorId;

    @SerializedName("current_state")
    private String currentState;

    @SerializedName("time")
    private String time;

    // Constructor vacío
    public DatoSensor() {}

    // Constructor
    public DatoSensor(String sensorId, String currentState, String time) {
        this.sensorId = sensorId;
        this.currentState = currentState;
        this.time = time;
    }

    // Getters y setters
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DatoSensor{" +
                "sensorId='" + sensorId + '\'' +
                ", currentState='" + currentState + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}