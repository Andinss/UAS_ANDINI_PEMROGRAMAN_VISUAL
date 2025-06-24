/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m8.tugas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import m8.tugas.models.Session;

/**
 *
 * @author LENOVO
 */
public class DashboardController {

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Button dashboardButton, logoutButton;

    @FXML
    private void initialize() {
        // Cek apakah user sudah login
        if (!Session.isLoggedIn()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/m8/tugas/views/Login.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Dulu");
                stage.show();

                // Tutup jendela sekarang (Dashboard)
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("Dashboard terbuka");
    }

    @FXML
    private void handleDashboardAction(ActionEvent event) {
        setMainContent("/m8/tugas/views/buku_view.fxml");
    }

    @FXML
    private void handleButtonLogoutAction() {
        try {
            Session.setIsLoggedIn(false); // logout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/m8/tugas/views/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            mainContent.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}