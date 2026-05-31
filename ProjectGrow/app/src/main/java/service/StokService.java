package service;

import database.DatabaseConnection;
import model.Stok;
import model.StokBuah;
import model.StokSayur;
import model.StokBahanPangan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StokService {

    private Stok StokData(int id, String nama, String kat, double jml, double trj) {
        if(kat.equalsIgnoreCase("Buah")) {
            return new StokBuah(id, nama, jml, trj);
        }else if(kat.equalsIgnoreCase("Sayur")) {
            return new StokSayur(id, nama, jml, trj);
        }else{
            return new StokBahanPangan(id, nama, jml, trj);
        }
    }
    public boolean tambahStok(String nama, String kategori, double jumlahTambahan) {
        String cekQuery = "SELECT id_stok, jumlah, terjual FROM stok WHERE nama_barang = ? AND kategori = ?";
        try(Connection conn = DatabaseConnection.getConnection();
             PreparedStatement cekStmt = conn.prepareStatement(cekQuery)) {
            
            cekStmt.setString(1, nama);
            cekStmt.setString(2, kategori);
            ResultSet rs = cekStmt.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id_stok");
                double jumlahBaru = rs.getDouble("jumlah") + jumlahTambahan;
                double terjualSaatIni = rs.getDouble("terjual"); 
                
                String updateQuery = "UPDATE stok SET jumlah = ?, status = ? WHERE id_stok = ?";
                try(PreparedStatement updStmt = conn.prepareStatement(updateQuery)) {
                    Stok temp = StokData(id, nama, kategori, jumlahBaru, terjualSaatIni);
                    updStmt.setDouble(1, jumlahBaru);
                    updStmt.setString(2, temp.hitungStatusStok());
                    updStmt.setInt(3, id);
                    return updStmt.executeUpdate() > 0;
                }
            }else{
                String insertQuery = "INSERT INTO stok (nama_barang, kategori, jumlah, terjual, status) VALUES (?, ?, ?, 0.0, ?)";
                try(PreparedStatement insStmt = conn.prepareStatement(insertQuery)) {
                    Stok temp = StokData(0, nama, kategori, jumlahTambahan, 0.0);
                    insStmt.setString(1, nama);
                    insStmt.setString(2, kategori);
                    insStmt.setDouble(3, jumlahTambahan);
                    insStmt.setString(4, temp.hitungStatusStok());
                    return insStmt.executeUpdate() > 0;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean jualStok(String nama, String kategori, double jumlahTerjual) {
        String query = "SELECT id_stok, jumlah, terjual FROM stok WHERE nama_barang = ? AND kategori = ?";
        try(Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nama);
            pstmt.setString(2, kategori);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                int id = rs.getInt("id_stok");
                double jumlahLama = rs.getDouble("jumlah");
                double terjualLama = rs.getDouble("terjual");

                if(jumlahLama < jumlahTerjual) return false; 

                double jumlahBaru = jumlahLama - jumlahTerjual;
                double terjualBaru = terjualLama + jumlahTerjual; 
                
                String updateQuery = "UPDATE stok SET jumlah = ?, terjual = ?, status = ? WHERE id_stok = ?";
                try (PreparedStatement updStmt = conn.prepareStatement(updateQuery)) {
                    Stok temp = StokData(id, nama, kategori, jumlahBaru, terjualBaru);
                    updStmt.setDouble(1, jumlahBaru);
                    updStmt.setDouble(2, terjualBaru);
                    updStmt.setString(3, temp.hitungStatusStok());
                    updStmt.setInt(4, id);
                    return updStmt.executeUpdate() > 0;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Stok> CaridanListStok (String keyword) {
        List<Stok> daftarStok = new ArrayList<>();
        String query = "SELECT * FROM stok WHERE nama_barang LIKE ? OR kategori LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String key = "%" + keyword + "%";
            pstmt.setString(1, key);
            pstmt.setString(2, key);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id_stok");
                String nama = rs.getString("nama_barang");
                String kat = rs.getString("kategori");
                double jml = rs.getDouble("jumlah");
                double trj = rs.getDouble("terjual"); 

                daftarStok.add(StokData(id, nama, kat, jml, trj));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return daftarStok;
    }

}