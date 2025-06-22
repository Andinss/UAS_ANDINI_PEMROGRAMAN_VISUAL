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
        System.out.println("Dashboard terbuka");
    }

    @FXML
    private void handleDashboardAction(ActionEvent event) {
        setMainContent("/m8/tugas/views/buku_view.fxml");
    }

    @FXML
    private void handleButtonLogoutAction() {
        try {
            Session.setLoggedIn(false); 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/m8/tugas/views/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ma'soem University");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setMainContent(String fxml) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        javafx.scene.Parent newContent = loader.load(); 
        mainContent.getChildren().setAll(newContent);

        AnchorPane.setTopAnchor(newContent, 0.0);
        AnchorPane.setBottomAnchor(newContent, 0.0);
        AnchorPane.setLeftAnchor(newContent, 0.0);
        AnchorPane.setRightAnchor(newContent, 0.0);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}

