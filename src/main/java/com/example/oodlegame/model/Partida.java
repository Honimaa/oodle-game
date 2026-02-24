package com.example.oodlegame.model;

import java.time.LocalDateTime;

public class Partida {

    private int id;
    private Usuario usuario;
    private String ecuacionObjetivo;
    private int intentosUsados;
    private boolean victoria;
    private LocalDateTime fecha;

    public Partida() {
    }

    public Partida(int id, Usuario usuario, String ecuacionObjetivo, int intentosUsados, boolean victoria, LocalDateTime fecha) {
        this.id = id;
        this.usuario = usuario;
        this.ecuacionObjetivo = ecuacionObjetivo;
        this.intentosUsados = intentosUsados;
        this.victoria = victoria;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEcuacionObjetivo() {
        return ecuacionObjetivo;
    }

    public void setEcuacionObjetivo(String ecuacionObjetivo) {
        this.ecuacionObjetivo = ecuacionObjetivo;
    }

    public int getIntentosUsados() {
        return intentosUsados;
    }

    public void setIntentosUsados(int intentosUsados) {
        this.intentosUsados = intentosUsados;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

