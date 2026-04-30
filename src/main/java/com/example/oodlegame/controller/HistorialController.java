package com.example.oodlegame.controller;

import com.example.oodlegame.model.Partida;
import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.service.PartidaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialController {

    // ── Stats ────────────────────────────────────────────────
    @FXML private Label lblTotalPartidas;
    @FXML private Label lblVictorias;
    @FXML private Label lblDerrotas;
    @FXML private Label lblPromedioIntentos;

    // ── Tabla ────────────────────────────────────────────────
    @FXML private TableView<Partida>              tablaHistorial;
    @FXML private TableColumn<Partida, LocalDateTime> colFecha;
    @FXML private TableColumn<Partida, String>    colEcuacion;
    @FXML private TableColumn<Partida, Boolean>   colResultado;
    @FXML private TableColumn<Partida, Integer>   colIntentos;

    // ── Botones ──────────────────────────────────────────────
    @FXML private Button backButton;
    @FXML private Button btnLimpiar;

    // ── Estado ───────────────────────────────────────────────
    private Usuario usuarioActual;

    // ════════════════════════════════════════════════════════
    //  Llamado desde el controller anterior al cambiar escena:
    //    historialController.setUsuario(usuarioActual);
    // ════════════════════════════════════════════════════════
    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarHistorial();
    }

    // ════════════════════════════════════════════════════════
    //  INITIALIZE
    // ════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        configurarColumnas();
        aplicarEstiloFilas();
    }

    // ════════════════════════════════════════════════════════
    //  CONFIGURAR COLUMNAS
    // ════════════════════════════════════════════════════════
    private void configurarColumnas() {

        // ── Fecha ──
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");
            @Override
            protected void updateItem(LocalDateTime fecha, boolean empty) {
                super.updateItem(fecha, empty);
                if (empty || fecha == null) { setText(null); return; }
                setText(fmt.format(fecha));
                setStyle("-fx-text-fill: #8BA3B8; -fx-font-size: 12px; -fx-padding: 0 0 0 10;");
            }
        });

        // ── Ecuación ──
        colEcuacion.setCellValueFactory(new PropertyValueFactory<>("ecuacionObjetivo"));
        colEcuacion.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String ecuacion, boolean empty) {
                super.updateItem(ecuacion, empty);
                if (empty || ecuacion == null) { setText(null); return; }
                setText(ecuacion);
                setAlignment(Pos.CENTER);
                setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; " +
                        "-fx-font-weight: bold; -fx-alignment: CENTER;");
            }
        });

        // ── Resultado (ganó/perdió) — usa tu campo "victoria" ──
        colResultado.setCellValueFactory(new PropertyValueFactory<>("victoria"));
        colResultado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean victoria, boolean empty) {
                super.updateItem(victoria, empty);
                if (empty || victoria == null) { setText(null); setGraphic(null); return; }

                Label badge = new Label(victoria ? "✓  GANÓ" : "✗  PERDIÓ");
                badge.setStyle(
                        "-fx-background-color: " + (victoria ? "#1A3D2B" : "#3D1A1A") + "; " +
                                "-fx-text-fill: "         + (victoria ? "#6AAA64" : "#C05050") + "; " +
                                "-fx-font-size: 11px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 20; -fx-padding: 4 12 4 12;"
                );
                HBox box = new HBox(badge);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
                setText(null);
            }
        });

        // ── Intentos — barra visual de puntos ──
        colIntentos.setCellValueFactory(new PropertyValueFactory<>("intentosUsados"));
        colIntentos.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer intentos, boolean empty) {
                super.updateItem(intentos, empty);
                if (empty || intentos == null) { setText(null); setGraphic(null); return; }

                HBox puntos = new HBox(4);
                puntos.setAlignment(Pos.CENTER);
                for (int i = 0; i < 6; i++) {
                    Region punto = new Region();
                    punto.setPrefSize(11, 11);
                    punto.setStyle("-fx-background-radius: 3; -fx-background-color: " +
                            (i < intentos ? "#3A5BD9" : "#1E2D40") + ";");
                    puntos.getChildren().add(punto);
                }
                Label num = new Label(" " + intentos + "/6");
                num.setStyle("-fx-text-fill: #8BA3B8; -fx-font-size: 11px;");

                HBox box = new HBox(6, puntos, num);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
                setText(null);
            }
        });
    }

    // ════════════════════════════════════════════════════════
    //  ESTILO DE FILAS (alternas + hover)
    // ════════════════════════════════════════════════════════
    private void aplicarEstiloFilas() {
        tablaHistorial.setFixedCellSize(52);
        tablaHistorial.setRowFactory(tv -> {
            TableRow<Partida> row = new TableRow<>();

            // Color alterno según índice
            row.indexProperty().addListener((obs, oldIdx, newIdx) -> {
                if (row.isEmpty()) return;
                actualizarColorFila(row);
            });
            row.itemProperty().addListener((obs, o, n) -> actualizarColorFila(row));

            // Hover
            row.setOnMouseEntered(e ->
                    row.setStyle("-fx-background-color: #223050; -fx-border-width: 0;"));
            row.setOnMouseExited(e -> actualizarColorFila(row));

            return row;
        });
    }

    private void actualizarColorFila(TableRow<Partida> row) {
        if (row.isEmpty()) {
            row.setStyle("-fx-background-color: transparent;");
            return;
        }
        String bg = (row.getIndex() % 2 == 0) ? "#1A2535" : "#151E2D";
        row.setStyle("-fx-background-color: " + bg + "; -fx-border-width: 0;");
    }

    // ════════════════════════════════════════════════════════
    //  CARGAR HISTORIAL DESDE DAO
    // ════════════════════════════════════════════════════════
    private void cargarHistorial() {
        if (usuarioActual == null) return;

        PartidaDAO partidaDAO = new PartidaDAO();
        List<Partida> partidas = partidaDAO.obtenerPartidasUsuario(usuarioActual.getId());
        ObservableList<Partida> datos = FXCollections.observableArrayList(partidas);

        tablaHistorial.setItems(datos);
        actualizarStats(datos);
    }

    // ════════════════════════════════════════════════════════
    //  ACTUALIZAR TARJETAS DE ESTADÍSTICAS
    // ════════════════════════════════════════════════════════
    private void actualizarStats(ObservableList<Partida> datos) {
        int total      = datos.size();
        long victorias = datos.stream().filter(Partida::isVictoria).count();
        long derrotas  = total - victorias;
        double promedio = datos.stream()
                .mapToInt(Partida::getIntentosUsados)
                .average()
                .orElse(0);

        lblTotalPartidas.setText(String.valueOf(total));
        lblVictorias.setText(String.valueOf(victorias));
        lblDerrotas.setText(String.valueOf(derrotas));
        lblPromedioIntentos.setText(total > 0 ? String.format("%.1f", promedio) : "—");
    }

    // ════════════════════════════════════════════════════════
    //  HANDLERS
    // ════════════════════════════════════════════════════════
    @FXML
    private void onVolverClicked() {
        // TODO: navegar a Menu.fxml
    }

    @FXML
    private void onLimpiarHistorial() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Limpiar historial");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que quieres borrar todo el historial?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                tablaHistorial.getItems().clear();
                lblTotalPartidas.setText("0");
                lblVictorias.setText("0");
                lblDerrotas.setText("0");
                lblPromedioIntentos.setText("—");
                // TODO: partidaDAO.limpiarHistorialUsuario(usuarioActual.getId());
            }
        });
    }
}