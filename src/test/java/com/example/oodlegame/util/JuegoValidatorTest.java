package com.example.oodlegame.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JuegoValidatorTest {

    @Test
    void esNumeroValidoAceptaSoloNumerosDelUnoAlNueve() {
        assertTrue(JuegoValidator.esNumeroValido("1"));
        assertTrue(JuegoValidator.esNumeroValido("9"));
        assertTrue(JuegoValidator.esNumeroValido(" 5 "));
    }

    @Test
    void esNumeroValidoRechazaVaciosCeroLetrasYDosDigitos() {
        assertFalse(JuegoValidator.esNumeroValido(""));
        assertFalse(JuegoValidator.esNumeroValido("0"));
        assertFalse(JuegoValidator.esNumeroValido("10"));
        assertFalse(JuegoValidator.esNumeroValido("a"));
        assertFalse(JuegoValidator.esNumeroValido(null));
    }

    @Test
    void evaluarIntentoMarcaCorrectPartialYWrong() {
        int[] solucion = { 1, 2, 3, 4 };
        int[] intento = { 1, 3, 5, 2 };

        String[] estados = JuegoValidator.evaluarIntento(intento, solucion);

        assertArrayEquals(new String[] { "CORRECT", "PARTIAL", "WRONG", "PARTIAL" }, estados);
    }

    @Test
    void evaluarIntentoControlaNumerosRepetidos() {
        int[] solucion = { 1, 1, 2, 3 };
        int[] intento = { 1, 2, 1, 1 };

        String[] estados = JuegoValidator.evaluarIntento(intento, solucion);

        assertArrayEquals(new String[] { "CORRECT", "PARTIAL", "PARTIAL", "WRONG" }, estados);
    }

    @Test
    void esVictoriaSoloEsVerdaderoCuandoTodosSonCorrectos() {
        assertTrue(JuegoValidator.esVictoria(new String[] { "CORRECT", "CORRECT", "CORRECT", "CORRECT" }));
        assertFalse(JuegoValidator.esVictoria(new String[] { "CORRECT", "PARTIAL", "CORRECT", "CORRECT" }));
        assertFalse(JuegoValidator.esVictoria(null));
    }

    @Test
    void calcularResultadoRespetaPrioridadDeOperadores() {
        int resultado = JuegoValidator.calcularResultado(2, "+", 3, "*", 4, "-", 5);

        assertEquals(9, resultado);
    }

    @Test
    void calcularResultadoSoportaDivisionEnteraValida() {
        int resultado = JuegoValidator.calcularResultado(8, "/", 2, "+", 3, "*", 2);

        assertEquals(10, resultado);
    }

    @Test
    void esDivisionValidaRechazaDivisionesNoEnteras() {
        assertFalse(JuegoValidator.esDivisionValida(5, "/", 2, "+", 3, "-", 1));
        assertTrue(JuegoValidator.esDivisionValida(8, "/", 2, "+", 3, "-", 1));
    }
}
