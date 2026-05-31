package model;

public class DeteksiHama {
    private int id;
    private String namaHamaPenyakit;
    private String gejala;
    private String solusiPenanganan;
    private String tingkatBahaya;

    public DeteksiHama(String namaHamaPenyakit, String gejala, String solusiPenanganan, String tingkatBahaya) {
        this.namaHamaPenyakit = namaHamaPenyakit;
        this.gejala = gejala;
        this.solusiPenanganan = solusiPenanganan;
        this. tingkatBahaya = tingkatBahaya;
    }

    public DeteksiHama(int id, String namaHamaPenyakit, String gejala, String solusiPenanganan, String tingkatBahaya) {
        this.id = id;
        this.namaHamaPenyakit = namaHamaPenyakit;
        this.gejala = gejala;
        this.solusiPenanganan = solusiPenanganan;
        this.tingkatBahaya = tingkatBahaya;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaHamaPenyakit() { return namaHamaPenyakit; }
    public void setNamaHamaPenyakit(String namaHamaPenyakit) { this.namaHamaPenyakit = namaHamaPenyakit; }

    public String getGejala() { return gejala; }
    public void setGejala(String gejala) { this.gejala = gejala; }

    public String getSolusiPenanganan() { return solusiPenanganan; }
    public void setSolusiPenanganan(String solusiPenanganan) { this.solusiPenanganan = solusiPenanganan; }

    public String getTingkatBahaya() { return tingkatBahaya; }
    public void setTingkatBahaya(String tingkatBahaya) { this.tingkatBahaya = tingkatBahaya; }
}