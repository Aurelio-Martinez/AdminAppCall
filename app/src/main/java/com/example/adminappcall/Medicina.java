package com.example.adminappcall;


import com.google.firebase.database.Exclude;

// clase personalizada de contacto
public class Medicina {

    //url de la foto
    public String url;

    private String key;

    //nombre del medicamento
    public String nombre;
    public String dosis;
    public Long dia;
    public Long mes;
    public Long year;

    public Long getYear() {
        return year;
    }


    public Long getDia() {
        return dia;
    }


    public Long getMes() {
        return mes;
    }

    public String horario;


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getFinalMedicacion() {
        return dia+"/"+mes+"/"+year;
    }


    public String getHorario() {
        return horario;
    }

    public Medicina(String nombre, String dosis, String url,String key, Long dia,Long mes, Long year, String hora) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.url = url;
        this.key =key;
        this.dia =dia;
        this.mes=mes;
        this.year=year;
        this.horario =hora;
    }


    public String getUrl() {
        return url;
    }


    public String getNombre() {
        return nombre;
    }


    public String getDosis() {
        return dosis;
    }



    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

}
