package views;

import service.LoginService;
import model.Login;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class RegistasiApp extends Application {

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

        Label lblSubtitle = new Label("Daftarkan diri untuk mulai mengelola pertanian digital");
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
        txtEmail.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 0 10 0 10;");
        emailGroup.getChildren().addAll(lblEmail, txtEmail);


        VBox passwordGroup = new VBox(8);
        Label lblPassword = new Label("Password (Minimal 8 karakter)");
        lblPassword.setFont(Font.font("SansSerif", 13));
        lblPassword.setTextFill(Color.GRAY);
        
        StackPane passwordContainer = new StackPane();

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan password");
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 0 40 0 10;");

        TextField txtPasswordVisible = new TextField();
        txtPasswordVisible.setPromptText("Masukkan password");
        txtPasswordVisible.setPrefHeight(40);
        txtPasswordVisible.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 0 40 0 10;");
        txtPasswordVisible.setVisible(false);
        txtPasswordVisible.setManaged(false);

        txtPassword.textProperty().bindBidirectional(txtPasswordVisible.textProperty());

        Image imgEyeOpen = new Image("file:src/main/resources/imgEyeOpen.jpg");
        Image imgEyeClose = new Image("file:src/main/resources/imgEyeClose.jpg");

        ImageView ivMata = new ImageView(imgEyeOpen);
        ivMata.setFitWidth(20);
        ivMata.setFitHeight(20);
        ivMata.setPreserveRatio(true);

        Button btnTogglePassword = new Button();
        btnTogglePassword.setGraphic(ivMata);
        btnTogglePassword.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        btnTogglePassword.setCursor(Cursor.HAND);
        
        StackPane.setAlignment(btnTogglePassword, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnTogglePassword, new Insets(0, 12, 0, 0));

        btnTogglePassword.setOnAction(e -> {
            if(txtPassword.isVisible()) {
                txtPassword.setVisible(false);
                txtPassword.setManaged(false);
                txtPasswordVisible.setVisible(true);
                txtPasswordVisible.setManaged(true);
                ivMata.setImage(imgEyeClose);
            }else {
                txtPassword.setVisible(true);
                txtPassword.setManaged(true);
                txtPasswordVisible.setVisible(false);
                txtPasswordVisible.setManaged(false);
                ivMata.setImage(imgEyeOpen);
            }
        });

        passwordContainer.getChildren().addAll(txtPassword, txtPasswordVisible, btnTogglePassword);
        passwordGroup.getChildren().addAll(lblPassword, passwordContainer);

        Button btnDaftar = new Button("Daftar Sekarang");
        btnDaftar.setMaxWidth(Double.MAX_VALUE);
        btnDaftar.setPrefHeight(42);
        btnDaftar.setStyle("-fx-background-color: #294a20; -fx-background-radius: 5; " +
                           "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        btnDaftar.setCursor(Cursor.HAND);

        HBox footerLogin = new HBox(5);
        footerLogin.setAlignment(Pos.CENTER);

        Label lblSudahPunya = new Label("Sudah punya akun?");
        lblSudahPunya.setFont(Font.font("SansSerif", 12));
        lblSudahPunya.setTextFill(Color.BLACK);

        Label lblMasuk = new Label("Masuk di sini");
        lblMasuk.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        lblMasuk.setTextFill(Color.web("#294a20"));
        lblMasuk.setCursor(Cursor.HAND);

        lblMasuk.setOnMouseClicked(e -> kembaliKeLogin(primaryStage));

        footerLogin.getChildren().addAll(lblSudahPunya, lblMasuk);
        registerCard.getChildren().addAll(headerText, emailGroup, passwordGroup, btnDaftar, footerLogin);
        root.getChildren().add(registerCard);

        btnDaftar.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            Login login = new Login();
            login.setEmail(email);
            login.setPassword(password);

            LoginService service = new LoginService();

            String hasilValidasi = service.validasiLogin(login);
            if(!hasilValidasi.equals("OK")) {
                tampilkanAlert(Alert.AlertType.WARNING, "Validasi Gagal", hasilValidasi);
                return;
            }

            if(service.simpanLogin(login)) {
                tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Akun berhasil dibuat! Silakan login.");
                kembaliKeLogin(primaryStage);
            }else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mendaftarkan akun. Email mungkin sudah digunakan.");
            }
        });

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void kembaliKeLogin(Stage currentStage) {
        LoginApp loginApp = new LoginApp();
        Stage loginStage = new Stage();
        try {
            loginApp.start(loginStage);
            currentStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}