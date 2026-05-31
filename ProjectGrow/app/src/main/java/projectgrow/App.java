
package projectgrow;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.Panen;
import service.PanenService;

public class App {

    public static void main(String[] args) {
        // TESTING UNTUK LOGIKA HOME DAN MANAJEMEN PANEN BERBASIS CLI (TANPA UI)
        System.out.println("=== SISTEM PERTANIAN BERJALAN ===");

        DatabaseConnection.inisialisasiDatabase();
        PanenService service = new PanenService();
        service.tambahPanen(new Panen("Sawah A", "Padi", 1000, "Kg", "2026-05-20", "Cerah"));
        service.tambahPanen(new Panen("Sawah B", "Padi", 500, "Kg", "2026-05-21", "Cerah"));

        tampilkanHome(service);

        System.out.println("[MENU MANAJEMEN PANEN] -> Melihat Semua Data");
        cetakData(service);
    }

    private static void tampilkanHome(PanenService service) {
        System.out.println("\n========== HOME DASHBOARD ==========");
        List<Panen> daftar = service.getAllPanen();

        double totalHasilPanen = 0;
        List<String> lahanAktif = new ArrayList<>();

        for (Panen p : daftar) {
            totalHasilPanen += p.getJumlahPanen();

            if (!lahanAktif.contains(p.getNamaLahan())) {
                lahanAktif.add(p.getNamaLahan());
            }
        }

        System.out.println("RINGKASAN PANEN");
        System.out.println("   - Total Hasil Panen  : " + totalHasilPanen);
        System.out.println("   - Jumlah Lahan Aktif : " + lahanAktif.size() + " lahan");
        
        System.out.println("\nTIPS PERTANIAN HARI INI");
        System.out.println("   - Pastikan cuaca cerah saat memanen padi agar kualitas gabah terjaga.");
        System.out.println("=======================================\n");
    }

    private static void cetakData(PanenService service) {
        List<Panen> daftar = service.getAllPanen();
        System.out.println("--------------------------------------------------");
        for (Panen p : daftar) {
            System.out.printf("ID: %d | %s | %s | %.2f %s | %s\n",
                    p.getId(), p.getNamaLahan(), p.getJenisTanaman(), p.getJumlahPanen(), p.getSatuan(), p.getKondisiCuaca());
        }
        System.out.println("--------------------------------------------------\n");
    }
}
