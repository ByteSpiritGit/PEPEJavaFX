package com.example.pepejavafx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.control.SplitPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.example.pepejavafx.ImageUtils.LoadImage;
import static com.example.pepejavafx.ImageUtils.makeColoredImage;


public class ImageController {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private AnchorPane rightMainAnchorPane;

    private final History history = new History();

    private MyImage myImage;

    @FXML
    private ImageView imageView;

    @FXML
    private Menu openRecentMenu;

    @FXML
    private void initialize() {
        mainSplitPane.setDividerPositions(0.7);
        rightMainAnchorPane.setMinWidth(275);
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

    @FXML
    private void showAboutUs() {
        VBox vbox = new VBox();
        vbox.setPrefWidth(300);
        vbox.setPrefHeight(300);
        vbox.setStyle("-fx-background-color: #FFFFFF;");
        vbox.setSpacing(10);

        Text title = new Text("O nás");
        Text description = new Text("Vytvořeno s láskou pro PEPEho");

        Text teamLabel = new Text("Tým: ");
        teamLabel.setStyle("-fx-font-weight: bold;");

        Text teamMembers = new Text("Ondřej Šteffan, Jenda Soukeník, David Vrtílek"); // Replace with your team members
        Text version = new Text("Verze: 1.0");

        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(teamLabel, teamMembers, new Text("\n"), version);

        vbox.getChildren().addAll(title, description, textFlow);

        Stage stage = new Stage();
        stage.setTitle("O nás");
        stage.setScene(new Scene(vbox));
        stage.show();
    }

    @FXML
    private void showAboutApplication() {
        VBox vbox = new VBox();
        vbox.setPrefWidth(300);
        vbox.setPrefHeight(300);
        vbox.setStyle("-fx-background-color: #FFFFFF;");
        vbox.setSpacing(10);

        Label label = new Label("O aplikaci");
        Label label1 = new Label("Aplikace splňuje všechny požadavky zadání.");
        Label label3 = new Label("Version: 1.0");

        vbox.getChildren().addAll(label, label1, label3);

        Stage stage = new Stage();
        stage.setTitle("O aplikaci");
        stage.setScene(new Scene(vbox));
        stage.show();
    }
}
