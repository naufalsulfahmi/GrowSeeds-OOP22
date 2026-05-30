package service;

import database.DatabaseConnection;
import model.Panen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanenService {
    public void tambahPanen(Panen panen) {
        String sql = "INSERT INTO panen (nama_lahan, jenis_tanaman, jumlah_panen, satuan, tanggal_panen, kondisi_cuaca) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, panen.getNamaLahan());
            pstmt.setString(2, panen.getJenisTanaman());
            pstmt.setDouble(3, panen.getJumlahPanen());
            pstmt.setString(4, panen.getSatuan());
            pstmt.setString(5, panen.getTanggalPanen());
            pstmt.setString(6, panen.getKondisiCuaca());

            pstmt.executeUpdate();
            System.out.println("Data panen berhasil disimpan!");

        } catch (SQLException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    public List<Panen> getAllPanen() {
        List<Panen> listPanen = new ArrayList<>();
        String sql = "SELECT * FROM panen";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Panen p = new Panen(
                        rs.getInt("id"),
                        rs.getString("nama_lahan"),
                        rs.getString("jenis_tanaman"),
                        rs.getDouble("jumlah_panen"),
                        rs.getString("satuan"),
                        rs.getString("tanggal_panen"),
                        rs.getString("kondisi_cuaca")
                );
                listPanen.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
        }
        return listPanen;
    }

    public void updatePanen(Panen panen) {
        String sql = "UPDATE panen SET nama_lahan = ?, jenis_tanaman = ?, jumlah_panen = ?, " +
                     "satuan = ?, tanggal_panen = ?, kondisi_cuaca = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, panen.getNamaLahan());
            pstmt.setString(2, panen.getJenisTanaman());
            pstmt.setDouble(3, panen.getJumlahPanen());
            pstmt.setString(4, panen.getSatuan());
            pstmt.setString(5, panen.getTanggalPanen());
            pstmt.setString(6, panen.getKondisiCuaca());
            pstmt.setInt(7, panen.getId()); 
            pstmt.executeUpdate();
            System.out.println("Data panen berhasil diupdate!");

        } catch (SQLException e) {
            System.out.println("Gagal mengupdate data: " + e.getMessage());
        }
    }

    public void hapusPanen(int id) {
        String sql = "DELETE FROM panen WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Data panen berhasil dihapus!");

        } catch (SQLException e) {
            System.out.println("Gagal menghapus data: " + e.getMessage());
        }
    }
}
