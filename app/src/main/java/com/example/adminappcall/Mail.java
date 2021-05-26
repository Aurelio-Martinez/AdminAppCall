package com.example.adminappcall;

public class Mail {


    public String mail;

    private String key;

    public Mail(String mail, String key) {
        this.mail = mail;
        this.key = key;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String nombre) {
        this.mail = nombre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
