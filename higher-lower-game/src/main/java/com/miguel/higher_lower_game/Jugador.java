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
    private int golesSeleccion;
    private Integer idApiFootball;
    private String urlFoto;


    public Integer getIdApiFootball() {
        return idApiFootball;
    }
    public void setIdApiFootball(Integer idApiFootball) {
        this.idApiFootball = idApiFootball;
    }
    public String getUrlFoto() {
        return urlFoto;
    }
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
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
    public int getGolesSeleccion(){
        return golesSeleccion;
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
    public void setGolesCarrera(int golesCarrera){
        this.golesCarrera=golesCarrera;
    }
    public void setGolesTemporada(int golesTemporada){
        this.golesTemporada=golesTemporada;
    }
    public void setGolesSeleccion(int golesSeleccion){
        this.golesSeleccion=golesSeleccion;
    }
}

