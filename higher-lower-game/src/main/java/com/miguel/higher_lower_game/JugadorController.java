package com.miguel.higher_lower_game;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
@RestController
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorRepository jugadorRepository;

    @Value("${admin.sync.key}")
    private String claveAdmin;

    public JugadorController(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
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

    int golesElegido;
    int golesOtro;

    if(categoria.equals("golesCarrera")){
        golesElegido= elegido.getGolesCarrera();
        golesOtro=otro.getGolesCarrera();
    }else if(categoria.equals("golesTemporada")){
        golesElegido= elegido.getGolesTemporada();
        golesOtro=otro.getGolesTemporada();

    }else if(categoria.equals("golesSeleccion")){
        golesElegido=elegido.getGolesSeleccion();
        golesOtro= otro.getGolesSeleccion();

    } else {
    throw new IllegalArgumentException("Categoría no válida: " + categoria);
}

    return golesElegido >= golesOtro;
}

}