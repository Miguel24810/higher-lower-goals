
package com.miguel.higher_lower_game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiFootballService {

    private final RestTemplate restTemplate;

    @Value("${api.football.key}")
    private String apiKey;

    public ApiFootballService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonNode obtenerDatosCrudos(int idApiFootball, int temporada) {
        String url = "https://v3.football.api-sports.io/players?id=" + idApiFootball + "&season=" + temporada;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> respuesta = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JsonNode raiz;
        try {
            ObjectMapper mapper = new ObjectMapper();
            raiz = mapper.readTree(respuesta.getBody());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener datos del jugador " + idApiFootball, e);
        }

        JsonNode response = raiz == null ? null : raiz.get("response");
        if (response == null || !response.isArray() || response.isEmpty()) {
            throw new RuntimeException("Sin datos para el jugador " + idApiFootball + " en la temporada " + temporada);
        }
        return response.get(0);
    }
        public Jugador crearJugadorDesdeJson(JsonNode datos) {
        JsonNode player = datos.get("player");
        JsonNode statistics = datos.get("statistics");

        String nombre = player.get("name").asText();
        String foto = player.get("photo").asText();
        String nacionalidad = player.get("nationality").asText();
        String equipo = statistics.get(0).get("team").get("name").asText();

        int golesTemporada = 0;
        int golesSeleccion = 0;

        int[] ligasConocidas = {140, 39, 78, 61, 135};

        for (JsonNode entrada : statistics) {
            int ligaId = entrada.get("league").get("id").isNull() ? -1 : entrada.get("league").get("id").asInt();
            String equipoNombre = entrada.get("team").get("name").asText();
            int golesEntrada = entrada.get("goals").get("total").isNull() ? 0 : entrada.get("goals").get("total").asInt();

            for (int ligaConocida : ligasConocidas) {
                if (ligaId == ligaConocida) {
                    golesTemporada += golesEntrada;
                    break;
                }
            }

            if (equipoNombre.equals(nacionalidad)) {
                golesSeleccion += golesEntrada;
            }
        }

        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEquipo(equipo);
        jugador.setUrlFoto(foto);
        jugador.setGolesTemporada(golesTemporada);
        jugador.setGolesSeleccion(golesSeleccion);

        return jugador;
    }
}