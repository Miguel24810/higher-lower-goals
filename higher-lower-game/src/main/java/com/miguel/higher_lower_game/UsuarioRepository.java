package com.miguel.higher_lower_game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByNombre(String nombre);
    List<Usuario> findAll();

}
