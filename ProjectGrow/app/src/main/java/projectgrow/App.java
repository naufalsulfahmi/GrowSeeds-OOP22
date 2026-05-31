
package projectgrow;

import database.DatabaseConnection;
import javafx.application.Application;
import view.LoginView;

public class App {

    public static void main(String[] args) {
        DatabaseConnection.inisialisasiDatabase();
        Application.launch(LoginView.class, args);
    }
}
