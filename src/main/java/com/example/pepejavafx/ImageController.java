package com.example.pepejavafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


import static com.example.pepejavafx.ImageUtils.LoadImage;
import static com.example.pepejavafx.ImageUtils.makeColoredImage;

import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Node;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.ResourceBundle;


public class ImageController {

    private final History history = new History();

    private MyImage myImage;

    @FXML
    private ImageView imageView;

    @FXML
    private Menu openRecentMenu;

    @FXML
    private void initialize() {
        // Initialize your UI components
    }

    @FXML
    public void exitMenuClick(ActionEvent event) { Platform.exit(); }

    @FXML
    private void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            String imagePath = selectedFile.getAbsolutePath();
            myImage = new MyImage(LoadImage(imagePath), null, imagePath);

            imagePath = selectedFile.getAbsolutePath();
            displayImage(imagePath);
        }
    }

    @FXML
    private void saveImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(), null), "png", selectedFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void displayRecent(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String url = menuItem.getText();
        displayImage(url);
    }

    @FXML
    private void generateImage() {
        BufferedImage image = makeColoredImage();
        imageView.setImage(SwingFXUtils.toFXImage(image, null));

        myImage = new MyImage(image, null, null);
    }

    @FXML
    private void applyNegative() {
        try {
            BufferedImage originalImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            BufferedImage filteredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    int rgbOrig = originalImage.getRGB(x, y);
                    Color c = new Color(rgbOrig);
                    int r = 255 - c.getRed();
                    int g = 255 - c.getGreen();
                    int b = 255 - c.getBlue();
                    Color nc = new Color(r, g, b);
                    filteredImage.setRGB(x, y, nc.getRGB());
                }
            }
            myImage.modifiedImage = filteredImage;
            imageView.setImage(SwingFXUtils.toFXImage(myImage.modifiedImage, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void applyGrayscale() {
        try {
            BufferedImage originalImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            BufferedImage filteredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    int rgbOrig = originalImage.getRGB(x, y);
                    Color c = new Color(rgbOrig);
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int avg = (r + g + b) / 3;
                    Color nc = new Color(avg, avg, avg);
                    filteredImage.setRGB(x, y, nc.getRGB());
                }
            }
            imageView.setImage(SwingFXUtils.toFXImage(filteredImage, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayImage(BufferedImage image) {
        if (image != null) {
            imageView.setImage(SwingFXUtils.toFXImage(image, null));
            imageView.setPreserveRatio(true);
            openRecentMenu.getItems().clear();
            createRecentMenu();
        }
    }

    private void displayImage(String url) {
        if (url != null) {
            BufferedImage image = LoadImage(url);
            myImage = new MyImage(image, null, url);
            imageView.setImage(SwingFXUtils.toFXImage(myImage.getOriginalImage(), null));
            imageView.setPreserveRatio(true);
            openRecentMenu.getItems().clear();
            createRecentMenu();
        }
    }

    private void createRecentMenu() {
        String[] historyArray = history.getHistory();
        for (int i = 0; i < historyArray.length; i++) {
            if (historyArray[i] != null) {
                MenuItem menuItem = new MenuItem(historyArray[i]);
                menuItem.setOnAction(this::displayRecent);
                openRecentMenu.getItems().add(menuItem);
            }
        }
    }
}
