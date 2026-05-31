package model;


interface Securable {
    void verifikasiAkses(); 
}


public class Profile implements Securable {
    

    private int id;
    private String namaPengguna;
    private String emailKontak;
    private String statusLahan;


    public Profile(String namaPengguna, String emailKontak, String statusLahan) {
        this.namaPengguna = namaPengguna;
        this.emailKontak = emailKontak;
        this.statusLahan = statusLahan;
    }

 
    public Profile(int id, String namaPengguna, String emailKontak, String statusLahan) {
        this.id = id;
        this.namaPengguna = namaPengguna;
        this.emailKontak = emailKontak;
        this.statusLahan = statusLahan;
    }


    @Override
    public void verifikasiAkses() {

        System.out.println("Akses diverifikasi untuk pengguna: " + this.namaPengguna);
    }


    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNamaPengguna() { 
        return namaPengguna; 
    }
    public void setNamaPengguna(String namaPengguna) { 
        this.namaPengguna = namaPengguna; 
    }

    public String getEmailKontak() { 
        return emailKontak; 
    }
    public void setEmailKontak(String emailKontak) { 
        this.emailKontak = emailKontak; 
    }

    public String getStatusLahan() { 
        return statusLahan; 
    }
    public void setStatusLahan(String statusLahan) { 
        this.statusLahan = statusLahan; 
    }
}