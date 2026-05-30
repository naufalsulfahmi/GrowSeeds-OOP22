package model;

public class StokSayur extends Stok {
    public StokSayur(int idStok, String namaBarang, double jumlah, double terjual) {
        super(idStok, namaBarang, "Sayur", jumlah, terjual);
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