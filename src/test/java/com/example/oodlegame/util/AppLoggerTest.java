package com.example.oodlegame.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppLoggerTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void limpiarConfiguracion() {
        System.clearProperty("oodle.log.path");
    }

    @Test
    void errorCreaArchivoLogConFechaMensajeYExcepcion() throws Exception {
        Path logPath = tempDir.resolve("oodle-game.log");
        System.setProperty("oodle.log.path", logPath.toString());

        AppLogger.error("Error de prueba", new RuntimeException("fallo controlado"));

        String contenido = Files.readString(logPath, StandardCharsets.UTF_8);

        assertTrue(Files.exists(logPath));
        assertTrue(contenido.matches("(?s).*\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] \\[ERROR\\].*"));
        assertTrue(contenido.contains("Error de prueba"));
        assertTrue(contenido.contains("RuntimeException: fallo controlado"));
    }
}
