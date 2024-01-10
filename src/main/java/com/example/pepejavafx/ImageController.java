package com.example.pepejavafx;

import com.example.pepejavafx.filters.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
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

    @FXML
    private RadioButton modifiedImageRadioButton;

    @FXML
    RadioButton originalImageRadioButton;

    @FXML
    GridPane ConvMatrix;

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
            originalImageRadioButton.setSelected(true);
            modifiedImageRadioButton.setDisable(true);
            history.save(imagePath);
            imagePath = selectedFile.getAbsolutePath();
            displayImage(imagePath);
        }
    }

    @FXML
    private void saveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );
        File selectedFile = fileChooser.showSaveDialog(null);
        saveImage(selectedFile);
    }

    @FXML
    private void save(ActionEvent event) {
        if (myImage != null) {
            if (myImage.imagePath != null) {
                saveImage(new File(myImage.imagePath));
                return;
            }
            saveAs(event);
        }
    }



    private void saveImage(File file) {
        if (file != null) {
            try {
                myImage.imagePath = file.getAbsolutePath();
                history.save(myImage.imagePath);
                ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(), null), "png", file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }



    @FXML
    private void displayRecent(ActionEvent event) {
        originalImageRadioButton.setSelected(true);
        modifiedImageRadioButton.setDisable(true);
        MenuItem menuItem = (MenuItem) event.getSource();
        String url = menuItem.getText();
        displayImage(url);
    }

    @FXML
    private void generateImage() {
        BufferedImage image = makeColoredImage();
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
        originalImageRadioButton.setSelected(true);
        modifiedImageRadioButton.setDisable(true);
        myImage = new MyImage(image, null, null);
    }

    private void applyFilter(IFilter filter) {
        try {
            if (this.myImage == null) {
                    System.out.println("No image selected");
                    browseImage(null);
                if (this.myImage == null) {return;}
            }
            myImage.modifiedImage = filter.applyFilter(myImage.getOriginalImage());
            modifiedImageRadioButton.setDisable(false);
            modifiedImageRadioButton.setSelected(true);
            displayImage(myImage.modifiedImage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void applyNegative() {
        applyFilter(new Negative());
    }

    @FXML
    private void applyGrayscale() {
        applyFilter(new GrayScale());
    }

    @FXML
    private void applyThresholding() {
        applyFilter(new Thresholding());
    }

    @FXML
    private void applyConvolution() {
        if (getConvMatrix().length > 0) {
            applyFilter(new MyConv(getConvMatrix()));
            displayImage(myImage.modifiedImage);
            return;
        }
        applyFilter(new MyConv());
    }

    @FXML
    private void applyEdgeDetection() {
        applyFilter(new MyConv(new float[][]{
                {-1f, -1f, -1f},
                {-1f, 8f, -1f},
                {-1f, -1f, -1f}
        }));
    }

    @FXML
    private void applyEmboss() {
        applyFilter(new MyConv(new float[][]{
                {-2f, -1f, 0f},
                {-1f, 1f, 1f},
                {0f, 1f, 2f}
        }));
    }

    @FXML
    private void applySharpen() {
        applyFilter(new MyConv(new float[][]{
                {0f, -1f, 0f},
                {-1f, 5f, -1f},
                {0f, -1f, 0f}
        }));
    }



    private void displayImage(BufferedImage image) {
        if (image != null) {
            imageView.setImage(SwingFXUtils.toFXImage(image, null));
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
            for (String s : historyArray) {
                if (s != null) {
                    MenuItem menuItem = new MenuItem(s);
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
        vbox.setSpacing(5);
        vbox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        Text title = new Text("O nás");

        Text description = new Text("Vytvořeno s láskou pro PEPEho");

        Text teamLabel = new Text("Tým: ");
        teamLabel.setStyle("-fx-font-weight: bold;");

        Text teamMembers = new Text("Ondřej Šteffan, Jenda Soukeník, David Vrtílek");
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
        vbox.setSpacing(5);
        vbox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        Label label = new Label("O aplikaci");
        Label label1 = new Label("Aplikace splňuje všechny požadavky zadání.");
        Label label3 = new Label("Version: 1.0");

        vbox.getChildren().addAll(label, label1, label3);

        Stage stage = new Stage();
        stage.setTitle("O aplikaci");
        stage.setScene(new Scene(vbox));
        stage.show();
    }

    @FXML
    private void showOriginalImage() {
        if (myImage != null) {
            imageView.setImage(SwingFXUtils.toFXImage(myImage.getOriginalImage(), null));
        }
    }

    @FXML
    private void showModifiedImage() {
        if (myImage != null) {
            imageView.setImage(SwingFXUtils.toFXImage(myImage.modifiedImage, null));
        }
    }

    private float[][] getConvMatrix() {
        float[][] matrix = new float[3][3];
        for (int i = 0; i < ConvMatrix.getChildren().size(); i++) {
            TextField textField = (TextField) ConvMatrix.getChildren().get(i);
            if (textField.getText().isEmpty()) {
                return new float[0][0];
            }
            matrix[i / 3][i % 3] = Float.parseFloat(textField.getText());
        }
        return matrix;
    }
}
