package com.example.pepejavafx;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class ImageUtils {

    public static BufferedImage LoadImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = javax.imageio.ImageIO.read(new java.io.File(imagePath));
        } catch (Exception e) {
            System.out.println("Error loading image");
        }
        return image;
    }

    static BufferedImage makeColoredImage() {
        BufferedImage bImage = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < bImage.getWidth(); x++) {
            for (int y = 0; y < bImage.getHeight(); y++) {
                bImage.setRGB(x, y, (new Color((x + 69) % 255, (x * 5) % 255, (x * y) % 255).getRGB()));
            }
        }
        return bImage;
    }

}
