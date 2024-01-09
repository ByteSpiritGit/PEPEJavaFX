package com.example.pepejavafx.filters;

import java.awt.image.BufferedImage;

public class Conv implements IFilter{

    private Float[][] filterMatrix;

    public Conv(Float[][] filterMatrix) {
        this.filterMatrix = filterMatrix;
    }

    public Conv() {
        this.filterMatrix = new Float[][]{
                {0.0f, 0.0f, 0.0f},
                {0.0f, 1.0f, 0.0f},
                {0.0f, 0.0f, 0.0f}
        };
    }

    public BufferedImage applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int filterWidth = filterMatrix.length;
        int filterHeight = filterMatrix[0].length;

        int filterOffset = (filterWidth - 1) / 2;
        int calcOffset;

        int byteOffset = 0;

        int[] pixels = new int[width * height];
        int[] newPixels = new int[width * height];

        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int offsetY = filterOffset; offsetY < height - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX < width - filterOffset; offsetX++) {
                int newBlue = 0;
                int newGreen = 0;
                int newRed = 0;

                byteOffset = offsetY * width + offsetX;

                for (int filterY = -filterOffset; filterY <= filterOffset; filterY++) {
                    for (int filterX = -filterOffset; filterX <= filterOffset; filterX++) {
                        calcOffset = byteOffset + (filterX * 4) + (filterY * width * 4);
                        newBlue += (int) ((pixels[calcOffset] & 0xff) * filterMatrix[filterY + filterOffset][filterX + filterOffset]);
                        newGreen += (int) (((pixels[calcOffset] >> 8) & 0xff) * filterMatrix[filterY + filterOffset][filterX + filterOffset]);
                        newRed += (int) (((pixels[calcOffset] >> 16) & 0xff) * filterMatrix[filterY + filterOffset][filterX + filterOffset]);
                    }
                }

                newBlue = Math.min(Math.max(newBlue, 0), 255);
                newGreen = Math.min(Math.max(newGreen, 0), 255);
                newRed = Math.min(Math.max(newRed, 0), 255);

                newPixels[byteOffset] = (255 << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
            }
        }

        newImage.setRGB(0, 0, width, height, newPixels, 0, width);

        return newImage;
    }
}
