package com.example.pepejavafx;

import java.awt.image.BufferedImage;

public class MyImage {

    private final BufferedImage originalImage;
    public BufferedImage modifiedImage;

    public String imagePath;

    public MyImage() {
        originalImage = null;
        modifiedImage = null;
        imagePath = "";
    }

    public MyImage(BufferedImage originalImage, BufferedImage modifiedImage, String imagePath) {
        this.originalImage = originalImage;
        this.modifiedImage = modifiedImage;
        this.imagePath = imagePath;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }
}
