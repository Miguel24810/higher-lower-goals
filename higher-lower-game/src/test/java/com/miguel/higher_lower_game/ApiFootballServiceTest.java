package com.miguel.higher_lower_game;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class ApiFootballServiceTest {

    @Test
    void obtenerDatosCrudos_devuelveElJugadorCorrecto() {
        // Arrange: preparamos el mock y los datos falsos
        RestTemplate restTemplateFalso = Mockito.mock(RestTemplate.class);

        String jsonDeMentira = "{\"response\":[{\"player\":{\"id\":276,\"name\":\"Neymar\"}}]}";
        ResponseEntity<String> respuestaFalsa = new ResponseEntity<>(jsonDeMentira, org.springframework.http.HttpStatus.OK);

        Mockito.when(restTemplateFalso.exchange(
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(String.class)
        )).thenReturn(respuestaFalsa);

        ApiFootballService service = new ApiFootballService(restTemplateFalso);

        // Act: llamamos al método real que queremos probar
        // Act
        JsonNode resultado = service.obtenerDatosCrudos(276, 2023);

        // Assert: comprobamos que el resultado es el esperado
// Assert
        assertEquals("Neymar", resultado.get("player").get("name").asText());           
    }
}