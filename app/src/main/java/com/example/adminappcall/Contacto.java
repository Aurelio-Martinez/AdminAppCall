package com.example.adminappcall;

import com.google.firebase.database.Exclude;


// clase personalizada de contacto
public class Contacto {

    //url de la foto
    public final String url;

    private String key;

    //nombre del usuario
    public final String nombre;

    //numero de telefono
    private final String numero;

    private String owner;

    public String getOwner() {
        return owner;
    }





    public Contacto(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
        this.url="0";
    }

    public Contacto(String nombre, String numero, String url) {
        this.nombre = nombre;
        this.numero = numero;
        this.url = url;
    }
    public Contacto(String nombre, String numero, String url,String key, String owner) {
        this.nombre = nombre;
        this.numero = numero;
        this.url = url;
        this.key =key;
        this.owner = owner;
    }


    public String getUrl() {
        return url;
    }


    public String getNombre() {
        return nombre;
    }


    public String getNumero() {
        return numero;
    }



    @Exclude
    public String getKey() {
        return key;
    }

}
