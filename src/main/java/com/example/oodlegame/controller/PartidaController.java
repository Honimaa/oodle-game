package com.example.oodlegame.controller;

import com.example.oodlegame.model.Intento;
import com.example.oodlegame.model.Partida;
import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.service.PartidaDAO;
import com.example.oodlegame.util.AppLogger;
import com.example.oodlegame.util.JuegoValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartidaController {

    @FXML private TextField r1c1, r1c2, r1c3, r1c4;
    @FXML private TextField r2c1, r2c2, r2c3, r2c4;
    @FXML private TextField r3c1, r3c2, r3c3, r3c4;
    @FXML private TextField r4c1, r4c2, r4c3, r4c4;
    @FXML private TextField r5c1, r5c2, r5c3, r5c4;
    @FXML private TextField r6c1, r6c2, r6c3, r6c4;

    @FXML private Label r1op1, r1op2, r1op3;
    @FXML private Label r2op1, r2op2, r2op3;
    @FXML private Label r3op1, r3op2, r3op3;
    @FXML private Label r4op1, r4op2, r4op3;
    @FXML private Label r5op1, r5op2, r5op3;
    @FXML private Label r6op1, r6op2, r6op3;

    @FXML private Label ans1, ans2, ans3, ans4, ans5, ans6;

    @FXML private Circle dot1, dot2, dot3, dot4, dot5, dot6;

    private static final String COLOR_CORRECT = "#6AAA64";
    private static final String COLOR_PARTIAL = "#C9B458";
    private static final String COLOR_WRONG = "#787C7E";
    private static final String COLOR_DEFAULT_BG = "#1A2535";
    private static final String COLOR_DEFAULT_BORDER = "#1E2D40";
    private static final String COLOR_TARGET_BG = "#1E1A35";
    private static final String COLOR_TARGET_BORDER = "#3A2D6A";
    private static final String COLOR_BLUE = "#3A5BD9";
    private static final String COLOR_TEXT_MUTED = "#5A7A8A";

    private static final String CELL_STYLE_BASE =
            "-fx-font-size: 24px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-alignment: center; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; ";

    private static final String RESULT_STYLE_BASE =
            "-fx-font-size: 22px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-alignment: center; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; ";

    private Usuario usuarioActual;

    private Partida partida;
    private Intento intento;
    private final List<Intento> intentos = new ArrayList<>();

    private int[] solucionNums;
    private String[] solucionOps;
    private int resultadoEsperado;
    private int filaActual = 0;
    private boolean juegoTerminado = false;

    private TextField[][] fields;
    private Label[][] operadores;
    private Label[] ansLabels;
    private Circle[] dots;

    @FXML
    public void initialize() {
        fields = new TextField[][] {
                { r1c1, r1c2, r1c3, r1c4 },
                { r2c1, r2c2, r2c3, r2c4 },
                { r3c1, r3c2, r3c3, r3c4 },
                { r4c1, r4c2, r4c3, r4c4 },
                { r5c1, r5c2, r5c3, r5c4 },
                { r6c1, r6c2, r6c3, r6c4 }
        };

        operadores = new Label[][] {
                { r1op1, r1op2, r1op3 },
                { r2op1, r2op2, r2op3 },
                { r3op1, r3op2, r3op3 },
                { r4op1, r4op2, r4op3 },
                { r5op1, r5op2, r5op3 },
                { r6op1, r6op2, r6op3 }
        };

        ansLabels = new Label[] { ans1, ans2, ans3, ans4, ans5, ans6 };
        dots = new Circle[] { dot1, dot2, dot3, dot4, dot5, dot6 };

        configurarCampos();
        iniciarPartida();
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;

        if (this.partida != null) {
            this.partida.setUsuario(usuario);
        }
    }

    @FXML
    private void onHomeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            if (this.usuarioActual != null) {
                menuController.setUsuario(this.usuarioActual);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) fields[0][0].getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            AppLogger.error("Error al volver desde partida al menu", e);
            mostrarAlerta("Error", "No se pudo volver al menú.");
        }
    }

    @FXML
    private void onEnviarIntento() {
        if (this.juegoTerminado) {
            return;
        }

        int[] numerosIntento = leerYValidarFila(this.filaActual);
        if (numerosIntento == null) {
            return;
        }

        setFilaEditable(this.filaActual, false);

        String[] estados = evaluarIntento(numerosIntento);
        colorearFila(this.filaActual, estados);

        String expresionIntento = numerosIntento[0] + this.solucionOps[0]
                + numerosIntento[1] + this.solucionOps[1]
                + numerosIntento[2] + this.solucionOps[2]
                + numerosIntento[3];

        boolean correcto = esVictoria(estados);

        this.intento = new Intento(expresionIntento, correcto, this.intentos.size() + 1);
        this.intentos.add(this.intento);

        if (this.partida != null) {
            this.partida.setIntentosUsados(this.intentos.size());
        }

        actualizarDots(false);

        if (correcto) {
            finalizarPartida(true);
            return;
        }

        this.filaActual++;

        if (this.filaActual >= 6) {
            finalizarPartida(false);
        } else {
            setFilaEditable(this.filaActual, true);
            actualizarDots(false);
            this.fields[this.filaActual][0].requestFocus();
        }
    }

    private void iniciarPartida() {
        this.filaActual = 0;
        this.juegoTerminado = false;
        this.intentos.clear();

        String ecuacion = generarEcuacion();
        parsearEcuacion(ecuacion);

        this.partida = new Partida();
        this.partida.setUsuario(this.usuarioActual);
        this.partida.setEcuacionObjetivo(ecuacion);
        this.partida.setIntentosUsados(0);
        this.partida.setVictoria(false);
        this.partida.setFecha(LocalDateTime.now());

        this.intento = null;

        for (int fila = 0; fila < 6; fila++) {
            for (int op = 0; op < 3; op++) {
                this.operadores[fila][op].setText(simboloDisplay(this.solucionOps[op]));
                this.operadores[fila][op].setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXT_MUTED + ";");
            }
        }

        for (Label ans : this.ansLabels) {
            ans.setText(String.valueOf(this.resultadoEsperado));
            ans.setStyle(RESULT_STYLE_BASE +
                    "-fx-background-color: " + COLOR_TARGET_BG + "; " +
                    "-fx-border-color: " + COLOR_TARGET_BORDER + ";");
        }

        for (int fila = 0; fila < 6; fila++) {
            setFilaEditable(fila, false);
            for (TextField tf : this.fields[fila]) {
                tf.clear();
                aplicarEstiloDefault(tf);
            }
        }

        setFilaEditable(0, true);
        actualizarDots(false);
        this.fields[0][0].requestFocus();
    }

    private void configurarCampos() {
        for (TextField[] fila : this.fields) {
            for (TextField tf : fila) {
                tf.textProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue.matches("[1-9]?")) {
                        tf.setText(oldValue);
                    }
                });
            }
        }
    }

    private int[] leerYValidarFila(int fila) {
        int[] nums = new int[4];

        for (int col = 0; col < 4; col++) {
            String texto = this.fields[fila][col].getText().trim();

            if (!JuegoValidator.esNumeroValido(texto)) {
                mostrarAlerta("Formato inválido", "Cada casilla debe contener un número del 1 al 9.");
                return null;
            }

            nums[col] = Integer.parseInt(texto);
        }

        return nums;
    }

    private String[] evaluarIntento(int[] numerosIntento) {
        return JuegoValidator.evaluarIntento(numerosIntento, this.solucionNums);
    }

    private void colorearFila(int fila, String[] estados) {
        for (int col = 0; col < 4; col++) {
            String color = switch (estados[col]) {
                case "CORRECT" -> COLOR_CORRECT;
                case "PARTIAL" -> COLOR_PARTIAL;
                default -> COLOR_WRONG;
            };

            this.fields[fila][col].setStyle(CELL_STYLE_BASE +
                    "-fx-background-color: " + color + "; " +
                    "-fx-border-color: " + color + "; " +
                    "-fx-text-fill: #FFFFFF;");
        }
    }

    private void aplicarEstiloDefault(TextField tf) {
        tf.setStyle(CELL_STYLE_BASE +
                "-fx-background-color: " + COLOR_DEFAULT_BG + "; " +
                "-fx-border-color: " + COLOR_DEFAULT_BORDER + "; " +
                "-fx-text-fill: #FFFFFF;");
    }

    private void actualizarDots(boolean partidaFinalizada) {
        for (int i = 0; i < this.dots.length; i++) {
            this.dots[i].setStroke(Color.web(COLOR_DEFAULT_BORDER));
            this.dots[i].setStrokeWidth(1.5);

            if (i < this.intentos.size()) {
                this.dots[i].setFill(Color.web(COLOR_BLUE));
            } else if (!partidaFinalizada && i == this.filaActual) {
                this.dots[i].setFill(Color.web(COLOR_PARTIAL));
            } else {
                this.dots[i].setFill(Color.web(COLOR_DEFAULT_BG));
            }
        }
    }

    private void finalizarPartida(boolean victoria) {
        this.juegoTerminado = true;

        if (this.partida != null) {
            this.partida.setVictoria(victoria);
            this.partida.setIntentosUsados(this.intentos.size());
            this.partida.setFecha(LocalDateTime.now());

            if (this.usuarioActual != null) {
                try {
                    PartidaDAO partidaDAO = new PartidaDAO();
                    partidaDAO.guardarPartida(this.partida);
                } catch (Exception e) {
                    AppLogger.error("Error al guardar partida finalizada", e);
                    mostrarAlerta("Error", "No se pudo guardar la partida.");
                }
            }
        }

        for (int i = 0; i < this.dots.length; i++) {
            if (i < this.intentos.size()) {
                this.dots[i].setFill(Color.web(victoria ? COLOR_CORRECT : COLOR_WRONG));
            } else {
                this.dots[i].setFill(Color.web(COLOR_DEFAULT_BG));
            }
            this.dots[i].setStroke(Color.web(victoria ? COLOR_CORRECT : COLOR_WRONG));
        }

        actualizarDots(true);
        mostrarMensajeFinal(victoria);
    }

    private void mostrarMensajeFinal(boolean victoria) {
        String titulo = victoria ? "Ganaste" : "Fin del juego";
        String mensaje = victoria
                ? "Encontraste la ecuación correcta."
                : "La ecuación era: " + this.solucionNums[0] + " " + simboloDisplay(this.solucionOps[0])
                + " " + this.solucionNums[1] + " " + simboloDisplay(this.solucionOps[1])
                + " " + this.solucionNums[2] + " " + simboloDisplay(this.solucionOps[2])
                + " " + this.solucionNums[3] + " = " + this.resultadoEsperado;

        mostrarAlerta(titulo, mensaje);
    }

    public static String generarEcuacion() {
        Random rand = new Random();
        String[] ops = { "+", "-", "*", "/" };
        int intentos = 0;

        while (intentos < 1000) {
            int a = rand.nextInt(9) + 1;
            int b = rand.nextInt(9) + 1;
            int c = rand.nextInt(9) + 1;
            int d = rand.nextInt(9) + 1;

            String op1 = ops[rand.nextInt(ops.length)];
            String op2 = ops[rand.nextInt(ops.length)];
            String op3 = ops[rand.nextInt(ops.length)];

            if (!esDivisionValida(a, op1, b, op2, c, op3, d)) {
                intentos++;
                continue;
            }

            int resultado = calcularResultado(a, op1, b, op2, c, op3, d);

            if (Math.abs(resultado) <= 99) {
                return a + op1 + b + op2 + c + op3 + d + "=" + resultado;
            }

            intentos++;
        }

        return "2*3+4-2=8";
    }

    private static boolean esDivisionValida(int a, String op1, int b, String op2, int c, String op3, int d) {
        return JuegoValidator.esDivisionValida(a, op1, b, op2, c, op3, d);
    }

    private static int calcularResultado(int a, String op1, int b, String op2, int c, String op3, int d) {
        return JuegoValidator.calcularResultado(a, op1, b, op2, c, op3, d);
    }

    private void parsearEcuacion(String ecuacion) {
        String[] partes = ecuacion.split("=");
        this.resultadoEsperado = Integer.parseInt(partes[1]);

        String expr = partes[0];
        this.solucionNums = new int[4];
        this.solucionOps = new String[3];

        int numIdx = 0;
        int opIdx = 0;

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (Character.isDigit(ch)) {
                this.solucionNums[numIdx++] = ch - '0';
            } else {
                this.solucionOps[opIdx++] = String.valueOf(ch);
            }
        }
    }

    private String simboloDisplay(String op) {
        return switch (op) {
            case "*" -> "×";
            case "/" -> "÷";
            default -> op;
        };
    }

    private boolean esVictoria(String[] estados) {
        return JuegoValidator.esVictoria(estados);
    }

    private void setFilaEditable(int fila, boolean editable) {
        for (TextField tf : this.fields[fila]) {
            tf.setEditable(editable);
            tf.setMouseTransparent(!editable);
            tf.setFocusTraversable(editable);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
