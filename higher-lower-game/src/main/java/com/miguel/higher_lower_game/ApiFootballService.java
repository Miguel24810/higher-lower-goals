
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

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode raiz = mapper.readTree(respuesta.getBody());
            return raiz.get("response").get(0);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener datos del jugador " + idApiFootball, e);
        }
    }
}