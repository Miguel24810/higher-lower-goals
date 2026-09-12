package com.miguel.higher_lower_game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    @Query(value = "SELECT * FROM jugador ORDER BY RAND() LIMIT 2", nativeQuery = true)
    List<Jugador> obtenerDosAleatorios();
}
