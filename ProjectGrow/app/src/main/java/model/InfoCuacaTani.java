package model;

public class InfoCuacaTani {
    private int id;
    private String wilayahLahan;
    private double suhu;
    private double kelembaban;
    private double curahHujan; // Nilai mm yang kita bahas kemarin

    public InfoCuacaTani(String wilayahLahan, double suhu, double kelembaban, double curahHujan) {
        this.wilayahLahan = wilayahLahan;
        this.suhu = suhu;
        this.kelembaban = kelembaban;
        this.curahHujan = curahHujan;
    }

    public InfoCuacaTani(int id, String wilayahLahan, double suhu, double kelembaban, double curahHujan) {
        this.id = id;
        this.wilayahLahan = wilayahLahan;
        this.suhu = suhu;
        this.kelembaban = kelembaban;
        this.curahHujan = curahHujan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWilayahLahan() { return wilayahLahan; }
    public void setWilayahLahan(String wilayahLahan) { this.wilayahLahan = wilayahLahan; }

    public double getSuhu() { return suhu; }
    public void setSuhu(double suhu) { this.suhu = suhu; }

    public double getKelembaban() { return kelembaban; }
    public void setKelembaban(double kelembaban) { this.kelembaban = kelembaban; }

    public double getCurahHujan() { return curahHujan; }
    public void setCurahHujan(double curahHujan) { this.curahHujan = curahHujan; }
}