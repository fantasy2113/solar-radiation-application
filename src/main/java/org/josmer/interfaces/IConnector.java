package org.josmer.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface IConnector<T> {

    T getEntity(PreparedStatement preparedStatement) throws SQLException;

    List<T> getEntities(PreparedStatement preparedStatement) throws SQLException;

    int updateEntity(PreparedStatement preparedStatement) throws SQLException;

    int saveAll(List<T> entities) throws SQLException;
}
