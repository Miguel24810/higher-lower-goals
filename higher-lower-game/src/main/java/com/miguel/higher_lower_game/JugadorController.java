package com.miguel.higher_lower_game;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorRepository jugadorRepository;

    public JugadorController(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }
    @GetMapping
    public List<Jugador> obtenerTodos() {
    return jugadorRepository.findAll();
}   
    @PostMapping
    public Jugador crear(@RequestBody  Jugador jugador){
        return jugadorRepository.save(jugador);

    }

    @GetMapping("/random-pair")
    public List<Jugador> obtenerParAleatorio(){
        return jugadorRepository.obtenerDosAleatorios();}



}