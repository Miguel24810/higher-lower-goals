package com.miguel.higher_lower_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorServiceTest {

    @Test
    void comparar_devuelveTrueCuandoElegidoTieneMasGoles() {
        // Arrange
        JugadorService service = new JugadorService();

        Jugador elegido = new Jugador();
        elegido.setGolesCarrera(100);

        Jugador otro = new Jugador();
        otro.setGolesCarrera(50);

        // Act
        boolean resultado = service.comparar(elegido, otro, "golesCarrera");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void obtenerGolesPorCategoria_lanzaExcepcionCuandoCategoriaNoExiste() {
        // Arrange
        JugadorService service = new JugadorService();
        Jugador jugador = new Jugador();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.obtenerGolesPorCategoria(jugador, "golesInventados"));
    }
}
