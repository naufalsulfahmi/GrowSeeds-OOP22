package model;

public class StokBuah extends Stok {
    public StokBuah(int idStok, String namaBarang, double jumlah, double terjual) {
        super(idStok, namaBarang, "Buah", jumlah, terjual);
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