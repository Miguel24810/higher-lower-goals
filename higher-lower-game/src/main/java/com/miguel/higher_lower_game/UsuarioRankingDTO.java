package com.miguel.higher_lower_game;

public class UsuarioRankingDTO {
    private String nombre;
    private int recordCarrera;
    private int recordSeleccion;
    private int recordTemporada;

    public UsuarioRankingDTO(String nombre, int recordCarrera, int recordSeleccion, int recordTemporada) {
        this.nombre = nombre;
        this.recordCarrera = recordCarrera;
        this.recordSeleccion=recordSeleccion;
        this.recordTemporada=recordTemporada;
    }

    public String getNombre() {
        return nombre;
    }

	public int getRecordCarrera() {
		return recordCarrera;

	}
	public int getRecordTemporada() {
		return recordTemporada;
		
	}
	public int getRecordSeleccion() {
		return recordSeleccion;
		
	}
}