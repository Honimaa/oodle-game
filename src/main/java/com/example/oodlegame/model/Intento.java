package com.example.oodlegame.model;

public class Intento {

    private String expresion;
    private boolean correcto;
    private int numeroIntento;

    public Intento() {
    }

    public Intento(String expresion, boolean correcto, int numeroIntento) {
        this.expresion = expresion;
        this.correcto = correcto;
        this.numeroIntento = numeroIntento;
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

    public int getNumeroIntento() {
        return numeroIntento;
    }

    public void setNumeroIntento(int numeroIntento) {
        this.numeroIntento = numeroIntento;
    }
}
