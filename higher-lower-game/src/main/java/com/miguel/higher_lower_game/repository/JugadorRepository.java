package com.miguel.higher_lower_game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.miguel.higher_lower_game.model.Jugador;


public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    Optional<Jugador> findByIdApiFootball(Integer idApiFootball);

    @Query(value = "SELECT * FROM jugador ORDER BY RAND() LIMIT 2", nativeQuery = true)
    List<Jugador> obtenerDosAleatorios();
}
