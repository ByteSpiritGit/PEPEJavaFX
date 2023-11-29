package com.example.pepejavafx;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class ImageController {

    private String imagePath;

    @FXML
    private ImageView imageView;

    @FXML
    private Button browseButton;

    @FXML
    private Button displayButton;

    @FXML
    private void initialize() {
        // You can initialize components or perform other setup here.
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
        }
        displayImage(event);
    }

    private void displayImage(ActionEvent event) {
        String imagePath = this.imagePath;
        if (!imagePath.isEmpty()) {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }
    }

    @FXML
    private void removeImage(ActionEvent event) {
        imageView.setImage(null);
    }
}
