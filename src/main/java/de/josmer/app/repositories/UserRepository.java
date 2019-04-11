package de.josmer.app.repositories;

import de.josmer.app.entities.User;
import de.josmer.app.lib.interfaces.IUserRepository;
import de.josmer.app.lib.utils.Toolbox;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class UserRepository extends Repository<User> implements IUserRepository {
    public UserRepository(final String databaseUrl) {
        super(databaseUrl);
    }

    public UserRepository() {
        super();
    }

    @Override
    protected User mapToEntity(ResultSet rs) throws SQLException {
        User user = new User();
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setActive(rs.getBoolean("is_active"));
        user.setId(rs.getInt("id"));
        return user;
    }

    @Override
    public Optional<User> get(final int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ? LIMIT 1");
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return Optional.ofNullable(mapToEntity(rs));
                }
            } catch (SQLException | URISyntaxException e) {
                LOGGER.info(e.getMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> get(final String login) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ? LIMIT 1");
                preparedStatement.setString(1, login);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return Optional.ofNullable(mapToEntity(rs));
                }
            } catch (SQLException | URISyntaxException e) {
                LOGGER.info(e.getMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void saveUser(final String login, final String plainPassword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement("INSERT INTO users (login,password,is_active) VALUES (?,?,?)");
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, Toolbox.hashPassword(plainPassword));
                preparedStatement.setBoolean(3, true);
                preparedStatement.executeUpdate();
                connection.commit();
                LOGGER.info("insert user");
            } catch (SQLException | URISyntaxException e) {
                LOGGER.info(e.getMessage());
                if (connection != null) {
                    connection.rollback();
                }
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
