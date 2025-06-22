/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package m8.tugas;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();

        // Memuat file FXML
        Parent root = FXMLLoader.load(getClass().getResource("/m8/tugas/views/login.fxml"));

        // Membuat scene dengan root dari FXML
        Scene scene = new Scene(root);

        // Mengatur stage (jendela utama)
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ma'soem University");
        Image icon = new Image(getClass()
                .getResourceAsStream("/assets/img/icon.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }
    
    public void changeScene(String fxmlFile) throws Exception {

    Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlFile));

    // mengambil ukuran root container pada file fxml
    double width = newRoot.prefWidth(-1);  // -1 nilai preferensi
    double height = newRoot.prefHeight(-1); // -1 nilai preferensi

    primaryStage.getScene().setRoot(newRoot);
    primaryStage.setWidth(width);
    primaryStage.setHeight(height);
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}