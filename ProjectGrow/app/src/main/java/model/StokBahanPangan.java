package model;

public class StokBahanPangan extends Stok {
    public StokBahanPangan(int idStok, String namaBarang, double jumlah, double terjual) {
        super(idStok, namaBarang, "Bahan Pangan", jumlah, terjual);
    }

    @Override
    public String hitungStatusStok() {
        if (this.jumlah <= 0) 
            return "Habis";
        if (this.jumlah < 10) 
            return "Stok Menipis";
        return "Tersedia";
    }
}