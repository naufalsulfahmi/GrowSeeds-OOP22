package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Login;
import service.LoginService;

import java.util.Objects;

public class RegistrasiView extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GrowSeeds - Daftar Akun Baru");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #eef4ee;");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        VBox registerCard = new VBox(20);
        registerCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        registerCard.setPadding(new Insets(40, 45, 40, 45));
        registerCard.setMaxWidth(450);
        registerCard.setPrefWidth(450);

        Label lblTitle = new Label("Buat Akun");
        lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.web("#323232"));

        Label lblSubtitle = new Label("Setiap akun memiliki penyimpanan data pertanian terpisah");
        lblSubtitle.setFont(Font.font("SansSerif", 13));
        lblSubtitle.setTextFill(Color.GRAY);

        VBox headerText = new VBox(5, lblTitle, lblSubtitle);

        VBox emailGroup = new VBox(8);
        Label lblEmail = new Label("Alamat Email");
        lblEmail.setFont(Font.font("SansSerif", 13));
        lblEmail.setTextFill(Color.GRAY);
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("petani@growseeds.id");
        txtEmail.setPrefHeight(40);
        txtEmail.setStyle(fieldStyle(10));
        emailGroup.getChildren().addAll(lblEmail, txtEmail);

        VBox passwordGroup = new VBox(8);
        Label lblPassword = new Label("Password (minimal 8 karakter)");
        lblPassword.setFont(Font.font("SansSerif", 13));
        lblPassword.setTextFill(Color.GRAY);
        PasswordField txtPassword = new PasswordField();
        TextField txtPasswordVisible = new TextField();
        StackPane passwordContainer = createPasswordContainer(txtPassword, txtPasswordVisible);
        passwordGroup.getChildren().addAll(lblPassword, passwordContainer);

        Button btnDaftar = new Button("Daftar Sekarang");
        btnDaftar.setMaxWidth(Double.MAX_VALUE);
        btnDaftar.setPrefHeight(42);
        btnDaftar.setStyle("-fx-background-color: #3C6630; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        btnDaftar.setCursor(Cursor.HAND);

        HBox footerLogin = new HBox(5);
        footerLogin.setAlignment(Pos.CENTER);
        Label lblSudahPunya = new Label("Sudah punya akun?");
        Label lblMasuk = new Label("Masuk di sini");
        lblMasuk.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        lblMasuk.setTextFill(Color.web("#3C6630"));
        lblMasuk.setCursor(Cursor.HAND);
        lblMasuk.setOnMouseClicked(e -> kembaliKeLogin(primaryStage));
        footerLogin.getChildren().addAll(lblSudahPunya, lblMasuk);

        registerCard.getChildren().addAll(headerText, emailGroup, passwordGroup, btnDaftar, footerLogin);
        root.getChildren().add(registerCard);

        btnDaftar.setOnAction(e -> {
            Login login = new Login(txtEmail.getText().trim(), txtPassword.getText());
            LoginService service = new LoginService();
            String hasilValidasi = service.validasiLogin(login);

            if (!"OK".equals(hasilValidasi)) {
                tampilkanAlert(Alert.AlertType.WARNING, "Data Belum Valid", hasilValidasi, "Perbaiki Data");
                return;
            }

            LoginService.RegistrationStatus status = service.registrasi(login);
            switch (status) {
                case SUCCESS -> {
                    tampilkanAlert(Alert.AlertType.INFORMATION, "Akun Berhasil Dibuat",
                            "Akun berhasil disimpan. Silakan masuk menggunakan email dan password tersebut.",
                            "Lanjut ke Login");
                    kembaliKeLogin(primaryStage);
                }
                case EMAIL_ALREADY_USED -> {
                    tampilkanAlert(Alert.AlertType.WARNING, "Email Sudah Terdaftar",
                            "Email tersebut sudah digunakan oleh akun lain. Masukkan alamat email yang berbeda.",
                            "Gunakan Email Lain");
                    txtEmail.requestFocus();
                    txtEmail.selectAll();
                }
                case DATABASE_ERROR -> tampilkanAlert(Alert.AlertType.ERROR, "Pendaftaran Gagal",
                        "Akun tidak dapat disimpan karena terjadi kesalahan pada database.", "Tutup");
            }
        });

        txtPassword.setOnAction(e -> btnDaftar.fire());
        txtPasswordVisible.setOnAction(e -> btnDaftar.fire());

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createPasswordContainer(PasswordField passwordField, TextField visibleField) {
        passwordField.setPromptText("Masukkan password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle(fieldStyle(40));

        visibleField.setPromptText("Masukkan password");
        visibleField.setPrefHeight(40);
        visibleField.setStyle(fieldStyle(40));
        visibleField.setVisible(false);
        visibleField.setManaged(false);
        passwordField.textProperty().bindBidirectional(visibleField.textProperty());

        Image eyeOpen = loadImage("/imgEyeOpen.jpg");
        Image eyeClose = loadImage("/imgEyeClose.jpg");
        ImageView icon = new ImageView(eyeOpen);
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);

        Button toggle = new Button();
        toggle.setGraphic(icon);
        toggle.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        toggle.setCursor(Cursor.HAND);
        StackPane.setAlignment(toggle, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggle, new Insets(0, 12, 0, 0));
        toggle.setOnAction(e -> {
            boolean tampilkan = passwordField.isVisible();
            passwordField.setVisible(!tampilkan);
            passwordField.setManaged(!tampilkan);
            visibleField.setVisible(tampilkan);
            visibleField.setManaged(tampilkan);
            icon.setImage(tampilkan ? eyeClose : eyeOpen);
        });

        return new StackPane(passwordField, visibleField, toggle);
    }

    private Image loadImage(String resource) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(resource)));
    }

    private String fieldStyle(int rightPadding) {
        return "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; " +
                "-fx-padding: 0 " + rightPadding + " 0 10;";
    }

    private void kembaliKeLogin(Stage currentStage) {
        try {
            new LoginView().start(new Stage());
            currentStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String message, String buttonLabel) {
        ButtonType button = new ButtonType(buttonLabel, ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(type, message, button);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
