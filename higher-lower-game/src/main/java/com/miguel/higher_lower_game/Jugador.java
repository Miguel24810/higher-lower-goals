package com.miguel.higher_lower_game;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 

public class Jugador {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String equipo;
    private int golesTemporada;
    private int golesCarrera;
    private int golesCompeticion;


    public Long getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public String getEquipo(){
        return equipo;
    }
    public int getGolesCarrera(){
        return golesCarrera;
    }
        
    public int getGolesTemporada(){
        return golesTemporada;
    }    
    public int getGolesCompeticion(){
        return golesCompeticion;
    }

    public void setId(Long id){
        this.id=id;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public void setEquipo(String equipo){
        this.equipo=equipo;
    }
    public void setGoles_carrera(int golesCarrera){
        this.golesCarrera=golesCarrera;
    }
    public void setGoles_temporada(int golesTemporada){
        this.golesTemporada=golesTemporada;
    }
    public void setGoles_competicion(int golesCompeticion){
        this.golesCompeticion=golesCompeticion;
    }
}

