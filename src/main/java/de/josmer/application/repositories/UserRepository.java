package de.josmer.application.repositories;

import de.josmer.application.entities.User;
import de.josmer.application.interfaces.IUserRepository;
import org.springframework.stereotype.Component;

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
    protected User map(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public Optional<User> get(int id) {


        return null;
    }

    @Override
    public Optional<User> get(String login) {
        return null;
    }
}
