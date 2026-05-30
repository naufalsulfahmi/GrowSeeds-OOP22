package model;


public abstract class Stok {
    protected int idStok;
    protected String namaBarang;
    protected String kategori;
    protected double jumlah;
    protected double terjual;

    public Stok(int idStok, String namaBarang, String kategori, double jumlah,double terjual) {
        this.idStok = idStok;
        this.namaBarang = namaBarang;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.terjual = terjual;
    }

    public abstract String hitungStatusStok();

    
    public int getIdStok() {
        return idStok; 
    }
    public void setIdStok(int idStok) { 
        this.idStok = idStok; 
    }


    public String getNamaBarang() { 
        return namaBarang; 
    }
    public void setNamaBarang(String namaBarang) { 
        this.namaBarang = namaBarang; 
    }


    public String getKategori() { 
        return kategori; 
    }
    public void setKategori(String kategori) {
         this.kategori = kategori; 
    }


    public double getJumlah() {
         return jumlah; 
    }
    public void setJumlah(double jumlah) {
         this.jumlah = jumlah; 
    }

    
    public double getTerjual() {
         return terjual; 
    }
    public void setTerjual(double terjual) {
         this.terjual = terjual; 
    }

}