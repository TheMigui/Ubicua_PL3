package com.example.apppecl3;

public class SensorData {
    private String currentState;
    private boolean pedestrianWaiting;
    private String time;

    // Constructor vacío necesario para Retrofit
    public SensorData() {}

    // Constructor
    public SensorData(String currentState, boolean pedestrianWaiting, String time) {
        this.currentState = currentState;
        this.pedestrianWaiting = pedestrianWaiting;
        this.time = time;
    }

    // Getters
    public String getCurrentState() { return currentState; }
    public boolean isPedestrianWaiting() { return pedestrianWaiting; }
    public String getTime() { return time; }

    // Setters (opcionales, pero útiles)
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public void setPedestrianWaiting(boolean pedestrianWaiting) { this.pedestrianWaiting = pedestrianWaiting; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "SensorData{" +
                "currentState='" + currentState + '\'' +
                ", pedestrianWaiting=" + pedestrianWaiting +
                ", time='" + time + '\'' +
                '}';
    }
}