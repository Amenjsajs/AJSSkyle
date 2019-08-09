package com.ajs.db.factory;

import com.ajs.db.dao.MessageDAO;
import com.ajs.db.dao.UserDAO;

public abstract class AbstractDaoFactory {
    public static final int DAO_FACTORY = 0;

    public abstract UserDAO getUserDAO();

    public abstract MessageDAO getMessageDAO();

    public static AbstractDaoFactory getFactory(int type) {
        if (type == DAO_FACTORY) return new DaoFactory();
        return null;
    }
}