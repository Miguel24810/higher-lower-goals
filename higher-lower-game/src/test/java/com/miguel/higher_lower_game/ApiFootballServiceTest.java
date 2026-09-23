package com.miguel.higher_lower_game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class ApiFootballServiceTest {

    @Test
    void obtenerDatosCrudos_devuelveElJugadorCorrecto() {
        // Arrange
        RestTemplate restTemplateFalso = Mockito.mock(RestTemplate.class);

        String jsonDeMentira = "{\"response\":[{\"player\":{\"id\":276,\"name\":\"Neymar\"}}]}";
        ResponseEntity<String> respuestaFalsa = new ResponseEntity<>(jsonDeMentira, HttpStatus.OK);

        Mockito.when(restTemplateFalso.exchange(
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(String.class)
        )).thenReturn(respuestaFalsa);

        ApiFootballService service = new ApiFootballService(restTemplateFalso);

        // Act
        JsonNode resultado = service.obtenerDatosCrudos(276, 2023);

        // Assert
        assertEquals("Neymar", resultado.get("player").get("name").asText());
    }

    @Test
    void crearJugadorDesdeJson_sumaGolesCorrectamente() throws Exception {
        // Arrange
        String jsonDeMentira = "{\"player\":{\"name\":\"Neymar\",\"photo\":\"foto.jpg\",\"nationality\":\"Brasil\"},\"statistics\":[{\"league\":{\"id\":140},\"team\":{\"name\":\"Barcelona\"},\"goals\":{\"total\":10}},{\"league\":{\"id\":999},\"team\":{\"name\":\"Brasil\"},\"goals\":{\"total\":3}}]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode datos = mapper.readTree(jsonDeMentira);
        ApiFootballService service = new ApiFootballService(Mockito.mock(RestTemplate.class));

        // Act
        Jugador resultado = service.crearJugadorDesdeJson(datos);

        // Assert
        assertEquals(10, resultado.getGolesTemporada());
        assertEquals(3, resultado.getGolesSeleccion());
    }
}