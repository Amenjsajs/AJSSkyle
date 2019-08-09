package com.ajs.test;

import javax.swing.*;
import java.awt.*;

public class ImageTest {
    public static void main(String[] args) {
        Image image = new ImageIcon("sauvegarde/avatar/amenjsajs@gmail.com.png").getImage();
        System.out.println(image.getHeight(null));
    }
}
