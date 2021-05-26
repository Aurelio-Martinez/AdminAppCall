package com.example.adminappcall;

public class Subusuario {


    public String nombre;

    private String key;

    public Subusuario(String nombre, String key) {
        this.nombre = nombre;
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
