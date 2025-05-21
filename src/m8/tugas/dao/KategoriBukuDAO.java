/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package m8.tugas.dao;

import m8.tugas.models.KategoriBuku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriBukuDAO {

    private Connection conn;

    public KategoriBukuDAO(Connection conn) {
        this.conn = conn;
    }

    public List<KategoriBuku> getAllKategori() {
    List<KategoriBuku> list = new ArrayList<>();
    try {
        String sql = "SELECT * FROM kategori_buku";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            KategoriBuku kategori = new KategoriBuku();
            kategori.setKodeKategori(rs.getString("kode_kategori"));
            kategori.setNamaKategori(rs.getString("nama_kategori"));
            list.add(kategori);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


    public KategoriBuku getKategoriByKode(String kode) throws SQLException {
        String sql = "SELECT * FROM kategori_buku WHERE kode_kategori = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, kode);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new KategoriBuku(
                rs.getString("kode_kategori"),
                rs.getString("nama_kategori")
            );
        }

        return null;
    }
}
