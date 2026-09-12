package com.miguel.higher_lower_game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByNombre(String nombre);
    
}
