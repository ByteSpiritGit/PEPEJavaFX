package com.example.pepejavafx;

import javafx.application.Platform;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageController {

    private History history = new History();
    private String imagePath;
    private Image originalImage;  // Add this field

    @FXML
    private ImageView imageView;

    @FXML
    private Menu openRecentMenu;

    @FXML
    private RadioButton radioButtonOriginal;

    @FXML
    private RadioButton radioButtonModified;

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

    @FXML
    private void generateImage() {
        Image img = makeColoredImage();
        originalImage = img;
        redrawPanel();

        // Enable/disable UI components as needed
        radioButtonOriginal.setSelected(true);
        radioButtonModified.setDisable(false);
    }

    private Image makeColoredImage() {
        int width = 600;
        int height = 600;

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        Platform.runLater(() -> {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Creating a colored image based on the given logic
                    int r = x % 255;
                    int g = y % 255;
                    int b = (x + y) % 55;

                    Color color = Color.rgb(r, g, b);
                    pixelWriter.setColor(x, y, color);
                }
            }
        });

        return writableImage;
    }

    private void displayImage(String imagePath) {
        if (!imagePath.isEmpty()) {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            history.save(imagePath);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);

            openRecentMenu.getItems().clear();
            createRecentMenu();
        }
    }

    private void redrawPanel() {
        // Add logic to redraw the panel based on the generated image
        imageView.setImage(originalImage);
        imageView.setPreserveRatio(true);

        // Additional logic for redrawing the panel if needed
    }

    private void createRecentMenu() {
        String[] historyArray = history.getHistory();
        for (int i = 0; i < 5; i++) {
            if (historyArray[i] != null) {
                MenuItem menuItem = new MenuItem(historyArray[i]);
                menuItem.setOnAction(this::displayRecent);
                openRecentMenu.getItems().add(menuItem);
            }
        }
    }

    @FXML
    private void displayRecent(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String url = menuItem.getText();
        displayImage(url);
    }
}
