package com.miguel.higher_lower_game.service;

import org.springframework.stereotype.Service;

import com.miguel.higher_lower_game.model.Jugador;

@Service
public class JugadorService {

    public int obtenerGolesPorCategoria(Jugador jugador, String categoria) {
        if (categoria.equals("golesCarrera")) {
            return jugador.getGolesCarrera();
        } else if (categoria.equals("golesTemporada")) {
            return jugador.getGolesTemporada();
        } else if (categoria.equals("golesSeleccion")) {
            return jugador.getGolesSeleccion();
        } else {
            throw new IllegalArgumentException("Categoría no válida: " + categoria);
        }
    }

    public boolean comparar(Jugador elegido, Jugador otro, String categoria) {
        int golesElegido = obtenerGolesPorCategoria(elegido, categoria);
        int golesOtro = obtenerGolesPorCategoria(otro, categoria);
        return golesElegido >= golesOtro;
    }
}
