
package com.miguel.higher_lower_game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Usuario {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idUsuario;
    @Column(unique=true)
    private String nombre;
    private String password;
    private int recordCarrera;
	private int recordTemporada;
	private int recordSeleccion;
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRecordCarrera(int recordCarrera) {
		this.recordCarrera = recordCarrera;
	}
	public void setRecordTemporada(int recordTemporada) {
		this.recordTemporada = recordTemporada;
	}
	public void setRecordSeleccion(int recordSeleccion) {
		this.recordSeleccion = recordSeleccion;
	}
	public String getNombre() {
		return nombre;
	}
	public String getPassword() {
		return password;
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
