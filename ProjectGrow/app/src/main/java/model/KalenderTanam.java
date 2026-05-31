package model;

public class KalenderTanam {
    private int id;
    private String namaTanaman;
    private String tanggalSemai;
    private String estimasiPanen;
    private String fasePertumbuhan;

    public KalenderTanam(String namaTanaman, String tanggalSemai, String estimasiPanen, String fasePertumbuhan) {
        this.namaTanaman = namaTanaman;
        this.tanggalSemai = tanggalSemai;
        this.estimasiPanen = estimasiPanen;
        this.fasePertumbuhan = fasePertumbuhan;
    }

    public KalenderTanam(int id, String namaTanaman, String tanggalSemai, String estimasiPanen, String fasePertumbuhan) {
        this.id = id;
        this.namaTanaman = namaTanaman;
        this.tanggalSemai = tanggalSemai;
        this.estimasiPanen = estimasiPanen;
        this.fasePertumbuhan = fasePertumbuhan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaTanaman() { return namaTanaman; }
    public void setNamaTanaman(String namaTanaman) { this.namaTanaman = namaTanaman; }

    public String getTanggalSemai() { return tanggalSemai; }
    public void setTanggalSemai(String tanggalSemai) { this.tanggalSemai = tanggalSemai; }

    public String getEstimasiPanen() { return estimasiPanen; }
    public void setEstimasiPanen(String estimasiPanen) { this.estimasiPanen = estimasiPanen; }

    public String getFasePertumbuhan() { return fasePertumbuhan; }
    public void setFasePertumbuhan(String fasePertumbuhan) { this.fasePertumbuhan = fasePertumbuhan; }
}