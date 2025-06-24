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
import javafx.scene.image.Image;
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
        javafx.application.Platform.runLater(() -> {
            if (!Session.isLoggedIn()) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/m8/tugas/views/Login.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Ma'soem University");

                    Image icon = new Image(getClass().getResourceAsStream("/assets/img/icon.png"));
                    stage.getIcons().add(icon);

                    stage.show();

                    Stage currentStage = (Stage) mainContent.getScene().getWindow();
                    currentStage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Dashboard terbuka");
            }
        });
    }

    @FXML
    private void handleDashboardAction(ActionEvent event) {
        setMainContent("/m8/tugas/views/buku_view.fxml");
    }

    @FXML
    private void handleButtonLogoutAction() {
        try {
            Session.clear(); 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/m8/tugas/views/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ma'soem University");

            Image icon = new Image(getClass().getResourceAsStream("/assets/img/icon.png"));
            stage.getIcons().add(icon); 

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