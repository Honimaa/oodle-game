package com.example.oodlegame.model;

public class Intento {

    private String expresion;
    private boolean autenticacion;


    public Intento() {
    }

    public Intento(String expresion, boolean autenticacion) {
        this.expresion = expresion;
        this.autenticacion = autenticacion;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public boolean getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(boolean autenticacion) {
        this.autenticacion = autenticacion;
    }
}
