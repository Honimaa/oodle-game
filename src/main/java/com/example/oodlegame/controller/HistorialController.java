package com.example.oodlegame.controller;

import com.example.oodlegame.model.Partida;
import com.example.oodlegame.model.Usuario;
import com.example.oodlegame.service.PartidaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialController {

    @FXML private Label lblTotalPartidas;
    @FXML private Label lblVictorias;
    @FXML private Label lblDerrotas;
    @FXML private Label lblPromedioIntentos;

    @FXML private TableView<Partida> tablaHistorial;
    @FXML private TableColumn<Partida, LocalDateTime> colFecha;
    @FXML private TableColumn<Partida, String> colEcuacion;
    @FXML private TableColumn<Partida, Boolean> colResultado;
    @FXML private TableColumn<Partida, Integer> colIntentos;

    @FXML private Button backButton;
    @FXML private Button btnLimpiar;

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarHistorial();
    }

    @FXML
    public void initialize() {
        configurarTabla();
        configurarColumnas();
        aplicarEstiloFilas();
    }

    private void configurarTabla() {
        tablaHistorial.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaHistorial.setFixedCellSize(52);

        tablaHistorial.setStyle(
                "-fx-background-color: #0F1923;" +
                        "-fx-control-inner-background: #1A2535;" +
                        "-fx-table-cell-border-color: #22364A;" +
                        "-fx-selection-bar: #223050;" +
                        "-fx-selection-bar-non-focused: #223050;" +
                        "-fx-text-background-color: #FFFFFF;"
        );

        Label placeholder = new Label("Aún no tienes partidas registradas.");
        placeholder.setStyle("-fx-text-fill: #8BA3B8; -fx-font-size: 13px;");
        tablaHistorial.setPlaceholder(placeholder);
    }

    private void configurarColumnas() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(col -> new TableCell<Partida, LocalDateTime>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

            @Override
            protected void updateItem(LocalDateTime fecha, boolean empty) {
                super.updateItem(fecha, empty);

                setGraphic(null);
                setAlignment(Pos.CENTER);
                setStyle(estiloCeldaTexto());

                if (empty || fecha == null) {
                    setText(null);
                } else {
                    setText(fmt.format(fecha));
                }
            }
        });

        colEcuacion.setCellValueFactory(new PropertyValueFactory<>("ecuacionObjetivo"));
        colEcuacion.setCellFactory(col -> new TableCell<Partida, String>() {
            @Override
            protected void updateItem(String ecuacion, boolean empty) {
                super.updateItem(ecuacion, empty);

                setGraphic(null);
                setAlignment(Pos.CENTER);
                setStyle(estiloCeldaTexto());

                if (empty || ecuacion == null) {
                    setText(null);
                } else {
                    setText(ecuacion);
                }
            }
        });

        colResultado.setCellValueFactory(new PropertyValueFactory<>("victoria"));
        colResultado.setCellFactory(col -> new TableCell<Partida, Boolean>() {
            @Override
            protected void updateItem(Boolean victoria, boolean empty) {
                super.updateItem(victoria, empty);

                setText(null);
                setStyle("-fx-background-color: transparent; -fx-alignment: CENTER;");

                if (empty || victoria == null) {
                    setGraphic(null);
                    return;
                }

                Label badge = new Label(victoria ? "✓  GANÓ" : "✗  PERDIÓ");
                badge.setStyle(
                        "-fx-background-color: " + (victoria ? "#1A3D2B" : "#3D1A1A") + "; " +
                                "-fx-text-fill: " + (victoria ? "#6AAA64" : "#C05050") + "; " +
                                "-fx-font-size: 11px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 4 12 4 12;"
                );

                HBox box = new HBox(badge);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
        });

        colIntentos.setCellValueFactory(new PropertyValueFactory<>("intentosUsados"));
        colIntentos.setCellFactory(col -> new TableCell<Partida, Integer>() {
            @Override
            protected void updateItem(Integer intentos, boolean empty) {
                super.updateItem(intentos, empty);

                setText(null);
                setStyle("-fx-background-color: transparent; -fx-alignment: CENTER;");

                if (empty || intentos == null) {
                    setGraphic(null);
                    return;
                }

                HBox puntos = new HBox(4);
                puntos.setAlignment(Pos.CENTER);

                for (int i = 0; i < 6; i++) {
                    Region punto = new Region();
                    punto.setPrefSize(11, 11);
                    punto.setStyle(
                            "-fx-background-radius: 3; " +
                                    "-fx-background-color: " + (i < intentos ? "#3A5BD9" : "#1E2D40") + ";"
                    );
                    puntos.getChildren().add(punto);
                }

                Label num = new Label(intentos + "/6");
                num.setStyle("-fx-text-fill: #8BA3B8; -fx-font-size: 11px;");

                HBox box = new HBox(8, puntos, num);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
        });
    }

    private String estiloCeldaTexto() {
        return "-fx-text-fill: #FFFFFF; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-color: transparent; " +
                "-fx-alignment: CENTER;";
    }

    private void aplicarEstiloFilas() {
        tablaHistorial.setRowFactory(tv -> new TableRow<Partida>() {
            @Override
            protected void updateItem(Partida partida, boolean empty) {
                super.updateItem(partida, empty);

                if (empty || partida == null) {
                    setStyle("-fx-background-color: #0F1923;");
                    return;
                }

                String bg = getIndex() % 2 == 0 ? "#1A2535" : "#151E2D";
                setStyle("-fx-background-color: " + bg + "; -fx-border-width: 0;");
            }
        });
    }

    private void cargarHistorial() {
        if (usuarioActual == null) {
            return;
        }

        PartidaDAO partidaDAO = new PartidaDAO();
        List<Partida> partidas = partidaDAO.obtenerPartidasUsuario(usuarioActual.getId());
        ObservableList<Partida> datos = FXCollections.observableArrayList(partidas);

        tablaHistorial.setItems(datos);
        tablaHistorial.refresh();
        actualizarStats(datos);
    }

    private void actualizarStats(ObservableList<Partida> datos) {
        int total = datos.size();
        long victorias = datos.stream().filter(Partida::isVictoria).count();
        long derrotas = total - victorias;
        double promedio = datos.stream()
                .mapToInt(Partida::getIntentosUsados)
                .average()
                .orElse(0);

        lblTotalPartidas.setText(String.valueOf(total));
        lblVictorias.setText(String.valueOf(victorias));
        lblDerrotas.setText(String.valueOf(derrotas));
        lblPromedioIntentos.setText(total > 0 ? String.format("%.1f", promedio) : "—");
    }

    @FXML
    private void onVolverClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oodlegame/Menu.fxml"));
            Parent root = loader.load();

            MenuController menuController = loader.getController();
            menuController.setUsuario(usuarioActual);

            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver al menú.");
        }
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
                actualizarStats(tablaHistorial.getItems());
                // TODO: partidaDAO.limpiarHistorialUsuario(usuarioActual.getId());
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
