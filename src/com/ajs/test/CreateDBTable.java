package com.ajs.test;

import com.ajs.utils.SkyleConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDBTable {
    public static void main(String[] args) {
        try (Connection con = SkyleConnection.getIntance()){
            
            String query ="CREATE SEQUENCE message_id_seq INCREMENT BY 1 MINVALUE 1 START 1;"+
                    "CREATE SEQUENCE users_id_seq INCREMENT BY 1 MINVALUE 1 START 1;"+
                    "CREATE TABLE message (id INT NOT NULL, sender_id INT NOT NULL, receiver_id INT NOT NULL, type VARCHAR(255) NOT NULL, content TEXT NOT NULL, msg_height INT NOT NULL, created_date TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL, PRIMARY KEY(id));"+
                    "CREATE TABLE users (id INT NOT NULL, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, birth_date DATE NOT NULL, password VARCHAR(255) NOT NULL, legend VARCHAR(255) DEFAULT NULL, avatar BYTEA DEFAULT NULL, PRIMARY KEY(id));"+
                    "CREATE UNIQUE INDEX UNIQ_1483A5E9E7927C74 ON users (email);";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)){
                System.out.println(preparedStatement.execute());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
