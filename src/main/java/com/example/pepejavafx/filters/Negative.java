package com.example.pepejavafx.filters;

import java.awt.image.BufferedImage;

public class Negative implements IFilter{

    public BufferedImage applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int p = image.getRGB(i, j);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                p = (a<<24) | (r<<16) | (g<<8) | b;

                result.setRGB(i, j, p);
            }
        }

        return result;
    }

}
