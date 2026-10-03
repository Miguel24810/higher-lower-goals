package com.miguel.higher_lower_game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miguel.higher_lower_game.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByNombre(String nombre);
    List<Usuario> findAll();

}
