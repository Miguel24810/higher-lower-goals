package com.miguel.higher_lower_game;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

        return "Sincronización completada";
    }
}