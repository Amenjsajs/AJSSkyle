package com.ajs.db.factory;

import com.ajs.Utils.SkyleConnection;
import com.ajs.db.dao.MessageDAO;
import com.ajs.db.dao.UserDAO;

import java.sql.Connection;

public class DaoFactory extends AbstractDaoFactory{
    public static final Connection connection = SkyleConnection.getIntance();

    @Override
    public UserDAO getUserDAO() {
        return new UserDAO(connection);
    }

    @Override
    public MessageDAO getMessageDAO() {
        return new MessageDAO(connection);
    }
}
