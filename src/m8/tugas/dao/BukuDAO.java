/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package m8.tugas.dao;

import m8.tugas.models.Buku;
import m8.tugas.models.KategoriBuku;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    private Connection conn;

    public BukuDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Buku> getAllBuku() throws SQLException {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT b.*, k.nama_kategori FROM buku b JOIN kategori_buku k ON b.kode_kategori = k.kode_kategori";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            KategoriBuku kategori = new KategoriBuku(
                rs.getString("kode_kategori"),
                rs.getString("nama_kategori")
            );

            Buku buku = new Buku(
                rs.getString("kode_buku"),
                kategori,
                rs.getString("judul"),
                rs.getString("pengarang"),
                rs.getString("penerbit"),
                rs.getInt("tahun_terbit"),
                rs.getInt("edisi"),
                rs.getDate("tanggal_pengadaan").toLocalDate()
            );

            list.add(buku);
        }

        return list;
    }

    public void insertBuku(Buku buku) throws SQLException {
        String sql = "INSERT INTO buku (kode_buku, kode_kategori, judul, pengarang, penerbit, tahun_terbit, edisi, tanggal_pengadaan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, buku.getKodeBuku());
        stmt.setString(2, buku.getKategori().getKodeKategori());
        stmt.setString(3, buku.getJudul());
        stmt.setString(4, buku.getPengarang());
        stmt.setString(5, buku.getPenerbit());
        stmt.setInt(6, buku.getTahunTerbit());
        stmt.setInt(7, buku.getEdisi());
        stmt.setDate(8, Date.valueOf(buku.getTanggalPengadaan()));
        stmt.executeUpdate();
    }

    public void updateBuku(Buku buku) throws SQLException {
        String sql = "UPDATE buku SET kode_kategori = ?, judul = ?, pengarang = ?, penerbit = ?, tahun_terbit = ?, edisi = ?, tanggal_pengadaan = ? WHERE kode_buku = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, buku.getKategori().getKodeKategori());
        stmt.setString(2, buku.getJudul());
        stmt.setString(3, buku.getPengarang());
        stmt.setString(4, buku.getPenerbit());
        stmt.setInt(5, buku.getTahunTerbit());
        stmt.setInt(6, buku.getEdisi());
        stmt.setDate(7, Date.valueOf(buku.getTanggalPengadaan()));
        stmt.setString(8, buku.getKodeBuku());
        stmt.executeUpdate();
    }

    public void deleteBuku(String kodeBuku) throws SQLException {
        String sql = "DELETE FROM buku WHERE kode_buku = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, kodeBuku);
        stmt.executeUpdate();
    }
}

