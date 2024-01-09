package com.example.pepejavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageController {

    private History history = new History();
    private String imagePath;

    @FXML
    private ImageView imageView;

    @FXML
    private Menu openRecentMenu;

    @FXML
    private void initialize() {
        // Initialize your UI components
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

    @FXML
    private void generateImage() {
        BufferedImage image = makeColoredImage();
        imageView.setImage(SwingFXUtils.toFXImage(image, null));

        // Set the current file to a default value (you can modify this as needed)
        imagePath = "./unknown.png";
    }

    private BufferedImage makeColoredImage() {
        BufferedImage bImage = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < bImage.getWidth(); x++) {
            for (int y = 0; y < bImage.getHeight(); y++) {
                bImage.setRGB(x, y, (new Color((x + 10) % 255, (x * 20) % 255, (x * y) % 255).getRGB()));
            }
        }
        return bImage;
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
            imageView.setImage(SwingFXUtils.toFXImage(filteredImage, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
