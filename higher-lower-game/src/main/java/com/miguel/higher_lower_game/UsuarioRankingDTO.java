package com.miguel.higher_lower_game;

public class UsuarioRankingDTO {
    private String nombre;
    private int record;

    public UsuarioRankingDTO(String nombre, int record) {
        this.nombre = nombre;
        this.record = record;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRecord() {
        return record;
    }
}