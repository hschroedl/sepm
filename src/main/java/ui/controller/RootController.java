package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class RootController extends AbstractController{

    @FXML
    public void handleShowArticleOverview(){
        mainApp.showArticleOverview();
    }

    @FXML
    public void handleShowReceiptOverview(){
        mainApp.showReceiptOverview();
    }


    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Wendy's Easter Shop\n" +
                "Version 1.0\n");
        alert.setContentText("Author: Hans-Jörg Schrödl" +
                "\nWebsite: hschroedl.at");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}