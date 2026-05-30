package service;

import database.DatabaseConnection;
import model.Login;
import java.sql.*;
import java.util.regex.Pattern;


public class LoginService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public String validasiLogin(Login login) {
        if (!Pattern.matches(EMAIL_REGEX, login.getEmail())) {
            return "Format email tidak valid";
        }
        if (login.getPassword().length() < 8) {
            return "Password minimal harus 8 karakter";
        }
        return "OK";
    }


public boolean simpanLogin(Login login) {
    String query = "INSERT INTO users (email, password) VALUES (?, ?)"; 
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        ps.setString(1, login.getEmail());
        ps.setString(2, login.getPassword());
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Gagal menyimpan: " + e.getMessage());
        return false;
    }
}

    public boolean cekLogin(Login login) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, login.getEmail());
            ps.setString(2, login.getPassword());
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error saat cek login: " + e.getMessage());
            return false;
        }
    }
}
