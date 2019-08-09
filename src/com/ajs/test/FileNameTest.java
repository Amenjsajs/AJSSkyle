package com.ajs.test;

import java.io.File;

public class FileNameTest {
    public static void main(String[] args) {
        File file = new File("sauvegarde/amenjs.png");
        int pos = file.getName().lastIndexOf(".");

        System.out.println(file.getName().substring(pos+1));
    }
}
