package service;

import database.DatabaseConnection;
import model.Login;
import session.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;

public class LoginService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public enum RegistrationStatus {
        SUCCESS,
        EMAIL_ALREADY_USED,
        DATABASE_ERROR
    }

    public String validasiLogin(Login login) {
        if (login == null || login.getEmail() == null || login.getEmail().isBlank()) {
            return "Email wajib diisi.";
        }
        if (!Pattern.matches(EMAIL_REGEX, login.getEmail().trim())) {
            return "Format email tidak valid.";
        }
        if (login.getPassword() == null || login.getPassword().isBlank()) {
            return "Password wajib diisi.";
        }
        if (login.getPassword().length() < 8) {
            return "Password minimal harus 8 karakter.";
        }
        return "OK";
    }

    public RegistrationStatus registrasi(Login login) {
        String email = normalisasiEmail(login.getEmail());
        if (emailSudahTerdaftar(email)) {
            return RegistrationStatus.EMAIL_ALREADY_USED;
        }

        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, login.getPassword());
            ps.executeUpdate();
            return RegistrationStatus.SUCCESS;
        } catch (SQLException e) {
            if (isUniqueConstraintViolation(e)) {
                return RegistrationStatus.EMAIL_ALREADY_USED;
            }
            System.err.println("Gagal menyimpan akun: " + e.getMessage());
            return RegistrationStatus.DATABASE_ERROR;
        }
    }

    public boolean simpanLogin(Login login) {
        return registrasi(login) == RegistrationStatus.SUCCESS;
    }

    public boolean cekLogin(Login login) {
        String query = "SELECT id_user, email FROM users WHERE LOWER(email) = LOWER(?) AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, normalisasiEmail(login.getEmail()));
            ps.setString(2, login.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserSession.login(rs.getInt("id_user"), rs.getString("email"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat cek login: " + e.getMessage());
        }
        return false;
    }

    public boolean emailSudahTerdaftar(String email) {
        String query = "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, normalisasiEmail(email));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa email: " + e.getMessage());
            return false;
        }
    }

    private String normalisasiEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isUniqueConstraintViolation(SQLException e) {
        String message = e.getMessage();
        return message != null && message.toLowerCase(Locale.ROOT).contains("unique");
    }
}
