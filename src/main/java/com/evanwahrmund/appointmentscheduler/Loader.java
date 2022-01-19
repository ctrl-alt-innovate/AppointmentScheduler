package com.evanwahrmund.appointmentscheduler;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Utility class for loading FXML files.
 */
public class Loader {
    /**
     * Loads a FXML file, assigns title, stylesheet, and initializes modality
     * @param fxml String of relative file location
     * @param title String to name window
     */
    public static void Load(String fxml, String title){
        try{
            FXMLLoader loader = new FXMLLoader(Loader.class.getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Loader.class.getResource("STYLESHEET.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.show();
        }catch (IOException ex){
            //ex.printStackTrace();
            System.out.println("Error: Could not load FXML.");
        }
    }
}
