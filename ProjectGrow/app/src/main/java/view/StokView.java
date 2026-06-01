package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Stok;
import service.StokService;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;


public class StokView extends VBox {
    private static final String GREEN_DARK = "#294a20";
    private static final String BACKGROUND = "#f5f7f4";
    private static final String RED_BUTTON = "#a83232";
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");

    private final StokService stokService;
    private final Runnable onStockChanged;
    private final TextField txtCariKomoditas = new TextField();
    private final TableView<Stok> stokTable = new TableView<>();
    private final StackPane tableCard = new StackPane();
    private final Label emptyStateLabel = new Label("Data tidak ditemukan atau gudang masih kosong.");

    public StokView() {
        this(new StokService(), () -> { });
    }

    public StokView(Runnable onStockChanged) {
        this(new StokService(), onStockChanged);
    }

    public StokView(StokService stokService) {
        this(stokService, () -> { });
    }

    public StokView(StokService stokService, Runnable onStockChanged) {
        this.stokService = Objects.requireNonNull(stokService, "stokService tidak boleh null");
        this.onStockChanged = Objects.requireNonNull(onStockChanged, "onStockChanged tidak boleh null");
        buildView();
        refreshData();
    }

    private void buildView() {
        setSpacing(0);
        setBackground(new Background(new BackgroundFill(Color.web(BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(20, 30, 20, 30));


        BorderPane headerPanel = new BorderPane();
        headerPanel.setPadding(new Insets(0, 0, 15, 0));

        VBox titleBlock = new VBox(5);
        Label title = new Label("Pelacakan Stok");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Pantau ketersediaan hasil panenmu di sini.");
        subtitle.setFont(Font.font("SansSerif", 13));
        subtitle.setTextFill(Color.GRAY);
        titleBlock.getChildren().addAll(title, subtitle);
        headerPanel.setLeft(titleBlock);

        HBox actionButtonPanel = new HBox(10);
        actionButtonPanel.setAlignment(Pos.CENTER_RIGHT);

        Button btnJual = createActionButton("$ Jual Stok", RED_BUTTON);
        Button btnTambah = createActionButton("+ Tambah Stok Baru", GREEN_DARK);
        btnJual.setOnAction(event -> openStockDialog(false));
        btnTambah.setOnAction(event -> openStockDialog(true));

        actionButtonPanel.getChildren().addAll(btnJual, btnTambah);
        headerPanel.setRight(actionButtonPanel);


        BorderPane searchBarPanel = new BorderPane();
        searchBarPanel.setPadding(new Insets(15, 0, 15, 0));

        Label lblCari = new Label("Cari Komoditas: ");
        lblCari.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        lblCari.setTextFill(Color.web(GREEN_DARK));
        lblCari.setPadding(new Insets(0, 10, 0, 0));

        txtCariKomoditas.setFont(Font.font("SansSerif", 14));
        txtCariKomoditas.setPromptText("Masukkan nama komoditas atau kategori...");
        txtCariKomoditas.setStyle(
                "-fx-border-color: #c8d7c3;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 8 12 8 12;" +
                "-fx-background-color: transparent;"
        );
        txtCariKomoditas.textProperty().addListener((observable, oldValue, newValue) -> refreshData());

        searchBarPanel.setLeft(lblCari);
        searchBarPanel.setCenter(txtCariKomoditas);
        BorderPane.setAlignment(lblCari, Pos.CENTER_LEFT);

        configureTable();

        emptyStateLabel.setFont(Font.font("SansSerif", 14));
        emptyStateLabel.setTextFill(Color.LIGHTGRAY);
        emptyStateLabel.setStyle("-fx-font-style: italic;");

        tableCard.setStyle("-fx-background-color: WHITE; -fx-background-radius: 20;");
        tableCard.setPadding(new Insets(20));
        tableCard.getChildren().addAll(stokTable, emptyStateLabel);

        BorderPane centerContainer = new BorderPane();
        centerContainer.setTop(searchBarPanel);
        centerContainer.setCenter(tableCard);

        getChildren().addAll(headerPanel, centerContainer);
        VBox.setVgrow(centerContainer, Priority.ALWAYS);
    }

    private void configureTable() {
        stokTable.setStyle(
                "-fx-font-family: 'SansSerif'; -fx-font-size: 14px; " +
                "-fx-selection-bar: #e6f2e1; -fx-selection-bar-non-focused: #e6f2e1; " +
                "-fx-cell-size: 40px; -fx-border-color: #f0f0f0;"
        );
        stokTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn<Stok, String> colId = new TableColumn<>("ID Stok");

        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().hashCode()))); 

        TableColumn<Stok, String> namaColumn = new TableColumn<>("Nama Komoditas");
        namaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaBarang()));

        TableColumn<Stok, String> kategoriColumn = new TableColumn<>("Kategori");
        kategoriColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategori()));

        TableColumn<Stok, String> jumlahColumn = new TableColumn<>("Sisa Stok (kg)");
        jumlahColumn.setCellValueFactory(data -> new SimpleStringProperty(NUMBER_FORMAT.format(data.getValue().getJumlah())));

        TableColumn<Stok, String> terjualColumn = new TableColumn<>("Terjual (kg)");
        terjualColumn.setCellValueFactory(data -> new SimpleStringProperty(NUMBER_FORMAT.format(data.getValue().getTerjual())));

        TableColumn<Stok, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().hitungStatusStok()));

        colId.setStyle("-fx-alignment: CENTER;");
        kategoriColumn.setStyle("-fx-alignment: CENTER;");
        jumlahColumn.setStyle("-fx-alignment: CENTER;");
        terjualColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setStyle("-fx-alignment: CENTER;");

        stokTable.getColumns().addAll(colId, namaColumn, kategoriColumn, jumlahColumn, terjualColumn, statusColumn);
    }

    public void refreshData() {
        try {
            String keyword = txtCariKomoditas.getText() == null ? "" : txtCariKomoditas.getText().trim();
            List<Stok> daftarStok = stokService.CaridanListStok(keyword);
            stokTable.setItems(FXCollections.observableArrayList(daftarStok));
            showEmptyState(daftarStok.isEmpty());
        } catch (IllegalStateException ex) {
            stokTable.getItems().clear();
            emptyStateLabel.setText("Silakan login terlebih dahulu untuk melihat data stok.");
            showEmptyState(true);
        }
    }

    private void showEmptyState(boolean empty) {
        tableCard.getChildren().clear();
        if (empty) {
            tableCard.getChildren().add(emptyStateLabel);
        } else {
            tableCard.getChildren().add(stokTable);
        }
    }

    private void openStockDialog(boolean isTambahStok) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isTambahStok ? "Tambah / Pasokan Stok" : "Transaksi Penjualan Stok");
        dialog.setHeaderText(null);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtNama = new TextField();
        ComboBox<String> cmbKategori = new ComboBox<>(
                FXCollections.observableArrayList("Buah", "Sayur", "Tanaman Pangan")
        );
        cmbKategori.getSelectionModel().selectFirst();
        cmbKategori.setMaxWidth(Double.MAX_VALUE);

        TextField txtJumlah = new TextField();

        if (isTambahStok) {
            grid.add(new Label("Nama Komoditas:"), 0, 0);
            grid.add(txtNama, 1, 0);
            grid.add(new Label("Kategori:"), 0, 1);
            grid.add(cmbKategori, 1, 1);
            grid.add(new Label("Jumlah Tambahan (kg):"), 0, 2);
            grid.add(txtJumlah, 1, 2);
        } else {
            grid.add(new Label("Nama Barang yang Dijual:"), 0, 0);
            grid.add(txtNama, 1, 0);
            grid.add(new Label("Kategori:"), 0, 1);
            grid.add(cmbKategori, 1, 1);
            grid.add(new Label("Jumlah Terjual (kg):"), 0, 2);
            grid.add(txtJumlah, 1, 2);
        }

        GridPane.setHgrow(txtNama, Priority.ALWAYS);
        GridPane.setHgrow(cmbKategori, Priority.ALWAYS);
        GridPane.setHgrow(txtJumlah, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setPrefWidth(440);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            Double jumlah = parseJumlah(txtJumlah.getText());
            String nama = txtNama.getText().trim();

            if (nama.isEmpty()) {
                event.consume();
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Nama komoditas wajib diisi!");
                return;
            }

            if (jumlah == null) {
                event.consume();
                showAlert(Alert.AlertType.ERROR, "Input Salah", "Jumlah stok harus berupa angka lebih besar dari nol!");
                return;
            }

            boolean berhasil;
            try {
                berhasil = isTambahStok
                        ? stokService.tambahStok(nama, cmbKategori.getValue(), jumlah)
                        : stokService.jualStok(nama, cmbKategori.getValue(), jumlah);
            } catch (IllegalStateException ex) {
                event.consume();
                showAlert(Alert.AlertType.WARNING, "Login Diperlukan", "Silakan login terlebih dahulu sebelum mengubah data stok.");
                return;
            }

            if (!berhasil) {
                event.consume();
                if (isTambahStok) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data ke database.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Transaksi gagal! Periksa kembali nama komoditas atau sisa stok kamu.");
                }
                return;
            }

            refreshData();
            onStockChanged.run();
            showAlert(
                    Alert.AlertType.INFORMATION, 
                    "Sukses", 
                    isTambahStok ? "Stok berhasil diperbarui!" : "Penjualan berhasil dicatat!"
            );
        });

        dialog.showAndWait();
    }

    private Double parseJumlah(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            double jumlah = Double.parseDouble(text.trim().replace(',', '.'));
            return jumlah > 0 ? jumlah : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Button createActionButton(String text, String backgroundColor) {
        Button button = new Button(text);
        button.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        button.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 15 10 15;"
        );
        return button;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int getJumlahStokRendah() {
        try {
            return stokService.hitungStokRendah();
        } catch (IllegalStateException ex) {
            return 0;
        }
    }

    public String formatJumlah(double jumlah) {
        return NUMBER_FORMAT.format(jumlah);
    }
}