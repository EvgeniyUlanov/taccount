package ru.job4j.eulanov.dbconnection;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionPool {

    private static BasicDataSource dataSource;

    public static synchronized BasicDataSource getDataSource() throws ClassNotFoundException {
        if (dataSource == null) {
            Class.forName("org.postgresql.Driver");
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:postgresql://localhost:5432/time_account");
            ds.setUsername("postgres");
            ds.setPassword("784512963");
            ds.setMinIdle(5);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(1000);
            dataSource = ds;
        }
        return dataSource;
    }

    public static synchronized void closeConnection() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
