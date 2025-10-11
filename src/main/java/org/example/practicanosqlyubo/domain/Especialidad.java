package org.example.practicanosqlyubo.domain;



public class Especialidad {
    private String nombre;
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    @Override
    public String toString() { return nombre; }
}