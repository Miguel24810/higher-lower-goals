package com.miguel.higher_lower_game;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Id;

public interface JugadorRepository extends JpaRepository<Jugador, Long>{
    
}
