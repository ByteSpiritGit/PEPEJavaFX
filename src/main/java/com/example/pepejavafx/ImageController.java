package com.example.pepejavafx;

import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class ImageController {

    private History history = new History();
    private String imagePath;

    @FXML
    private ImageView imageView;

    @FXML
    private Menu openRecentMenu;

    @FXML
    private void initialize() {
    }

    @FXML
    private void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            displayImage(imagePath);
        }
    }

    private void displayImage(String imagePath) {
        if (!imagePath.isEmpty()) {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            history.save(imagePath);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            // Update recent menu
            openRecentMenu.getItems().clear();
            createRecentMenu();
        }
    }

    private void createRecentMenu() {
        String[] historyArray = history.getHistory();
        for (int i = 0; i < 5; i++) {
            if (historyArray[i] != null) {
                System.out.println(historyArray[i]);
                MenuItem menuItem = new MenuItem(historyArray[i]);
                menuItem.setOnAction(this::displayRecent);
                openRecentMenu.getItems().add(menuItem);
            }
        }
        System.out.println("");
    }

    @FXML
    private void displayRecent(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String url = menuItem.getText();
        displayImage(url);
    }
}
