package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:pertanian.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS panen ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nama_lahan TEXT NOT NULL, "
                   + "jenis_tanaman TEXT NOT NULL, "
                   + "jumlah_panen REAL NOT NULL, "
                   + "satuan TEXT NOT NULL, "
                   + "tanggal_panen TEXT NOT NULL, "
                   + "kondisi_cuaca TEXT"
                   + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Database dan tabel panen siap digunakan!");
            
        } catch (SQLException e) {
            System.out.println("Gagal menyiapkan database: " + e.getMessage());
        }
    }
}
