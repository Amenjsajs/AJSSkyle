package com.ajs.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SkyleConnection {
    private static final String driver = "org.postgresql.Driver";
    private static Connection instance;
    private static String url = "";
    private static String user = "";
    private static String password = "";

    static {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("config/config.json")) {
            JSONObject config = (JSONObject) parser.parse(reader);
            url = String.valueOf(config.get("url"));
            user = String.valueOf(config.get("user"));
            password = String.valueOf(config.get("password"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static Connection getIntance() {
        if (instance == null) {
            try {
                Class.forName(driver);
                instance = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion Ok");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        getIntance();
    }
}
