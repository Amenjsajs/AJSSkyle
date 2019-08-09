package com.ajs.db.dao;

import com.ajs.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends DAO<User> {
    public UserDAO(Connection connection) {
        super(connection);
    }

    private boolean createOrUpdate(User user, boolean create) {
        String query;

        if(create)
            query = "INSERT INTO users(id, first_name, last_name, email, birth_date, password, legend, avatar_path) VALUES (nextval('users_id_seq'),?, ?, ?, ?, ?, ?, ?)";
        else
            query = "UPDATE users SET first_name = ?, last_name = ?, email = ?, birth_date = ?, password = ?, legend = ?, avatar_path = ? WHERE id = ?";

        try (PreparedStatement preReq = connection.prepareStatement(query)) {
            preReq.setString(1, user.getFirstName());
            preReq.setString(2, user.getLastName());
            preReq.setString(3, user.getEmail());
            preReq.setDate(4, new java.sql.Date(user.getBirthDate().getTime()));
            preReq.setString(5, user.getPassword());
            preReq.setString(6, user.getLegend());
            preReq.setString(7,user.getAvatarPath());
            preReq.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findByEmailAndPassword(String email, String password){
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement preReq = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            preReq.setString(1, email);
            preReq.setString(2, password);
            try (ResultSet resultSet = preReq.executeQuery()) {
                if (resultSet.first()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setBirthDate(resultSet.getDate("birth_date"));
                    user.setPassword(resultSet.getString("password"));
                    user.setLegend(resultSet.getString("legend"));
                    user.setAvatarPath(resultSet.getString("avatar_path"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean create(User obj) {
        return createOrUpdate(obj, true);
    }

    @Override
    public boolean delete(User obj) {
        String query = "DELETE FROM users WHERE email = ?";
        try (PreparedStatement preReq = connection.prepareStatement(query)) {
            preReq.setString(1, obj.getEmail());
            preReq.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User obj) {
        return createOrUpdate(obj, false);
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public List<User> find(int offset, int limit) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
