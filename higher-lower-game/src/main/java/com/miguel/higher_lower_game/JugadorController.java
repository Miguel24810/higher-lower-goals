package com.miguel.higher_lower_game;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
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

    }else if(categoria.equals("golesCompeticion")){
        golesElegido=elegido.getGolesCompeticion();
        golesOtro= otro.getGolesCompeticion();

    } else {
    throw new IllegalArgumentException("Categoría no válida: " + categoria);
}

    return golesElegido >= golesOtro;
}

}