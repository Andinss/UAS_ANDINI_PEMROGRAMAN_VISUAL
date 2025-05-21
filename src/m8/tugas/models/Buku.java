/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package m8.tugas.models;

import java.time.LocalDate;

public class Buku {
    private String kodeBuku;
    private KategoriBuku kategori;
    private String judul;
    private String pengarang;
    private String penerbit;
    private int tahunTerbit;
    private int edisi;
    private LocalDate tanggalPengadaan;

    public Buku() {
    }

    public Buku(String kodeBuku, KategoriBuku kategori, String judul, String pengarang,
                String penerbit, int tahunTerbit, int edisi, LocalDate tanggalPengadaan) {
        this.kodeBuku = kodeBuku;
        this.kategori = kategori;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.edisi = edisi;
        this.tanggalPengadaan = tanggalPengadaan;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public KategoriBuku getKategori() {
        return kategori;
    }

    public void setKategori(KategoriBuku kategori) {
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public int getEdisi() {
        return edisi;
    }

    public void setEdisi(int edisi) {
        this.edisi = edisi;
    }

    public LocalDate getTanggalPengadaan() {
        return tanggalPengadaan;
    }

    public void setTanggalPengadaan(LocalDate tanggalPengadaan) {
        this.tanggalPengadaan = tanggalPengadaan;
    }
}
