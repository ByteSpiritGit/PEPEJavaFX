package com.example.pepejavafx.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MyConv implements IFilter{

    private float[][] filterMatrix;

    public MyConv(float[][] filterMatrix) {
        this.filterMatrix = filterMatrix;
    }

    public MyConv() {
        this.filterMatrix = new float[][]{
                {-1f, -1f, -1f},
                {-1f, 8f, -1f},
                {-1f, -1f, -1f}
        };
    }

    // Function to apply convolution on a 2D array
    public BufferedImage applyFilter(BufferedImage image) {
        return ApplyConvolution(image, filterMatrix);
    }

    // Function to apply convolution on a BufferedImage
    public BufferedImage ApplyConvolution(BufferedImage image, float[][] filterMatrix) {
        // Get height and width of
        int rows = image.getHeight();
        int cols = image.getWidth();
        // New buffered image
        BufferedImage newImage = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

        // Apply convolution
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {

                int sum = getPixelValue(filterMatrix, image, i, j);
                newImage.setRGB(j, i, sum);
            }
        }

        return newImage;
    }

    public int getPixelValue(float[][] filterMatrix, BufferedImage image, int i, int j) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        for (int k = -1; k <= 1; k++) {
            Color pixel;
            for (int l = -1; l <= 1; l++){
                pixel = new Color(image.getRGB(j + l, i + k));
                redSum += (int) (pixel.getRed() * filterMatrix[k + 1][l + 1]);
                greenSum += (int) (pixel.getGreen() * filterMatrix[k + 1][l + 1]);
                blueSum += (int) (pixel.getBlue() * filterMatrix[k + 1][l + 1]);
            }
        }
        int red = Math.min(255, Math.max(0, redSum));
        int green = Math.min(255, Math.max(0, greenSum));
        int blue = Math.min(255, Math.max(0, blueSum));

        return new Color(red, green, blue).getRGB();
    }}
