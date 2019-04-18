package de.josmer.application.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class ARepository<E> {
    static final Logger LOGGER = LoggerFactory.getLogger(ARepository.class.getName());
    private final String databaseUrl;

    ARepository(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    ARepository() {
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
