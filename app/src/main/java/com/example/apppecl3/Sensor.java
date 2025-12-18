package com.example.apppecl3;

public class Sensor {
    private String id;
    private String tipo;
    private String street_id;

    // Constructor vacío necesario para Retrofit
    public Sensor() {}

    // Constructor
    public Sensor(String id, String tipo, String street_id) {
        this.id = id;
        this.tipo = tipo;
        this.street_id = street_id;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStreetId() { return street_id; }
    public void setStreetId(String street_id) { this.street_id = street_id; }
}
