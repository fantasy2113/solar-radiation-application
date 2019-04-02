package org.josmer.interfaces;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface IRepository<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);
}
