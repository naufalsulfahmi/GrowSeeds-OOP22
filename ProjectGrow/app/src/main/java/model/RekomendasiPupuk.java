package model;

public class RekomendasiPupuk {
    private int id;
    private String jenisTanaman;
    private double luasLahan;
    private double kebutuhanUrea;
    private double kebutuhanNpk;

    public RekomendasiPupuk(String jenisTanaman, double luasLahan, double kebutuhanUrea, double kebutuhanNpk) {
        this.jenisTanaman = jenisTanaman;
        this.luasLahan = luasLahan;
        this.kebutuhanUrea = kebutuhanUrea;
        this.kebutuhanNpk = kebutuhanNpk;
    }

    public RekomendasiPupuk(int id, String jenisTanaman, double luasLahan, double kebutuhanUrea, double kebutuhanNpk) {
        this.id = id;
        this.jenisTanaman = jenisTanaman;
        this.luasLahan = luasLahan;
        this.kebutuhanUrea = kebutuhanUrea;
        this.kebutuhanNpk = kebutuhanNpk;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJenisTanaman() { return jenisTanaman; }
    public void setJenisTanaman(String jenisTanaman) { this.jenisTanaman = jenisTanaman; }

    public double getLuasLahan() { return luasLahan; }
    public void setLuasLahan(double luasLahan) { this.luasLahan = luasLahan; }

    public double getKebutuhanUrea() { return kebutuhanUrea; }
    public void setKebutuhanUrea(double kebutuhanUrea) { this.kebutuhanUrea = kebutuhanUrea; }

    public double getKebutuhanNpk() { return kebutuhanNpk; }
    public void setKebutuhanNpk(double kebutuhanNpk) { this.kebutuhanNpk = kebutuhanNpk; }
}