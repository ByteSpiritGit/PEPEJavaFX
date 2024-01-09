package com.example.pepejavafx.filters;

import java.awt.image.BufferedImage;

public class Thresholding implements IFilter{

    public BufferedImage applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int threshold = 127;
        int p;
        int a;
        int r;
        int g;
        int b;
        int avg;

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                p = image.getRGB(i, j);
                a = (p>>24)&0xff;
                r = (p>>16)&0xff;
                g = (p>>8)&0xff;
                b = p&0xff;

                avg = (r+g+b)/3;

                if(avg > threshold){
                    r = 255;
                    g = 255;
                    b = 255;
                }else{
                    r = 0;
                    g = 0;
                    b = 0;
                }

                p = (a<<24) | (r<<16) | (g<<8) | b;

                result.setRGB(i, j, p);
            }
        }

        return result;
    }
}
