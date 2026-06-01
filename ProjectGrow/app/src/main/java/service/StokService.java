package service;

import database.DatabaseConnection;
import model.Stok;
import model.StokBahanPangan;
import model.StokBuah;
import model.StokSayur;
import session.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StokService {
    private static final double BATAS_STOK_RENDAH = 10.0;

    private Stok stokData(int id, String nama, String kategori, double jumlah, double terjual) {
        if (kategori.equalsIgnoreCase("Buah")) {
            return new StokBuah(id, nama, jumlah, terjual);
        }
        if (kategori.equalsIgnoreCase("Sayur")) {
            return new StokSayur(id, nama, jumlah, terjual);
        }
        return new StokBahanPangan(id, nama, jumlah, terjual);
    }


    public boolean tambahStok(String nama, String kategori, double jumlahTambahan) {
        if (!inputValid(nama, kategori, jumlahTambahan)) {
            return false;
        }

        Integer idUser = ambilIdUserAktif();
        if (idUser == null) {
            return false;
        }

        String namaBersih = nama.trim();
        String kategoriBersih = normalisasiKategori(kategori);
        String cekQuery = "SELECT id_stok, jumlah, COALESCE(terjual, 0) AS terjual " +
                "FROM stok WHERE id_user = ? " +
                "AND LOWER(nama_barang) = LOWER(?) " +
                "AND LOWER(kategori) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            persiapkanTabelStok(conn, idUser);
            conn.setAutoCommit(false);

            try (PreparedStatement cekStmt = conn.prepareStatement(cekQuery)) {
                cekStmt.setInt(1, idUser);
                cekStmt.setString(2, namaBersih);
                cekStmt.setString(3, kategoriBersih);

                try (ResultSet rs = cekStmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id_stok");
                        double jumlahBaru = rs.getDouble("jumlah") + jumlahTambahan;
                        double terjualSaatIni = rs.getDouble("terjual");

                        String updateQuery ="UPDATE stok SET jumlah = ?, status = ? " + "WHERE id_stok = ? AND id_user = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            Stok stok = stokData(id, namaBersih, kategoriBersih, jumlahBaru, terjualSaatIni);
                            updateStmt.setDouble(1, jumlahBaru);
                            updateStmt.setString(2, stok.hitungStatusStok());
                            updateStmt.setInt(3, id);
                            updateStmt.setInt(4, idUser);

                            boolean berhasil = updateStmt.executeUpdate() > 0;
                            if (berhasil) {
                                conn.commit();
                            } else {
                                conn.rollback();
                            }
                            return berhasil;
                        }
                    }
                }

                String insertQuery = "INSERT INTO stok " +
                                    "(id_user, nama_barang, kategori, jumlah, terjual, status) " +
                                    "VALUES (?, ?, ?, ?, 0.0, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    Stok stok = stokData(0, namaBersih, kategoriBersih, jumlahTambahan, 0.0);
                    insertStmt.setInt(1, idUser);
                    insertStmt.setString(2, namaBersih);
                    insertStmt.setString(3, kategoriBersih);
                    insertStmt.setDouble(4, jumlahTambahan);
                    insertStmt.setString(5, stok.hitungStatusStok());

                    boolean berhasil = insertStmt.executeUpdate() > 0;
                    if (berhasil) {
                        conn.commit();
                    } else {
                        conn.rollback();
                    }
                    return berhasil;
                }
            } catch (SQLException e) {
                rollbackTanpaMengganggu(conn);
                System.err.println("Gagal menambah stok: " + e.getMessage());
                return false;
            } finally {
                kembalikanAutoCommit(conn);
            }
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database stok: " + e.getMessage());
            return false;
        }
    }

    public boolean jualStok(String nama, String kategori, double jumlahTerjual) {
        if (!inputValid(nama, kategori, jumlahTerjual)) {
            return false;
        }

        Integer idUser = ambilIdUserAktif();
        if (idUser == null) {
            return false;
        }

        String namaBersih = nama.trim();
        String kategoriBersih = normalisasiKategori(kategori);
        String cekQuery = "SELECT id_stok, jumlah, COALESCE(terjual, 0) AS terjual " +
                "FROM stok WHERE id_user = ? " +
                "AND LOWER(nama_barang) = LOWER(?) " +
                "AND LOWER(kategori) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            persiapkanTabelStok(conn, idUser);
            conn.setAutoCommit(false);

            try (PreparedStatement cekStmt = conn.prepareStatement(cekQuery)) {
                cekStmt.setInt(1, idUser);
                cekStmt.setString(2, namaBersih);
                cekStmt.setString(3, kategoriBersih);

                try (ResultSet rs = cekStmt.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }

                    int id = rs.getInt("id_stok");
                    double jumlahLama = rs.getDouble("jumlah");
                    double terjualLama = rs.getDouble("terjual");

                    if (jumlahLama < jumlahTerjual) {
                        conn.rollback();
                        return false;
                    }

                    double jumlahBaru = jumlahLama - jumlahTerjual;
                    double terjualBaru = terjualLama + jumlahTerjual;
                    String updateQuery = "UPDATE stok SET jumlah = ?, terjual = ?, status = ? " +
                            "WHERE id_stok = ? AND id_user = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        Stok stok = stokData(id, namaBersih, kategoriBersih, jumlahBaru, terjualBaru);
                        updateStmt.setDouble(1, jumlahBaru);
                        updateStmt.setDouble(2, terjualBaru);
                        updateStmt.setString(3, stok.hitungStatusStok());
                        updateStmt.setInt(4, id);
                        updateStmt.setInt(5, idUser);

                        boolean berhasil = updateStmt.executeUpdate() > 0;
                        if (berhasil) {
                            conn.commit();
                        } else {
                            conn.rollback();
                        }
                        return berhasil;
                    }
                }
            } catch (SQLException e) {
                rollbackTanpaMengganggu(conn);
                System.err.println("Gagal mencatat penjualan stok: " + e.getMessage());
                return false;
            } finally {
                kembalikanAutoCommit(conn);
            }
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database stok: " + e.getMessage());
            return false;
        }
    }


    public List<Stok> cariDanListStok(String keyword) {
        Integer idUser = ambilIdUserAktif();
        List<Stok> daftarStok = new ArrayList<>();
        if (idUser == null) {
            return daftarStok;
        }

        String query = "SELECT id_stok, nama_barang, kategori, jumlah, " +
                    "COALESCE(terjual, 0) AS terjual FROM stok " +
                    "WHERE id_user = ? AND " +
                    "(LOWER(nama_barang) LIKE LOWER(?) OR LOWER(kategori) LIKE LOWER(?)) " +
                    "ORDER BY CASE status " +
                    "WHEN 'Habis' THEN 1 WHEN 'Stok Menipis' THEN 2 ELSE 3 END, nama_barang";

        try (Connection conn = DatabaseConnection.getConnection()) {
            persiapkanTabelStok(conn, idUser);

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                String key = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                stmt.setInt(1, idUser);
                stmt.setString(2, key);
                stmt.setString(3, key);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        daftarStok.add(stokData(
                                rs.getInt("id_stok"),
                                rs.getString("nama_barang"),
                                rs.getString("kategori"),
                                rs.getDouble("jumlah"),
                                rs.getDouble("terjual")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data stok: " + e.getMessage());
        }
        return daftarStok;
    }


    public List<Stok> CaridanListStok(String keyword) {
        return cariDanListStok(keyword);
    }

    public List<Stok> getStokRendah() {
        List<Stok> stokRendah = new ArrayList<>();
        for (Stok stok : cariDanListStok("")) {
            if (stok.getJumlah() < BATAS_STOK_RENDAH) {
                stokRendah.add(stok);
            }
        }
        return stokRendah;
    }

    public int hitungStokRendah() {
        return getStokRendah().size();
    }

    public int hitungTotalJenisStok() {
        return cariDanListStok("").size();
    }

    private boolean inputValid(String nama, String kategori, double jumlah) {
        return nama != null && !nama.isBlank()
                && kategori != null && !kategori.isBlank()
                && jumlah > 0;
    }

    private String normalisasiKategori(String kategori) {
        String nilai = kategori == null ? "" : kategori.trim();
        if (nilai.equalsIgnoreCase("Buah")) {
            return "Buah";
        }
        if (nilai.equalsIgnoreCase("Sayur")) {
            return "Sayur";
        }
        return "Bahan Pangan";
    }

    private Integer ambilIdUserAktif() {
        try {
            return UserSession.requireUserId();
        } catch (IllegalStateException e) {
            System.err.println("Pelacakan stok memerlukan akun yang sedang login.");
            return null;
        }
    }

    private void persiapkanTabelStok(Connection conn, int idUser) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stok (" +
                    "id_stok INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_user INTEGER, " +
                    "nama_barang TEXT NOT NULL, " +
                    "kategori TEXT NOT NULL, " +
                    "jumlah REAL NOT NULL DEFAULT 0, " +
                    "terjual REAL NOT NULL DEFAULT 0, " +
                    "status TEXT NOT NULL DEFAULT 'Tersedia'" +
                    ")");
        }

        tambahKolomJikaBelumAda(conn, "id_user", "INTEGER");
        tambahKolomJikaBelumAda(conn, "terjual", "REAL NOT NULL DEFAULT 0");
        tambahKolomJikaBelumAda(conn, "status", "TEXT NOT NULL DEFAULT 'Tersedia'");

        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE stok SET id_user = ? WHERE id_user IS NULL")) {
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE stok SET terjual = 0 WHERE terjual IS NULL");
            stmt.executeUpdate("UPDATE stok SET status = 'Tersedia' WHERE status IS NULL OR TRIM(status) = ''");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_stok_user ON stok(id_user)");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_stok_user_barang " +
                    "ON stok(id_user, nama_barang, kategori)");
        }
    }

    private void tambahKolomJikaBelumAda(Connection conn, String namaKolom, String definisiKolom)
            throws SQLException {
        if (kolomAda(conn, namaKolom)) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE stok ADD COLUMN " + namaKolom + " " + definisiKolom);
        }
    }

    private boolean kolomAda(Connection conn, String namaKolom) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(stok)")) {
            while (rs.next()) {
                if (namaKolom.equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void rollbackTanpaMengganggu(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ignored) {

        }
    }

    private void kembalikanAutoCommit(Connection conn) {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ignored) {}
    }
}
