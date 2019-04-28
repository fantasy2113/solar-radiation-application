package de.josmer.app.model.repositories;

import de.josmer.app.library.interfaces.IUserRepository;
import de.josmer.app.library.utils.Toolbox;
import de.josmer.app.model.entities.User;
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
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement("SELECT * FROM users WHERE id = ? LIMIT 1")) {
            preparedStatement.setInt(1, id);
            return getData(preparedStatement);
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> get(final String login) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement("SELECT * FROM users WHERE login = ? LIMIT 1")) {
            preparedStatement.setString(1, login);
            return getData(preparedStatement);
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<User> getData(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                return Optional.ofNullable(mapToEntity(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveUser(final String login, final String plainPassword) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement("INSERT INTO users (login,password,is_active) VALUES (?,?,?)")) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, Toolbox.hashPassword(plainPassword));
            preparedStatement.setBoolean(3, true);
            preparedStatement.executeUpdate();
            connection.commit();
            LOGGER.info("insert user");
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
