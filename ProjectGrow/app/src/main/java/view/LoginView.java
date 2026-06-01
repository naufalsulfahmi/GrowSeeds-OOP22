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

public class LoginView extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GrowSeeds - Masuk");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #eef4ee;");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        VBox loginCard = new VBox(20);
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        loginCard.setPadding(new Insets(40, 45, 40, 45));
        loginCard.setMaxWidth(450);
        loginCard.setPrefWidth(450);

        Label lblTitle = new Label("Selamat datang!");
        lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        lblTitle.setTextFill(Color.web("#323232"));

        Label lblSubtitle = new Label("Masuk untuk mengelola data pertanian akun Anda");
        lblSubtitle.setFont(Font.font("SansSerif", 13));
        lblSubtitle.setTextFill(Color.GRAY);

        VBox headerText = new VBox(5, lblTitle, lblSubtitle);

        VBox usernameGroup = new VBox(8);
        Label lblUsername = new Label("Alamat Email");
        lblUsername.setFont(Font.font("SansSerif", 13));
        lblUsername.setTextFill(Color.GRAY);

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("petani@growseeds.id");
        txtUsername.setPrefHeight(40);
        txtUsername.setStyle(fieldStyle(10));
        usernameGroup.getChildren().addAll(lblUsername, txtUsername);

        VBox passwordGroup = new VBox(8);
        Label lblPassword = new Label("Password");
        lblPassword.setFont(Font.font("SansSerif", 13));
        lblPassword.setTextFill(Color.GRAY);

        PasswordField txtPassword = new PasswordField();
        TextField txtPasswordVisible = new TextField();
        StackPane passwordContainer = createPasswordContainer(txtPassword, txtPasswordVisible);
        passwordGroup.getChildren().addAll(lblPassword, passwordContainer);

        Button btnMasuk = new Button("Masuk");
        btnMasuk.setMaxWidth(Double.MAX_VALUE);
        btnMasuk.setPrefHeight(42);
        btnMasuk.setStyle("-fx-background-color: #3C6630; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        btnMasuk.setCursor(Cursor.HAND);

        HBox footerRegister = new HBox(5);
        footerRegister.setAlignment(Pos.CENTER);
        Label lblBelumPunya = new Label("Belum punya akun?");
        lblBelumPunya.setFont(Font.font("SansSerif", 12));

        Label lblDaftar = new Label("Daftar sekarang");
        lblDaftar.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        lblDaftar.setTextFill(Color.web("#3C6630"));
        lblDaftar.setCursor(Cursor.HAND);
        lblDaftar.setOnMouseClicked(e -> bukaRegistrasi(primaryStage));

        footerRegister.getChildren().addAll(lblBelumPunya, lblDaftar);
        loginCard.getChildren().addAll(headerText, usernameGroup, passwordGroup, btnMasuk, footerRegister);
        root.getChildren().add(loginCard);

        btnMasuk.setOnAction(e -> {
            Login login = new Login(txtUsername.getText().trim(), txtPassword.getText());
            LoginService service = new LoginService();

            if (login.getEmail().isBlank() || login.getPassword().isBlank()) {
                tampilkanAlert(Alert.AlertType.WARNING, "Data Belum Lengkap",
                        "Email dan password wajib diisi.", "Lengkapi Data");
                return;
            }

            if (service.cekLogin(login)) {
                DashboardView dashboard = new DashboardView();
                Stage dashboardStage = new Stage();
                try {
                    dashboard.start(dashboardStage);
                    primaryStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Login Gagal",
                        "Email atau password tidak sesuai. Periksa kembali data akun Anda.", "Coba Lagi");
                txtPassword.clear();
                txtPassword.requestFocus();
            }
        });

        txtPassword.setOnAction(e -> btnMasuk.fire());
        txtPasswordVisible.setOnAction(e -> btnMasuk.fire());

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

    private void bukaRegistrasi(Stage currentStage) {
        try {
            new RegistrasiView().start(new Stage());
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
