package database;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:coba.growseeds.db";

    static {
        inisialisasiDatabase();
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver SQLite tidak ditemukan. ",
                    e
            );
        }

        return DriverManager.getConnection(URL);
    }

    public static void inisialisasiDatabase() {

        String queryTabelUser = "CREATE TABLE IF NOT EXISTS users (" +
                                "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "email TEXT NOT NULL UNIQUE, " +
                                "password TEXT NOT NULL" +
                                ");";

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

            stmt.executeUpdate(queryTabelUser);
            stmt.executeUpdate(sql);

            File databaseFile = new File("cobagrowseeds.db");

            System.out.println("Database GrowSeeds siap digunakan.");
            System.out.println("Lokasi database: "
                    + databaseFile.getAbsolutePath());


        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal inisialisasi database: " + e.getMessage(),
                    e
            );
        }
    }
}
    