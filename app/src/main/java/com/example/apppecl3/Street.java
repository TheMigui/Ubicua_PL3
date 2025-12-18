package com.example.apppecl3;

public class Street {
    private String id;
    private String nombre;

    // Constructor vacío necesario para Retrofit
    public Street() {}

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}