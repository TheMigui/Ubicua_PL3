package com.example.apppecl3;

public class DatoSensor {
    private String sensorId;
    private String currentState;
    private String time;

    public DatoSensor(String sensorId, String currentState, String time) {
        this.sensorId = sensorId;
        this.currentState = currentState;
        this.time = time;
    }

    public String getSensorId() { return sensorId; }
    public String getCurrentState() { return currentState; }
    public String getTime() { return time; }

    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public void setTime(String time) { this.time = time; }
}