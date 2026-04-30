package com.example.oodlegame.controller;

import com.example.oodlegame.model.Intento;
import com.example.oodlegame.model.Partida;
import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.service.PartidaDAO;
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
import java.util.Arrays;
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
    private Partida partidaActual;
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

        if (partidaActual != null) {
            partidaActual.setUsuario(usuario);
        }
    }

    @FXML
    private void onHomeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            if (usuarioActual != null) {
                menuController.setUsuario(usuarioActual);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) fields[0][0].getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver al menú.");
        }
    }

    @FXML
    private void onEnviarIntento() {
        if (juegoTerminado) {
            return;
        }

        int[] intento = leerYValidarFila(filaActual);
        if (intento == null) {
            return;
        }

        setFilaEditable(filaActual, false);

        String[] estados = evaluarIntento(intento);
        colorearFila(filaActual, estados);

        String expresionIntento = intento[0] + solucionOps[0]
                + intento[1] + solucionOps[1]
                + intento[2] + solucionOps[2]
                + intento[3];

        boolean correcto = esVictoria(estados);
        Intento intentoObj = new Intento(expresionIntento, correcto);
        intentos.add(intentoObj);

        if (partidaActual != null) {
            partidaActual.setIntentosUsados(intentos.size());
        }

        actualizarDots(false);

        if (correcto) {
            finalizarPartida(true);
            return;
        }

        filaActual++;

        if (filaActual >= 6) {
            finalizarPartida(false);
        } else {
            setFilaEditable(filaActual, true);
            actualizarDots(false);
            fields[filaActual][0].requestFocus();
        }
    }

    private void iniciarPartida() {
        filaActual = 0;
        juegoTerminado = false;
        intentos.clear();

        String ecuacion = generarEcuacion();
        parsearEcuacion(ecuacion);

        partidaActual = new Partida();
        partidaActual.setUsuario(usuarioActual);
        partidaActual.setEcuacionObjetivo(ecuacion);
        partidaActual.setIntentosUsados(0);
        partidaActual.setVictoria(false);
        partidaActual.setFecha(LocalDateTime.now());

        for (int fila = 0; fila < 6; fila++) {
            for (int op = 0; op < 3; op++) {
                operadores[fila][op].setText(simboloDisplay(solucionOps[op]));
                operadores[fila][op].setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXT_MUTED + ";");
            }
        }

        for (Label ans : ansLabels) {
            ans.setText(String.valueOf(resultadoEsperado));
            ans.setStyle(RESULT_STYLE_BASE +
                    "-fx-background-color: " + COLOR_TARGET_BG + "; " +
                    "-fx-border-color: " + COLOR_TARGET_BORDER + ";");
        }

        for (int fila = 0; fila < 6; fila++) {
            setFilaEditable(fila, false);
            for (TextField tf : fields[fila]) {
                tf.clear();
                aplicarEstiloDefault(tf);
            }
        }

        setFilaEditable(0, true);
        actualizarDots(false);
        fields[0][0].requestFocus();
    }

    private void configurarCampos() {
        for (TextField[] fila : fields) {
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
            String texto = fields[fila][col].getText().trim();

            if (texto.isEmpty() || !texto.matches("[1-9]")) {
                mostrarAlerta("Formato inválido", "Cada casilla debe contener un número del 1 al 9.");
                return null;
            }

            nums[col] = Integer.parseInt(texto);
        }

        return nums;
    }

    private String[] evaluarIntento(int[] intento) {
        String[] estados = new String[4];
        boolean[] solucionUsada = new boolean[4];
        boolean[] intentoUsado = new boolean[4];

        for (int i = 0; i < 4; i++) {
            if (intento[i] == solucionNums[i]) {
                estados[i] = "CORRECT";
                solucionUsada[i] = true;
                intentoUsado[i] = true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (intentoUsado[i]) {
                continue;
            }

            for (int j = 0; j < 4; j++) {
                if (!solucionUsada[j] && intento[i] == solucionNums[j]) {
                    estados[i] = "PARTIAL";
                    solucionUsada[j] = true;
                    intentoUsado[i] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (estados[i] == null) {
                estados[i] = "WRONG";
            }
        }

        return estados;
    }

    private void colorearFila(int fila, String[] estados) {
        for (int col = 0; col < 4; col++) {
            String color = switch (estados[col]) {
                case "CORRECT" -> COLOR_CORRECT;
                case "PARTIAL" -> COLOR_PARTIAL;
                default -> COLOR_WRONG;
            };

            fields[fila][col].setStyle(CELL_STYLE_BASE +
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
        for (int i = 0; i < dots.length; i++) {
            dots[i].setStroke(Color.web(COLOR_DEFAULT_BORDER));
            dots[i].setStrokeWidth(1.5);

            if (i < intentos.size()) {
                dots[i].setFill(Color.web(COLOR_BLUE));
            } else if (!partidaFinalizada && i == filaActual) {
                dots[i].setFill(Color.web(COLOR_PARTIAL));
            } else {
                dots[i].setFill(Color.web(COLOR_DEFAULT_BG));
            }
        }
    }

    private void finalizarPartida(boolean victoria) {
        juegoTerminado = true;

        if (partidaActual != null) {
            partidaActual.setVictoria(victoria);
            partidaActual.setIntentosUsados(intentos.size());
            partidaActual.setFecha(LocalDateTime.now());

            if (usuarioActual != null) {
                try {
                    PartidaDAO partidaDAO = new PartidaDAO();
                    partidaDAO.guardarPartida(partidaActual);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error", "No se pudo guardar la partida.");
                }
            }
        }

        for (int i = 0; i < dots.length; i++) {
            if (i < intentos.size()) {
                dots[i].setFill(Color.web(victoria ? COLOR_CORRECT : COLOR_WRONG));
            } else {
                dots[i].setFill(Color.web(COLOR_DEFAULT_BG));
            }
            dots[i].setStroke(Color.web(victoria ? COLOR_CORRECT : COLOR_WRONG));
        }

        actualizarDots(true);
        mostrarMensajeFinal(victoria);
    }

    private void mostrarMensajeFinal(boolean victoria) {
        String titulo = victoria ? "Ganaste" : "Fin del juego";
        String mensaje = victoria
                ? "Encontraste la ecuación correcta."
                : "La ecuación era: " + solucionNums[0] + " " + simboloDisplay(solucionOps[0])
                + " " + solucionNums[1] + " " + simboloDisplay(solucionOps[1])
                + " " + solucionNums[2] + " " + simboloDisplay(solucionOps[2])
                + " " + solucionNums[3] + " = " + resultadoEsperado;

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
        int[] nums = { a, b, c, d };
        String[] opArr = { op1, op2, op3 };

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("/")) {
                if (nums[i + 1] == 0 || nums[i] % nums[i + 1] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int calcularResultado(int a, String op1, int b, String op2, int c, String op3, int d) {
        int[] nums = { a, b, c, d };
        String[] opArr = { op1, op2, op3 };

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("*") || opArr[i].equals("/")) {
                int val = opArr[i].equals("*") ? nums[i] * nums[i + 1] : nums[i] / nums[i + 1];
                nums[i] = val;

                for (int j = i + 1; j < nums.length - 1; j++) {
                    nums[j] = nums[j + 1];
                    opArr[j - 1] = opArr[j];
                }

                opArr = Arrays.copyOf(opArr, opArr.length - 1);
                nums = Arrays.copyOf(nums, nums.length - 1);
                i--;
            }
        }

        int resultado = nums[0];

        for (int i = 0; i < opArr.length; i++) {
            if (opArr[i].equals("+")) {
                resultado += nums[i + 1];
            } else if (opArr[i].equals("-")) {
                resultado -= nums[i + 1];
            }
        }

        return resultado;
    }

    private void parsearEcuacion(String ecuacion) {
        String[] partes = ecuacion.split("=");
        resultadoEsperado = Integer.parseInt(partes[1]);

        String expr = partes[0];
        solucionNums = new int[4];
        solucionOps = new String[3];

        int numIdx = 0;
        int opIdx = 0;

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (Character.isDigit(ch)) {
                solucionNums[numIdx++] = ch - '0';
            } else {
                solucionOps[opIdx++] = String.valueOf(ch);
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
        for (String e : estados) {
            if (!e.equals("CORRECT")) {
                return false;
            }
        }

        return true;
    }

    private void setFilaEditable(int fila, boolean editable) {
        for (TextField tf : fields[fila]) {
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
