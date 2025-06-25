/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package m8.tugas.controllers;

import java.io.FileWriter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import java.io.File;
import java.util.Comparator;

public class BukuController implements Initializable {

    @FXML private TextField kodeField, judulField, pengarangField, penerbitField, tahunField, edisiField, searchField;
    @FXML private DatePicker tanggalPengadaanPicker;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<KategoriBuku> kategoriComboBox;
    @FXML private ComboBox<String> sortComboBox;

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
        sortComboBox.setItems(FXCollections.observableArrayList("Judul A-Z","Judul Z-A","Pengadaan Terbaru","Pengadaan Terlama","Kategori A-Z"));

        bukuTable.setOnMouseClicked(event -> handleTableClick());

        loadKategori();
        loadTable();
    }

    private void loadKategori() {
        ObservableList<KategoriBuku> list = FXCollections.observableArrayList(kategoriDAO.getAllKategori());
        kategoriComboBox.setItems(list);
        
        kodeField.setText(bukuDAO.generateKodeBuku());
        kodeField.setEditable(false); // agar tidak bisa diubah manual
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
    
    @FXML
    private void handleFilter() {
        String keyword = searchField.getText().toLowerCase();
        String selectedSort = sortComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        try {
            List<Buku> list = bukuDAO.getAllBuku();
            List<Buku> filtered = new ArrayList<>();

            for (Buku buku : list) {
                boolean cocokKeyword = buku.getJudul().toLowerCase().contains(keyword) ||
                                       buku.getPengarang().toLowerCase().contains(keyword);

                boolean cocokTanggal = true;
                if (startDate != null && endDate != null) {
                    cocokTanggal = !buku.getTanggalPengadaan().isBefore(startDate) &&
                                   !buku.getTanggalPengadaan().isAfter(endDate);
                } else if (startDate != null) {
                    cocokTanggal = !buku.getTanggalPengadaan().isBefore(startDate);
                } else if (endDate != null) {
                    cocokTanggal = !buku.getTanggalPengadaan().isAfter(endDate);
                }

                if (cocokKeyword && cocokTanggal) {
                    filtered.add(buku);
                }
            }

            // Sorting
            if (selectedSort != null) {
                switch (selectedSort) {
                    case "Judul A-Z" -> filtered.sort(Comparator.comparing(Buku::getJudul));
                    case "Judul Z-A" -> filtered.sort(Comparator.comparing(Buku::getJudul).reversed());
                    case "Pengadaan Terbaru" -> filtered.sort(Comparator.comparing(Buku::getTanggalPengadaan).reversed());
                    case "Pengadaan Terlama" -> filtered.sort(Comparator.comparing(Buku::getTanggalPengadaan));
                    case "Kategori A-Z" -> filtered.sort(Comparator.comparing(b -> b.getKategori().getNamaKategori()));
                }
            }

            bukuTable.setItems(FXCollections.observableArrayList(filtered));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal melakukan filter/sort.");
        }
    }
    
    @FXML
    private void handleReset() {
        searchField.clear();
        sortComboBox.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        loadTable(); 
    }
    
    @FXML
    private void handleExportCSV() {
        ObservableList<Buku> list = bukuTable.getItems();

        if (list.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Kosong", "Tidak ada data untuk diekspor.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Laporan Buku");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("buku_export.csv");

        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return; // User cancel
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Kode Buku,Kategori,Judul,Pengarang,Penerbit,Tahun,Edisi,Tanggal Pengadaan\n");

            for (Buku buku : list) {
                writer.write(String.format("%s,%s,%s,%s,%s,%d,%d,%s\n",
                        buku.getKodeBuku(),
                        buku.getKategori().getNamaKategori(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        buku.getPenerbit(),
                        buku.getTahunTerbit(),
                        buku.getEdisi(),
                        buku.getTanggalPengadaan()));
            }

            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil diekspor ke: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Export gagal: " + e.getMessage());
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
        kodeField.setText(bukuDAO.generateKodeBuku()); // Generate ulang
        kategoriComboBox.getSelectionModel().clearSelection();
        judulField.clear();
        pengarangField.clear();
        penerbitField.clear();
        tahunField.clear();
        edisiField.clear();
        tanggalPengadaanPicker.setValue(null);
    } 
}
