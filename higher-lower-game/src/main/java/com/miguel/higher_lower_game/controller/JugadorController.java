package com.miguel.higher_lower_game.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.miguel.higher_lower_game.model.Jugador;
import com.miguel.higher_lower_game.repository.JugadorRepository;
import com.miguel.higher_lower_game.service.JugadorService;

import java.util.Map;
@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorRepository jugadorRepository;
    private final JugadorService jugadorService;

    @Value("${admin.sync.key}")
    private String claveAdmin;

    public JugadorController(JugadorRepository jugadorRepository, JugadorService jugadorService) {
        this.jugadorRepository = jugadorRepository;
        this.jugadorService = jugadorService;
}
    @GetMapping
    public List<Jugador> obtenerTodos() {
    return jugadorRepository.findAll();
}  
    @PostMapping
    public Jugador crear(@RequestHeader(value = "X-Admin-Key", required = false) String claveRecibida,
                         @RequestBody Jugador jugador) {
        if (claveRecibida == null || !MessageDigest.isEqual(
                claveAdmin.getBytes(StandardCharsets.UTF_8), claveRecibida.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        return jugadorRepository.save(jugador);

    }

    @GetMapping("/random-pair")
    public List<Jugador> obtenerParAleatorio(){
        return jugadorRepository.obtenerDosAleatorios();}

    @PostMapping("/comparar")
    public boolean comparar(@RequestBody Map<String, Object> datos) {
        Long idElegido = Long.valueOf(datos.get("idElegido").toString());
        Long idOtro = Long.valueOf(datos.get("idOtro").toString());
        String categoria = datos.get("categoria").toString();

    Jugador elegido = jugadorRepository.findById(idElegido).get();
    Jugador otro = jugadorRepository.findById(idOtro).get();

 return jugadorService.comparar(elegido, otro, categoria);
    }
}