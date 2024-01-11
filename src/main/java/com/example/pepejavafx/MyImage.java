package com.example.pepejavafx;

import java.awt.image.BufferedImage;

public class MyImage {

    private BufferedImage image;
//    public BufferedImage modifiedImage;

    public String imagePath;

    public MyImage() {
        image = null;
        imagePath = "";
    }

    public MyImage(BufferedImage originalImage, String imagePath) {
        this.image = originalImage;
        this.imagePath = imagePath;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public MyImage clone() {
        return new MyImage(image, imagePath);
    }
}
