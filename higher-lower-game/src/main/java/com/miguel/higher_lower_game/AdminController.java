package com.miguel.higher_lower_game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
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

        // Aquí: la lógica de sincronización (la construimos en el siguiente paso)
        int[] idsJugadores = {278}; // ejemplo, los iremos ampliando

        for (int idApi : idsJugadores) {
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
        }
        return "Sincronización completada";
    }

}