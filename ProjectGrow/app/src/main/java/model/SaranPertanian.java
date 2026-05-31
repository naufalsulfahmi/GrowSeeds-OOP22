package model;

public class SaranPertanian {
    private int id;
    private String tanggalSaran;
    private String statusLahan;
    private String rekomendasiTindakan;

    public SaranPertanian(String tanggalSaran, String statusLahan, String rekomendasiTindakan) {
        this.tanggalSaran = tanggalSaran;
        this.statusLahan = statusLahan;
        this.rekomendasiTindakan = rekomendasiTindakan;
    }

    public SaranPertanian(int id, String tanggalSaran, String statusLahan, String rekomendasiTindakan) {
        this.id = id;
        this.tanggalSaran = tanggalSaran;
        this.statusLahan = statusLahan;
        this.rekomendasiTindakan = rekomendasiTindakan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTanggalSaran() { return tanggalSaran; }
    public void setTanggalSaran(String tanggalSaran) { this.tanggalSaran = tanggalSaran; }

    public String getStatusLahan() { return statusLahan; }
    public void setStatusLahan(String statusLahan) { this.statusLahan = statusLahan; }

    public String getRekomendasiTindakan() { return rekomendasiTindakan; }
    public void setRekomendasiTindakan(String rekomendasiTindakan) { this.rekomendasiTindakan = rekomendasiTindakan; }
}