package com.ajs.db.dao;

import java.sql.Connection;
import java.util.List;

public abstract class DAO<T> {
    protected Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }

    protected abstract boolean create(T obj);

    protected abstract boolean delete(T obj);

    protected abstract boolean update(T obj);

    protected abstract T findById(int id);

    protected abstract List<T> find(int offset, int limit);

    protected abstract List<T> findAll();
}
