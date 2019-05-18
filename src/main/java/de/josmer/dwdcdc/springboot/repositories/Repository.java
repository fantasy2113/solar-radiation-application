package de.josmer.dwdcdc.springboot.repositories;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class Repository<E> {
    private final String databaseUrl;

    Repository(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    Repository() {
        this.databaseUrl = System.getenv("DATABASE_URL");
    }

    Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }

    protected abstract E mapToEntity(ResultSet rs) throws SQLException;
}
