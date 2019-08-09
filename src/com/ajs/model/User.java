package com.ajs.model;

import java.awt.*;
import java.io.*;
import java.util.Base64;
import java.util.Date;

public class User implements Comparable<User>, Serializable {
    private static final long serialVersionUID = -4425643939276759191L;
    private int id;
    private transient Image avatar;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String password;
    private String legend;
    private String avatarPath;
    private String avatarData;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void encodeAvatar() throws IOException {
        File file = new File(avatarPath);

        //Image conversion to byte array
        FileInputStream imageInFile;
        imageInFile = new FileInputStream(file);
        byte[] imageData = new byte[(int) file.length()];
        imageInFile.read(imageData);

        //File conversion byte array in Base64 String
        avatarData = Base64.getEncoder().encodeToString(imageData);
        imageInFile.close();
        System.out.println("Fin traitement du ficher!");
    }

    public byte[] decodeAvatar() {
        return Base64.getDecoder().decode(avatarData);
    }

    public String getAvatarName() {
        int pos = avatarPath.lastIndexOf("/");
        return avatarPath.substring(pos + 1);
    }

    public void saveAvatar() throws IOException {
        byte[] fileByteArray = decodeAvatar();
        try (FileOutputStream imageOutFile = new FileOutputStream(avatarPath)){
            imageOutFile.write(fileByteArray);
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%s,%s,%s,%s,%s,%s]", getClass().getName(), firstName, lastName, email, password, birthDate, legend);
    }

    @Override
    public int compareTo(User anotherUser) {
        return anotherUser != null ? email.compareTo(anotherUser.getEmail()) : -1;
    }
}