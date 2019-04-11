package de.josmer.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Repository<E> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Repository.class.getName());
    private final String databaseUrl;

    public Repository(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public Repository() {
        this.databaseUrl = System.getenv("DATABASE_URL");
    }

    protected Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }

    protected abstract E map(ResultSet rs) throws SQLException;
}
