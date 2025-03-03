package io.kragelv.library.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import io.kragelv.library.dao.connection.ConnectionManager;

public abstract class AbstractDAOImpl {

    protected Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    protected void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                System.err.println("Failed to close resource: " + e.getMessage());
            }
        }
    }
}