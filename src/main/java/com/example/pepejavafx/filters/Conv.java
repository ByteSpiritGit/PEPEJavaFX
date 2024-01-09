package com.example.pepejavafx.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Conv implements IFilter{

    private int[][] filterMatrix;

    public Conv(int[][] filterMatrix) {
        this.filterMatrix = filterMatrix;
    }

    public Conv() {
        this.filterMatrix = new int[][]{
                {1, 2, 1},
                {2, 5, 2},
                {1, 2, 1}
        };
    }

    // Function to apply convolution on a 2D array
    public BufferedImage applyFilter(BufferedImage image) {
        return ApplyConvolution(image, filterMatrix);
    }

    // Function to apply convolution on a BufferedImage
    public BufferedImage ApplyConvolution(BufferedImage image, int[][] filterMatrix) {
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

    public int getPixelValue(int[][] filterMatrix, BufferedImage image, int i, int j) {
        int sum = 0;
        for (int k = -1; k <= 1; k++) {
            Color pixel;
            for (int l = -1; l <= 1; l++){
                pixel = new Color(image.getRGB(j + l, i + k));
                sum += pixel.getRed() * filterMatrix[k + 1][l + 1];
            }
        }

        return sum;
    }}
