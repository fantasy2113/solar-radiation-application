package org.josmer.app.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {

    public final boolean isConnected() {
        try {
            Connection connection = getConnection();
            connection.close();
            return true;
        } catch (SQLException | URISyntaxException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    protected final Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
