/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m8.tugas.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import m8.tugas.models.Session;

/**
 *
 * @author LENOVO
 */

public class LoginController {

    @FXML private TextField inputUsername;
    @FXML private PasswordField inputPassword;
    @FXML private Label labelError;

    @FXML
    private void handleButtonLoginAction() {
        String user = inputUsername.getText();
        String pass = inputPassword.getText();

        if (user.equals("admin") && pass.equals("123")) {
            Session.setIsLoggedIn(true); 

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/m8/tugas/views/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) inputUsername.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Ma'soem University");

                Image icon = new Image(getClass().getResourceAsStream("/assets/img/icon.png"));
                stage.getIcons().add(icon); 

                stage.show(); 
            } catch (Exception e) {
                labelError.setText("Gagal membuka dashboard.");
                e.printStackTrace();
            }
        } else {
            labelError.setText("Username/password salah!");
        }
    }
}

