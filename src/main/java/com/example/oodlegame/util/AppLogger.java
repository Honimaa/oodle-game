package com.example.oodlegame.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class AppLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Path DEFAULT_LOG_PATH = Paths.get("logs", "oodle-game.log");
    private static final String LOG_PATH_PROPERTY = "oodle.log.path";

    private AppLogger() {
    }

    public static void error(String mensaje, Throwable error) {
        escribir("ERROR", mensaje, error);
    }

    public static void info(String mensaje) {
        escribir("INFO", mensaje, null);
    }

    private static synchronized void escribir(String nivel, String mensaje, Throwable error) {
        Path logPath = obtenerRutaLog();

        try {
            Path parent = logPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    logPath,
                    construirEntrada(nivel, mensaje, error),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ioException) {
            System.err.println("No se pudo escribir en el archivo log: " + ioException.getMessage());
        }
    }

    private static Path obtenerRutaLog() {
        String customPath = System.getProperty(LOG_PATH_PROPERTY);

        if (customPath == null || customPath.isBlank()) {
            return DEFAULT_LOG_PATH;
        }

        return Paths.get(customPath);
    }

    private static String construirEntrada(String nivel, String mensaje, Throwable error) {
        StringBuilder entrada = new StringBuilder();
        entrada.append("[")
                .append(LocalDateTime.now().format(FORMATTER))
                .append("] [")
                .append(nivel)
                .append("] ")
                .append(mensaje)
                .append(System.lineSeparator());

        if (error != null) {
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            entrada.append(sw).append(System.lineSeparator());
        }

        return entrada.toString();
    }
}
