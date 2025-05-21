/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package m8.tugas.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import m8.tugas.dao.BukuDAO;
import m8.tugas.dao.KategoriBukuDAO;
import m8.tugas.dao.DBUtil;
import m8.tugas.models.Buku;
import m8.tugas.models.KategoriBuku;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BukuController implements Initializable {

    @FXML private TextField kodeField, judulField, pengarangField, penerbitField, tahunField, edisiField;
    @FXML private DatePicker tanggalPengadaanPicker;
    @FXML private ComboBox<KategoriBuku> kategoriComboBox;

    @FXML private TableView<Buku> bukuTable;
    @FXML private TableColumn<Buku, String> kategoriColumn;
    @FXML private TableColumn<Buku, String> kodeColumn, judulColumn, pengarangColumn, penerbitColumn;
    @FXML private TableColumn<Buku, Integer> tahunColumn, edisiColumn;
    @FXML private TableColumn<Buku, LocalDate> pengadaanColumn;

    private final BukuDAO bukuDAO;
    private final KategoriBukuDAO kategoriDAO;

    public BukuController() {
        try {
            Connection conn = DBUtil.getConnection();
            bukuDAO = new BukuDAO(conn);
            kategoriDAO = new KategoriBukuDAO(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke database", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set CellValueFactory sesuai field model Buku
        kodeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBuku()));
        kategoriColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKategori().getNamaKategori()));
        judulColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getJudul()));
        pengarangColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPengarang()));
        penerbitColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPenerbit()));
        tahunColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTahunTerbit()).asObject());
        edisiColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getEdisi()).asObject());
        pengadaanColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTanggalPengadaan()));

        bukuTable.setOnMouseClicked(event -> handleTableClick());

        loadKategori();
        loadTable();
    }

    private void loadKategori() {
        ObservableList<KategoriBuku> list = FXCollections.observableArrayList(kategoriDAO.getAllKategori());
        kategoriComboBox.setItems(list);
    }

    private void loadTable() {
        try {
            ObservableList<Buku> list = FXCollections.observableArrayList(bukuDAO.getAllBuku());
            bukuTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data buku.");
        }
    }

    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            try {
                Buku buku = new Buku(
                        kodeField.getText(),
                        kategoriComboBox.getValue(),
                        judulField.getText(),
                        pengarangField.getText(),
                        penerbitField.getText(),
                        Integer.parseInt(tahunField.getText()),
                        Integer.parseInt(edisiField.getText()),
                        tanggalPengadaanPicker.getValue()
                );
                bukuDAO.insertBuku(buku);
                loadTable();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil ditambahkan.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan data.");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            try {
                Buku buku = new Buku(
                        kodeField.getText(),
                        kategoriComboBox.getValue(),
                        judulField.getText(),
                        pengarangField.getText(),
                        penerbitField.getText(),
                        Integer.parseInt(tahunField.getText()),
                        Integer.parseInt(edisiField.getText()),
                        tanggalPengadaanPicker.getValue()
                );
                bukuDAO.updateBuku(buku);
                loadTable();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil diperbarui.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data.");
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (!kodeField.getText().isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi");
            confirm.setHeaderText("Apakah Anda yakin ingin menghapus data?");
            confirm.setContentText("Kode: " + kodeField.getText());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        bukuDAO.deleteBuku(kodeField.getText());
                        loadTable();
                        clearFields();
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data.");
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Pilih data dari tabel atau isi kode buku.");
        }
    }

    private boolean isInputValid() {
        if (kodeField.getText().isEmpty() || kategoriComboBox.getValue() == null || judulField.getText().isEmpty() ||
                pengarangField.getText().isEmpty() || penerbitField.getText().isEmpty() ||
                tahunField.getText().isEmpty() || edisiField.getText().isEmpty() || tanggalPengadaanPicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field harus diisi.");
            return false;
        }

        if (!kodeField.getText().matches("^[A-Za-z0-9]{2,10}$")) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Kode buku hanya boleh huruf/angka (max 10 karakter).");
            return false;
        }

        if (!tahunField.getText().matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Tahun harus terdiri dari 4 digit angka.");
            return false;
        }

        if (!edisiField.getText().matches("^\\d{1,2}$")) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Edisi hanya boleh 1-2 digit angka.");
            return false;
        }

        try {
            Integer.parseInt(tahunField.getText());
            Integer.parseInt(edisiField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Tahun dan Edisi harus berupa angka.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleTableClick() {
        Buku selected = bukuTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            kodeField.setText(selected.getKodeBuku());
            kategoriComboBox.setValue(selected.getKategori());
            judulField.setText(selected.getJudul());
            pengarangField.setText(selected.getPengarang());
            penerbitField.setText(selected.getPenerbit());
            tahunField.setText(String.valueOf(selected.getTahunTerbit()));
            edisiField.setText(String.valueOf(selected.getEdisi()));
            tanggalPengadaanPicker.setValue(selected.getTanggalPengadaan());
        }
    }

    private void clearFields() {
        kodeField.clear();
        kategoriComboBox.getSelectionModel().clearSelection();
        judulField.clear();
        pengarangField.clear();
        penerbitField.clear();
        tahunField.clear();
        edisiField.clear();
        tanggalPengadaanPicker.setValue(null);
    }
}
