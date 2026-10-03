package com.miguel.higher_lower_game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.miguel.higher_lower_game.model.Jugador;
import com.miguel.higher_lower_game.repository.JugadorRepository;
import com.miguel.higher_lower_game.service.ApiFootballService;

import java.util.Optional;
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ApiFootballService apiFootballService;
    private final JugadorRepository jugadorRepository;

    @Value("${admin.sync.key}")
    private String claveAdmin;

    public AdminController(ApiFootballService apiFootballService, JugadorRepository jugadorRepository) {
        this.apiFootballService = apiFootballService;
        this.jugadorRepository = jugadorRepository;
    }

    @PostMapping("/sincronizar-jugadores")
    public String sincronizar(@RequestHeader("X-Admin-Key") String claveRecibida) {
        if (!claveRecibida.equals(claveAdmin)) {
            throw new RuntimeException("No autorizado");
        }

       
        int[] idsJugadores = {278,56,133609,1100,762,1496,538,53,2472,744,754,756,521,619,931,1323,49,643,978,1460,1946,2937,629,631,636,2291,3033,174,8,184,508,9,153,328,1138,272}; 

        for (int idApi : idsJugadores) {
            try{
            JsonNode datos = apiFootballService.obtenerDatosCrudos(idApi, 2024);
            Jugador jugadorNuevo = apiFootballService.crearJugadorDesdeJson(datos);

            Optional<Jugador> existente = jugadorRepository.findByIdApiFootball(idApi);

            if (existente.isPresent()) {
                Jugador jugadorAActualizar = existente.get();
                jugadorAActualizar.setNombre(jugadorNuevo.getNombre());
                jugadorAActualizar.setUrlFoto(jugadorNuevo.getUrlFoto());
                jugadorAActualizar.setGolesTemporada(jugadorNuevo.getGolesTemporada());
                jugadorAActualizar.setGolesSeleccion(jugadorNuevo.getGolesSeleccion());
                jugadorRepository.save(jugadorAActualizar);
            } else {
                jugadorNuevo.setIdApiFootball(idApi);
                jugadorRepository.save(jugadorNuevo);
            }
            Thread.sleep(6500);
        } catch(Exception e){
            System.out.println("Fallo con el jugador "+ idApi +": "+ e.getMessage());
        }}
        return "Sincronización completada";
    }

}