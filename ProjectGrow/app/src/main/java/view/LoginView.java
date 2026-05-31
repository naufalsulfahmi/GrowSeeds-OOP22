package view;

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

        Label lblSubtitle = new Label("Masuk untuk memulai berkebun digitalmu");
        lblSubtitle.setFont(Font.font("SansSerif", 13));
        lblSubtitle.setTextFill(Color.GRAY);

        VBox headerText = new VBox(5, lblTitle, lblSubtitle);

        VBox usernameGroup = new VBox(8);
        Label lblUsername = new Label("Masukkan Email");
        lblUsername.setFont(Font.font("SansSerif", 13));
        lblUsername.setTextFill(Color.GRAY);
        
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("petani@growseeds.id");
        txtUsername.setPrefHeight(40);
        txtUsername.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 0 10 0 10;");
        usernameGroup.getChildren().addAll(lblUsername, txtUsername);

        VBox passwordGroup = new VBox(8);
        Label lblPassword = new Label("Password");
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


        Button btnMasuk = new Button("Masuk");
        btnMasuk.setMaxWidth(Double.MAX_VALUE);
        btnMasuk.setPrefHeight(42);
        btnMasuk.setStyle("-fx-background-color: #294a20; -fx-border-radius: 5; " +
                          "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        btnMasuk.setCursor(Cursor.HAND);


        HBox footerRegister = new HBox(5);
        footerRegister.setAlignment(Pos.CENTER);
        Label lblBelumPunya = new Label("Belum punya akun?");
        lblBelumPunya.setFont(Font.font("SansSerif", 12));
        lblBelumPunya.setTextFill(Color.BLACK);

        Label lblDaftar = new Label("Daftar sekarang");
        lblDaftar.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        lblDaftar.setTextFill(Color.web("#294a20"));
        lblDaftar.setCursor(Cursor.HAND);

        lblDaftar.setOnMouseClicked(e -> {
            RegistrasiView registerView = new RegistrasiView();
            Stage registerStage = new Stage();
            try{
                registerView.start(registerStage);
                primaryStage.close(); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        footerRegister.getChildren().addAll(lblBelumPunya, lblDaftar);
        loginCard.getChildren().addAll(headerText, usernameGroup, passwordGroup, btnMasuk, footerRegister);
        root.getChildren().add(loginCard);

        btnMasuk.setOnAction(e -> {
            String email = txtUsername.getText().trim();
            String password = txtPassword.getText();

            Login login = new Login();
            login.setEmail(email);
            login.setPassword(password);

            LoginService service = new LoginService();

            if(service.cekLogin(login)) {
                DashboardView dashboard = new DashboardView();
                Stage dashboardStage = new Stage();
                try {
                    dashboard.start(dashboardStage);
                    primaryStage.close(); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Email atau Password salah / belum terdaftar!");
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}