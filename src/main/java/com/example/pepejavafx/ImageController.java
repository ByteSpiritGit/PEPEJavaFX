package com.example.pepejavafx;

import com.example.pepejavafx.filters.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.example.pepejavafx.ImageUtils.LoadImage;
import static com.example.pepejavafx.ImageUtils.makeColoredImage;

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
            history.save(imagePath);
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

    private void applyFilter(IFilter filter) {
        try {
            myImage.modifiedImage = filter.applyFilter(myImage.getOriginalImage());
            displayImage(myImage.modifiedImage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void applyNegative() {
        applyFilter(new Negative());
        displayImage(myImage.modifiedImage);
    }

    @FXML
    private void applyGrayscale() {
        applyFilter(new GrayScale());
        displayImage(myImage.modifiedImage);
    }

    @FXML
    private void applyThresholding() {
        applyFilter(new Thresholding());
        displayImage(myImage.modifiedImage);
    }

    @FXML
    private void applyConvolution() {
        applyFilter(new Conv());
        displayImage(myImage.modifiedImage);
    }


    private void displayImage(BufferedImage image) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
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
