package com.ajs.db.dao;

import com.ajs.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MessageDAO extends DAO<Message> {
    public MessageDAO(Connection connection) {
        super(connection);
    }

    @Override
    public boolean create(Message obj) {
        String query = "INSERT INTO message(id, sender_id, receiver_id, type, content, msg_height, created_date, file_name) VALUES (nextval('users_id_seq'),?,?,?,?,?,?,?)";
        try (PreparedStatement preReq = connection.prepareStatement(query)) {
            preReq.setInt(1, obj.getSender().getId());
            preReq.setInt(2, obj.getReceiver().getId());
            preReq.setString(3, obj.getType());
            preReq.setString(4, obj.getContent());
            preReq.setInt(5, obj.getMsgHeight());
            preReq.setDate(6, new java.sql.Date(obj.getCreatedDate().getTime()));
            preReq.setString(7, obj.getFileName());
            preReq.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Message obj) {
        return false;
    }

    @Override
    public boolean update(Message obj) {
        return false;
    }

    @Override
    public Message findById(int id) {
        return null;
    }

    @Override
    public List<Message> find(int offset, int limit) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return null;
    }
}
