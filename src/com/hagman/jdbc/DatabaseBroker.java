package com.hagman.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @since 09/24/2014
 * @author tom
 */
public class DatabaseBroker {

    private String connectionString = null;
    private String username = null;
    private String password = null;

    public DatabaseBroker() {
    }

    public DatabaseBroker(String connectionString, String username, String password) {
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
        init();
    }

    private void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to access driver.", e);
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            connection.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get initial connection.", e);
        }


    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection", e);
        }
    }

}
