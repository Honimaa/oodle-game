package com.example.oodlegame.model;

public class Intento {

    private String expresion;
    private boolean correcto;

    public Intento() {
    }

    public Intento(String expresion, boolean correcto) {
        this.expresion = expresion;
        this.correcto = correcto;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }
}
